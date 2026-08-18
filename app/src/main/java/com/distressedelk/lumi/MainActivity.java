package com.distressedelk.lumi;

import android.app.*;
import android.os.*;
import android.provider.Settings;
import android.content.*;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.*;
import android.widget.*;
import android.text.InputType;
import android.graphics.drawable.GradientDrawable;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends Activity {
    LinearLayout root, content;
    TextView status;
    int accent = Color.rgb(127,232,255), bg = Color.rgb(12,17,24), panel = Color.rgb(21,28,38), text = Color.rgb(242,246,250), muted = Color.rgb(154,168,184);
    SharedPreferences prefs;

    @Override public void onCreate(Bundle b) {
        super.onCreate(b); prefs = getSharedPreferences("lumi", MODE_PRIVATE); showHome();
    }

    TextView tv(String s, int sp, int color) { TextView v=new TextView(this); v.setText(s); v.setTextSize(sp); v.setTextColor(color); v.setPadding(16,10,16,10); return v; }
    Button btn(String s) { Button b=new Button(this); b.setText(s); b.setTextColor(text); b.setTextSize(14); GradientDrawable g=new GradientDrawable(); g.setColor(panel); g.setCornerRadius(26); g.setStroke(1,accent); b.setBackground(g); b.setAllCaps(false); b.setPadding(12,6,12,6); return b; }
    void base(String title) {
        root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setBackgroundColor(bg); root.setPadding(18,18,18,18);
        TextView t=tv(title,24,text); t.setTypeface(Typeface.DEFAULT_BOLD); root.addView(t);
        status=tv("Lumi v0.2 • update channel active • Meta bridge ready",12,muted); root.addView(status);
        ScrollView sv=new ScrollView(this); content=new LinearLayout(this); content.setOrientation(LinearLayout.VERTICAL); content.setPadding(0,12,0,40); sv.addView(content); root.addView(sv,new LinearLayout.LayoutParams(-1,0,1));
        LinearLayout nav=new LinearLayout(this); nav.setGravity(Gravity.CENTER); String[] ns={"Home","Talk","Memory","Vault","Settings"};
        for(String n:ns){ Button b=btn(n); b.setOnClickListener(v->{switch(n){case"Home":showHome();break;case"Talk":showTalk();break;case"Memory":showMemory();break;case"Vault":openVault();break;case"Settings":showSettings();}}); nav.addView(b,new LinearLayout.LayoutParams(0,58,1)); }
        root.addView(nav); setContentView(root);
    }

    void showHome(){ base("Lumi");
        TextView avatar=tv("✧\nL U M I\n◜  ◝\n  •  •\n   ◡\n╰─────╯",30,accent); avatar.setGravity(Gravity.CENTER); avatar.setPadding(10,34,10,20); content.addView(avatar,new LinearLayout.LayoutParams(-1,290));
        TextView greeting=tv("Hey. I'm here. Update v0.2 is installed and your Lumi data stays with me.",18,text); greeting.setGravity(Gravity.CENTER); content.addView(greeting);
        Button overlay=btn("Show yourself • floating overlay"); overlay.setOnClickListener(v->showOverlay()); content.addView(overlay);
        TextView cards=tv("TODAY\n• Context Filter: "+prefs.getString("filter","Balanced")+"\n• Proactive mode: Important only\n• Social awareness: Discreet around others\n• Lumi Vault: PIN protected\n• Update system: persistent signed builds\n\nGLASSES\nRay-Ban Meta integration point is prepared, but the actual Meta wearable SDK is not bundled in this prototype.",15,text); cards.setBackgroundColor(panel); cards.setPadding(24,24,24,24); content.addView(cards);
    }

    void showTalk(){ base("Talk to Lumi");
        TextView transcript=tv("Lumi: Talk normally. Try: ‘remember my drill is in the west cabinet’, ‘show yourself’, ‘go home’, or ‘loosen the context filter’.",16,text); transcript.setBackgroundColor(panel); content.addView(transcript);
        EditText input=new EditText(this); input.setHint("Say or type anything..."); input.setHintTextColor(muted); input.setTextColor(text); input.setSingleLine(false); input.setMinLines(2); content.addView(input);
        Button send=btn("Send to Lumi"); content.addView(send); send.setOnClickListener(v->{String q=input.getText().toString().trim(); if(q.isEmpty())return; transcript.append("\n\nYou: "+q+"\nLumi: "+respond(q)); input.setText("");});
    }

    String respond(String q){String l=q.toLowerCase(Locale.US);
        if(l.contains("show yourself")){showOverlay(); return "There I am. Try not to look too pleased with yourself.";}
        if(l.contains("go home")){new Handler().postDelayed(this::showHome,400); return "Taking us home.";}
        if(l.contains("loosen") && l.contains("filter")){prefs.edit().putString("filter","Relaxed").apply(); return "Context Filter is now Relaxed.";}
        if(l.contains("strict") && l.contains("filter")){prefs.edit().putString("filter","Strict").apply(); return "Context Filter is now Strict.";}
        if(l.startsWith("remember") || l.contains("remember my") || l.contains("remember that")){String old=prefs.getString("memories",""); String stamp=new SimpleDateFormat("MMM d, h:mm a",Locale.US).format(new Date()); prefs.edit().putString("memories",old+"\n• "+stamp+" — "+q).apply(); return "Remembered.";}
        return "Prototype response: I heard you naturally. The conversational shell is working; live ChatGPT and Meta model connections come next.";
    }

    void showMemory(){ base("Memory"); String m=prefs.getString("memories","").trim(); content.addView(tv(m.isEmpty()?"No saved memories yet. Tell Lumi ‘remember…’ in Talk.":m,16,text)); Button clear=btn("Clear prototype memories"); clear.setOnClickListener(v->{prefs.edit().remove("memories").apply();showMemory();}); content.addView(clear); }

    void openVault(){ String pin=prefs.getString("pin",""); if(pin.isEmpty()){ setupPin(); return; }
        final EditText e=new EditText(this); e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD); e.setHint("Lumi PIN");
        new AlertDialog.Builder(this).setTitle("Unlock Lumi Vault").setView(e).setNegativeButton("Cancel",null).setPositiveButton("Unlock",(d,w)->{ if(e.getText().toString().equals(pin)) showVault(); else Toast.makeText(this,"Incorrect PIN",Toast.LENGTH_SHORT).show(); }).show();
    }
    void setupPin(){ final EditText e=new EditText(this); e.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD); e.setHint("Choose Lumi PIN"); new AlertDialog.Builder(this).setTitle("Create Lumi Vault PIN").setMessage("Separate from your phone unlock. Prototype storage only; production build will use encrypted storage.").setView(e).setPositiveButton("Save",(d,w)->{if(e.getText().length()>=4){prefs.edit().putString("pin",e.getText().toString()).apply();showVault();}else Toast.makeText(this,"Use at least 4 digits",Toast.LENGTH_SHORT).show();}).setNegativeButton("Cancel",null).show(); }
    void showVault(){ base("Lumi Vault"); content.addView(tv("Private gallery prototype\n\nCaptured media will live here instead of the normal phone Gallery. Production: encrypted files, 5-minute unlock window, automatic organization by people / places / objects / important moments, and emergency captures retained indefinitely.",16,text)); TextView lock=tv("🔒 PIN unlocked for this view",15,accent); content.addView(lock); }

    void showSettings(){ base("Settings");
        content.addView(tv("Context Filter",18,text)); RadioGroup rg=new RadioGroup(this); String cur=prefs.getString("filter","Balanced"); for(String s:new String[]{"Strict","Balanced","Relaxed","Custom"}){RadioButton r=new RadioButton(this);r.setText(s);r.setTextColor(text);r.setChecked(s.equals(cur));r.setOnClickListener(v->prefs.edit().putString("filter",s).apply());rg.addView(r);} content.addView(rg);
        content.addView(tv("Behavior",18,text)); content.addView(tv("✓ Important proactive cues only\n✓ Quiet around other people\n✓ Natural conversation\n✓ Learn from corrections\n✓ Home / Public / Travel profiles\n✓ High-risk actions require confirmation\n✓ Purchases require approval\n✓ Emergency: 30-second cancel → wife + location",15,text));
        Button change=btn("Change Lumi Vault PIN"); change.setOnClickListener(v->{prefs.edit().remove("pin").apply();setupPin();});content.addView(change);
        Button overlay=btn("Grant floating-overlay permission"); overlay.setOnClickListener(v->requestOverlay()); content.addView(overlay);
    }
    void requestOverlay(){ if(Build.VERSION.SDK_INT>=23 && !Settings.canDrawOverlays(this)){ startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName()))); } else Toast.makeText(this,"Overlay permission already available",Toast.LENGTH_SHORT).show(); }
    void showOverlay(){ if(Build.VERSION.SDK_INT>=23 && !Settings.canDrawOverlays(this)){ requestOverlay(); Toast.makeText(this,"Grant overlay permission, then try again",Toast.LENGTH_LONG).show(); return;} startService(new Intent(this,LumiOverlayService.class)); }
}
