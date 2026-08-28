package com.viki.spoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.UUID

class VikiSpoof : IXposedHookLoadPackage {
    
    companion object {
        private const val TAG = "VikiSpoof"
        private val SPOOFED_IDS = mutableMapOf<String, String>()
        
        fun generateFakeAndroidId(): String {
            return UUID.randomUUID().toString().replace("-", "").take(16).uppercase()
        }
        
        fun generateFakeIMEI(): String {
            return "35" + (0..12).joinToString("") { (0..9).random().toString() }
        }
        
        fun generateFakeSerialNumber(): String {
            return UUID.randomUUID().toString().replace("-", "").take(20).uppercase()
        }
        
        fun generateFakeMACAddress(): String {
            return (0..5).joinToString(":") { "%02x".format((0..255).random()) }
        }
    }
    
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName == "com.viki.spoof") return
        
        try {
            // Hook Build properties
            hookBuildProperties(lpparam)
            
            // Hook Settings.Secure.ANDROID_ID
            hookAndroidId(lpparam)
            
            // Hook TelephonyManager (IMEI, Serial)
            hookTelephonyManager(lpparam)
            
            // Hook WifiManager (MAC Address)
            hookWifiManager(lpparam)
            
            // Hook System properties (ro.*)
            hookSystemProperties(lpparam)
            
            XposedBridge.log("$TAG: Hooks applied to ${lpparam.packageName}")
        } catch (e: Exception) {
            XposedBridge.log("$TAG: Error hooking ${lpparam.packageName}: ${e.message}")
            e.printStackTrace()
        }
    }
    
    private fun hookBuildProperties(lpparam: XC_LoadPackage.LoadPackageParam) {
        val buildClass = lpparam.classLoader.loadClass("android.os.Build")
        
        // Spoof MODEL
        XposedHelpers.setStaticObjectField(buildClass, "MODEL", "SM-G991B")
        
        // Spoof MANUFACTURER
        XposedHelpers.setStaticObjectField(buildClass, "MANUFACTURER", "samsung")
        
        // Spoof DEVICE
        XposedHelpers.setStaticObjectField(buildClass, "DEVICE", "o1s")
        
        // Spoof FINGERPRINT
        XposedHelpers.setStaticObjectField(
            buildClass, 
            "FINGERPRINT", 
            "samsung/o1s/o1s:13/TP1A.220624.014/R16NW.G991BXXU2AUJA:user/release-keys"
        )
        
        // Spoof SERIAL
        try {
            XposedHelpers.findAndHookMethod(
                buildClass,
                "getSerial",
                object : XposedHelpers.MethodHook() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any {
                        return generateFakeSerialNumber()
                    }
                }
            )
        } catch (e: Exception) {
            XposedHelpers.setStaticObjectField(buildClass, "SERIAL", generateFakeSerialNumber())
        }
    }
    
    private fun hookAndroidId(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val settingsSecureClass = lpparam.classLoader.loadClass("android.provider.Settings\$Secure")
            val contentResolverClass = lpparam.classLoader.loadClass("android.content.ContentResolver")
            
            XposedHelpers.findAndHookMethod(
                settingsSecureClass,
                "getString",
                contentResolverClass,
                String::class.java,
                object : XposedHelpers.MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val key = param.args[1] as? String
                        if (key == "android_id") {
                            param.result = SPOOFED_IDS.getOrPut(lpparam.packageName) {
                                generateFakeAndroidId()
                            }
                        }
                    }
                }
            )
        } catch (e: Exception) {
            XposedBridge.log("$TAG: Error hooking Android ID: ${e.message}")
        }
    }
    
    private fun hookTelephonyManager(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val telephonyClass = lpparam.classLoader.loadClass("android.telephony.TelephonyManager")
            
            // Hook getDeviceId (IMEI)
            try {
                XposedHelpers.findAndHookMethod(
                    telephonyClass,
                    "getDeviceId",
                    object : XposedHelpers.MethodHook() {
                        override fun replaceHookedMethod(param: MethodHookParam?): Any {
                            return generateFakeIMEI()
                        }
                    }
                )
            } catch (e: Exception) {
                // Try with slot parameter
                try {
                    XposedHelpers.findAndHookMethod(
                        telephonyClass,
                        "getDeviceId",
                        Int::class.java,
                        object : XposedHelpers.MethodHook() {
                            override fun replaceHookedMethod(param: MethodHookParam?): Any {
                                return generateFakeIMEI()
                            }
                        }
                    )
                } catch (e2: Exception) {
                    // Continue if method not found
                }
            }
            
            // Hook getImei
            try {
                XposedHelpers.findAndHookMethod(
                    telephonyClass,
                    "getImei",
                    object : XposedHelpers.MethodHook() {
                        override fun replaceHookedMethod(param: MethodHookParam?): Any {
                            return generateFakeIMEI()
                        }
                    }
                )
            } catch (e: Exception) {
                // Continue if method not found
            }
        } catch (e: Exception) {
            XposedBridge.log("$TAG: Error hooking TelephonyManager: ${e.message}")
        }
    }
    
    private fun hookWifiManager(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val wifiInfoClass = lpparam.classLoader.loadClass("android.net.wifi.WifiInfo")
            
            // Hook getMacAddress method
            XposedHelpers.findAndHookMethod(
                wifiInfoClass,
                "getMacAddress",
                object : XposedHelpers.MethodHook() {
                    override fun replaceHookedMethod(param: MethodHookParam?): Any {
                        return generateFakeMACAddress()
                    }
                }
            )
        } catch (e: Exception) {
            XposedBridge.log("$TAG: Could not hook WifiInfo: ${e.message}")
        }
    }
    
    private fun hookSystemProperties(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            val systemPropertiesClass = lpparam.classLoader.loadClass("android.os.SystemProperties")
            
            XposedHelpers.findAndHookMethod(
                systemPropertiesClass,
                "get",
                String::class.java,
                object : XposedHelpers.MethodHook() {
                    override fun afterHookedMethod(param: MethodHookParam) {
                        val key = param.args[0] as? String
                        when (key) {
                            "ro.serialno" -> param.result = generateFakeSerialNumber()
                            "ro.build.fingerprint" -> param.result = 
                                "samsung/o1s/o1s:13/TP1A.220624.014/R16NW.G991BXXU2AUJA:user/release-keys"
                            "ro.build.version.release" -> param.result = "13"
                            "ro.product.model" -> param.result = "SM-G991B"
                            "ro.product.manufacturer" -> param.result = "samsung"
                        }
                    }
                }
            )
        } catch (e: Exception) {
            XposedBridge.log("$TAG: Could not hook SystemProperties: ${e.message}")
        }
    }
}
