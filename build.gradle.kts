plugins {
    id("com.android.application") version "8.1.0"
    kotlin("android") version "1.9.0"
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
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // LSPosed API
    compileOnly("org.lsposed.hiddenapibypass:hiddenapibypass:4.3")
    compileOnly("de.robv.android.xposed:api:82")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
}
