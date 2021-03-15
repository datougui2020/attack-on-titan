package com.hawksjamesf.yposed

import android.app.Activity
import android.app.ActivityThread
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageParser
import android.os.Bundle
import android.widget.Button
import com.jamesfchen.common.Utils

class YPosedActivity : Activity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        Utils.extractAssets(newBase, "yposedplugin-debug.apk")

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_yposed)
        findViewById<Button>(R.id.bt_hook_frida).setOnClickListener {
            val int = Intent()
            int.component = ComponentName(
                "com.hawksjamesf.yposedplugin",
                "com.hawksjamesf.yposedplugin.MyActivity"
            )
            startActivity(int)
        }
    }
    fun stringFromJava() = "string  from java"
}
