# Lumi Android Prototype 0.1

This is a native Android prototype for the Lumi companion concept.

## Implemented in this prototype
- Lumi full-screen home with stylized holographic placeholder avatar
- Natural-language command shell
- "show yourself" floating overlay
- "go home" full-screen navigation
- Context Filter: Strict / Balanced / Relaxed / Custom
- Prototype persistent memory commands
- PIN-protected Lumi Vault shell
- Settings reflecting agreed behavior and safety boundaries
- Permission flow for Android draw-over-other-apps
- Clean integration point for Ray-Ban Meta Wearables Device Access Toolkit

## Not yet live
- Actual ChatGPT API calls
- Meta wearable camera/mic/audio integration
- Custom "Hey Lumi" hardware wake word
- Secure encrypted media vault implementation
- Ambient listening / vision capture
- Contact/calendar/email/home automation connectors
- Emergency sensor logic
- Animated 3D avatar

## Build
Open in Android Studio (Jellyfish or newer recommended), sync Gradle, then Build > Build APK(s).
The project uses Android Gradle Plugin 8.7.2, compileSdk 35, minSdk 28.

## Important
The PIN is stored in SharedPreferences only in this prototype. Do not use this build for sensitive data. Production should use Android Keystore + encrypted file/database storage.
