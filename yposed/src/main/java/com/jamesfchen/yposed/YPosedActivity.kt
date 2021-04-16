package com.jamesfchen.yposed

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.jamesfchen.common.Loader
import com.jamesfchen.common.Utils

class YPosedActivity : Activity() {
    companion object {

        init {
            System.loadLibrary("gotplthook")
        }

        const val TAG = "cjf_attack"
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        AsyncTask.execute {
            Utils.extractAssets(newBase, "yposedplugin2-debug.apk")
            val dexFile = getFileStreamPath("yposedplugin2-debug.apk")
            val optDexFile = getFileStreamPath("yposedplugin2-debug.dex")
            ServiceManager.parseServices(getFileStreamPath("yposedplugin2-debug.apk"))
            Loader.loadDex(classLoader, dexFile, optDexFile)
            Utils.extractAssets(newBase, "yposedplugin3-debug.apk")
            ServiceManager.parseServices(getFileStreamPath("yposedplugin3-debug.apk"))
            Loader.loadApk(classLoader, getFileStreamPath("yposedplugin3-debug.apk"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yposed)

        findViewById<Button>(R.id.bt_load_plugin).setOnClickListener {
            startService(
                Intent().setComponent(
                    ComponentName(
                        "com.jamesfchen.yposedplugin3",
                        "com.jamesfchen.yposedplugin3.MyService"
                    )
                )
            )
            startActivity(
                Intent().setComponent(
                    ComponentName(
                        "com.jamesfchen.yposedplugin3",
                        "com.jamesfchen.yposedplugin3.MyActivity"
                    )
                )
            )
        }

        findViewById<Button>(R.id.bt_load_dex).setOnClickListener {
            startService(
                Intent().setComponent(
                    ComponentName(
                        "com.jamesfchen.yposedplugin2",
                        "com.jamesfchen.yposedplugin2.MyService"
                    )
                )
            )
            val int = Intent()
            int.component = ComponentName(
                "com.jamesfchen.yposedplugin2",
                "com.jamesfchen.yposedplugin2.MyActivity"
            )
            startActivity(int)
        }
        startService(Intent(this, TestService::class.java))
        findViewById<Button>(R.id.bt_send).setOnClickListener {
            NetClient.getInstance().sendRequest()
            stopService(Intent(this, TestService::class.java))
            stopService(
                Intent().setComponent(
                    ComponentName(
                        "com.jamesfchen.yposedplugin2",
                        "com.jamesfchen.yposedplugin2.MyService"
                    )
                )
            )
            stopService(
                Intent().setComponent(
                    ComponentName(
                        "com.jamesfchen.yposedplugin2",
                        "com.jamesfchen.yposedplugin2.MyService"
                    )
                )
            )
        }
        plthook_init()
    }

    fun stringFromJava() = "string  from java"
    external fun plthook_init()
    external fun gothook_init()
}
