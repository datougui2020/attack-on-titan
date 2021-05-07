@file:JvmName("Util")

package com.jamesfchen.common

import android.app.ActivityManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.os.Process
import android.text.TextUtils
import android.util.Log
import androidx.annotation.Keep
import dalvik.system.DexFile
import java.io.File
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

/**
 * Copyright ® $ 2020
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: Nov/27/2020  Fri
 */
@Keep
fun sha1ToHexString(cert: ByteArray): String? {
    try {
        val md = MessageDigest.getInstance("SHA1")
        val publicKey = md.digest(cert)
        val hexString = StringBuffer()
        for (i in publicKey.indices) {
            val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                .toUpperCase(Locale.US)
            if (appendString.length == 1) hexString.append("0")
            hexString.append(appendString)
            hexString.append(":")
        }
        val result = hexString.toString()
        return result.substring(0, result.length - 1)
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return null
}

fun isMainProcess(context: Context): Boolean {
    val processName = getProcessName(context, Process.myPid())
    val packageName = context.applicationContext.packageName
    if (TextUtils.isEmpty(processName) || TextUtils.isEmpty(packageName)) {
        return false
    }
    return packageName == processName
}

public fun getProcessName(context: Context, i: Int): String? {
    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
    if (activityManager == null || activityManager.runningAppProcesses == null) {
        return null
    }
    val runningAppProcesses: List<ActivityManager.RunningAppProcessInfo> =
        activityManager.runningAppProcesses
    for (next in runningAppProcesses) {
//        Log.d("cjf","process name:${next.processName}")
        if (next.pid == i) {
            return next.processName
        }
    }
    return null
}

fun getSysProp(str: String): String {
    return if (TextUtils.isEmpty(str)) {
        ""
    } else try {
        val cls = Class.forName("android.os.SystemProperties")
        cls.getDeclaredMethod("get", String::class.java).invoke(cls, str) as String
    } catch (th: Throwable) {
        ""
    }
}

fun printAllCalsses(packageCodePath: String) {
    printAllCalsses(File(packageCodePath))
}

fun printAllCalsses(pathFile: File) {
    val dexFile = DexFile(pathFile)

    val entries = dexFile.entries()
    Log.i("cjf_attack", "dexFile entries:${entries.hasMoreElements()}")
    while (entries.hasMoreElements()) {
        val clzName = entries.nextElement()
        if (clzName.contains("jamesfchen")) {
            Log.i("cjf_attack", "class name:$clzName")
        }
    }
}

private fun printAttrib(clz: Class<*>) {
    Log.e("cjf_attack", "clz:" + clz.name)
    val declaredClasses = clz.declaredClasses
    for (c in declaredClasses) {
        Log.e("cjf_attack", "inner clz:" + c.name)
    }
    val declaredFields = clz.declaredFields
    for (f in declaredFields) {
        f.isAccessible = true
        Log.e("cjf_attack", "field name：" + f.name + " ")
    }
    val declaredMethods = clz.declaredMethods
    for (m in declaredMethods) {
        m.isAccessible = true
        Log.e("cjf_attack", "method name：" + m.name + " ")
    }
}