plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = 34
    defaultConfig {
        applicationId = "com.viki.spoof"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    // LSPosed API
    compileOnly("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    compileOnly("de.robv.android.xposed:api:82")
    
    // Android Framework
    compileOnly("android.framework:android-framework:34")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    
    // Shared Preferences for configuration
    implementation("androidx.preference:preference-ktx:1.2.0")
}
