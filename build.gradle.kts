plugins {
    id("com.android.application") version "7.4.2"
    kotlin("android") version "1.8.10"
}

android {
    compileSdk = 33
    
    defaultConfig {
        applicationId = "com.viki.spoof"
        minSdk = 24
        targetSdk = 33
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

repositories {
    google()
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Xposed API
    compileOnly("com.github.rovo89:XposedBridgeApi:82")
    
    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.10")
}
