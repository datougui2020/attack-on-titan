package com.jamesfchen.yposed

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.jamesfchen.common.Loader
import com.jamesfchen.common.Utils

class YPosedActivity : Activity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Utils.extractAssets(newBase, "yposedplugin2-debug.apk")
        val dexFile = getFileStreamPath("yposedplugin2-debug.apk")
        val optDexFile = getFileStreamPath("yposedplugin2-debug.dex")
        Loader.loadDex(classLoader, dexFile, optDexFile)
        Loader.loadApk(classLoader, getFileStreamPath("yposedplugin3-debug.apk"))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yposed)
        findViewById<Button>(R.id.bt_load_plugin).setOnClickListener {
            val int = Intent()
            int.component = ComponentName(
                "com.jamesfchen.yposedplugin3",
                "com.jamesfchen.yposedplugin3.MyActivity"
            )
            startActivity(int)
        }

        findViewById<Button>(R.id.bt_load_dex).setOnClickListener {
            val int = Intent()
            int.component = ComponentName(
                "com.jamesfchen.yposedplugin2",
                "com.jamesfchen.yposedplugin2.MyActivity"
            )
            startActivity(int)
        }
        findViewById<Button>(R.id.bt_send).setOnClickListener {
            NetClient.getInstance().sendRequest()
        }
    }

    fun stringFromJava() = "string  from java"
}
