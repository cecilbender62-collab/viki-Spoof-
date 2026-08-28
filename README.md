# Viki Spoof - Android Identity Changer

An LSPosed module that spoofs Android device identifiers to enhance privacy and prevent device fingerprinting.

## Features

### Spoofed Identifiers

- ✅ **Android ID** - Randomized per package
- ✅ **IMEI** - Randomly generated (looks realistic)
- ✅ **Device Serial Number** - Randomized
- ✅ **MAC Address** - Spoofed per WiFi request
- ✅ **Build Properties:**
  - `MODEL` (e.g., SM-G991B)
  - `MANUFACTURER` (e.g., samsung)
  - `DEVICE`
  - `FINGERPRINT`
- ✅ **System Properties (ro.*):**
  - `ro.serialno`
  - `ro.build.fingerprint`
  - `ro.build.version.release`
  - `ro.product.model`
  - `ro.product.manufacturer`

## Installation

### Requirements
- Android device with LSPosed installed
- Android 7.0+ (API 24+)
- Root access

### Steps
1. Download the latest release APK
2. Install via Xposed Installer / LSPosed Manager
3. Enable the module
4. Reboot device
5. Verify activation in LSPosed Manager

## How It Works

The module uses the Xposed Framework to hook into Android system calls at runtime:

1. **Intercepts** method calls from apps requesting device identifiers
2. **Generates** realistic-looking fake values
3. **Returns** spoofed data instead of real device information

### Hooked Methods

#### Build Class
- `Build.MODEL`
- `Build.MANUFACTURER`
- `Build.DEVICE`
- `Build.FINGERPRINT`
- `Build.getSerial()`

#### Settings.Secure
- `getString(resolver, "android_id")`

#### TelephonyManager
- `getDeviceId()` - Returns fake IMEI
- `getImei(slotId)` - Returns fake IMEI
- `getImei()` - Returns fake IMEI

#### WifiManager
- `getConnectionInfo()` - MAC address spoofed
- `WifiInfo.getMacAddress()` - Returns fake MAC

#### SystemProperties
- `get(String key)` - Intercepts all property reads

## Configuration

Currently, the module spoofs values to simulate a **Samsung Galaxy S21 (SM-G991B)** running **Android 13**.

To customize spoofed values, edit `src/main/java/com/viki/spoof/VikiSpoof.kt`:

```kotlin
// Change MODEL
XposedHelpers.setStaticObjectField(buildClass, "MODEL", "YOUR_MODEL")

// Change MANUFACTURER
XposedHelpers.setStaticObjectField(buildClass, "MANUFACTURER", "YOUR_MANUFACTURER")

// Change FINGERPRINT
XposedHelpers.setStaticObjectField(buildClass, "FINGERPRINT", "YOUR_FINGERPRINT")
```

## Build from Source

### Prerequisites
- Android Studio or JDK 11+
- Gradle 7.5+

### Build Steps
```bash
git clone https://github.com/cecilbender62-collab/viki-Spoof-
cd viki-Spoof-
./gradlew build
```

The APK will be generated at: `app/build/outputs/apk/release/app-release.apk`

## Testing

### Verify Android ID Spoofing
```bash
adb shell
settings get secure android_id
```

### Verify Build Properties
```bash
adb shell getprop ro.build.fingerprint
adb shell getprop ro.product.model
```

### Verify IMEI Spoofing
Use an app that displays device information and restart it with the module enabled.

## Privacy Benefits

- **Prevents device fingerprinting** across apps
- **Blocks persistent tracking** via ANDROID_ID
- **Hides real device model** and manufacturer
- **Spoofs hardware identifiers** (IMEI, Serial)
- **Randomizes MAC addresses** per connection

## Limitations

- **Google Advertising ID (GAID)** - Use AdAway or similar to block ads and GAID tracking separately
- **Firebase Instance ID** - Requires additional configuration
- **Hardware-level tracking** - Some enterprise MDM solutions may still detect spoofing
- **Cross-device fingerprinting** - Behavioral patterns may still identify you

## Troubleshooting

### Module not working?
1. Verify LSPosed is installed and activated
2. Ensure module is enabled in LSPosed Manager
3. Check if the target app is in the scope
4. Reboot device

### Specific app not spoofed?
1. Check app package name in module settings
2. Verify app isn't bypassing Xposed hooks
3. Some apps may use alternative methods to read device info

### Performance issues?
- The module has minimal overhead
- If you experience lag, disable the module and reboot

## Ethical Considerations

This module is designed for **privacy enhancement and research**. Ensure you comply with:
- Your device manufacturer's policies
- App store terms of service
- Local laws regarding device modification
- App licensing agreements

Some apps may detect spoofing and restrict functionality. Use responsibly.

## Legal Disclaimer

This module is provided as-is for educational and privacy purposes. The authors are not responsible for:
- Violation of app ToS
- Loss of functionality in apps
- Any damage to your device
- Legal consequences from misuse

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Submit a pull request with clear descriptions

## License

[Add your chosen license here]

## Support

- 📧 Email: [contact info]
- 🐛 Issues: GitHub Issues
- 💬 Discussions: GitHub Discussions

## Changelog

### v1.0 (Initial Release)
- Android ID spoofing
- IMEI/Serial spoofing
- Build properties spoofing
- MAC address spoofing
- System properties hooking

---

**Disclaimer:** This is a security and privacy research project. Use at your own risk.
