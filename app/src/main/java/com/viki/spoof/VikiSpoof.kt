package com.viki.spoof

import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.util.UUID

class VikiSpoof : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null || lpparam.packageName == "com.viki.spoof") return

        try {
            hookBuildProperties(lpparam)
            XposedBridge.log("VikiSpoof: Hooked ${lpparam.packageName}")
        } catch (e: Exception) {
            XposedBridge.log("VikiSpoof Error: ${e.message}")
        }
    }

    private fun hookBuildProperties(lpparam: XC_LoadPackage.LoadPackageParam) {
        val buildClass = lpparam.classLoader.loadClass("android.os.Build")
        val fakeSerial = UUID.randomUUID().toString().take(16)
        
        XposedHelpers.setStaticObjectField(buildClass, "MODEL", "SM-G991B")
        XposedHelpers.setStaticObjectField(buildClass, "MANUFACTURER", "samsung")
        XposedHelpers.setStaticObjectField(buildClass, "DEVICE", "o1s")
        XposedHelpers.setStaticObjectField(buildClass, "SERIAL", fakeSerial)
    }
}
