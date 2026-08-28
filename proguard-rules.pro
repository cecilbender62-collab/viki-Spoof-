# ProGuard rules for Viki Spoof

# Keep Xposed API
-keep class de.robv.android.xposed.** { *; }
-keep interface de.robv.android.xposed.** { *; }

# Keep our module class
-keep class com.viki.spoof.** { *; }
-keepclassmembers class com.viki.spoof.** { *; }

# Keep Android framework classes
-keep class android.os.Build { *; }
-keep class android.provider.Settings { *; }
-keep class android.telephony.TelephonyManager { *; }
-keep class android.net.wifi.WifiManager { *; }
-keep class android.os.SystemProperties { *; }

# Keep companion objects
-keepclassmembers class com.viki.spoof.VikiSpoof {
    public static *** *;
}

# Keep method names
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Kotlin metadata
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
