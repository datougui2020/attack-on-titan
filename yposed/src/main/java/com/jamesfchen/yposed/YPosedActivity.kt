package com.jamesfchen.yposed

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import com.jamesfchen.common.Loader
import com.jamesfchen.common.Utils
import java.lang.StringBuilder
import com.jamesfchen.common.printAllCalsses
import com.jamesfchen.hook.ReceiverHelper
import android.widget.Toast


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
//            val uri = Uri.parse("content://com.jamesfchen.yposedplugin3.my_provider")
//            H.a(uri,this@YPosedActivity,"yposedplugin3")
        }

        findViewById<Button>(R.id.bt_load_dex).setOnClickListener {
//            startService(
//                Intent().setComponent(
//                    ComponentName(
//                        "com.jamesfchen.yposedplugin2",
//                        "com.jamesfchen.yposedplugin2.MyService"
//                    )
//                )
//            )
//            val int = Intent()
//            int.component = ComponentName(
//                "com.jamesfchen.yposedplugin2",
//                "com.jamesfchen.yposedplugin2.MyActivity"
//            )
//            startActivity(int)
            sendBroadcast(Intent("com.jamesfchen.yposedplugin2.MyReceiver"))
        }
        startService(Intent(this, TestService::class.java))
        findViewById<Button>(R.id.bt_send).setOnClickListener {
            val uri = Uri.parse("content://com.jamesfchen.yposedplugin2.my_provider")
            H.a(uri, this@YPosedActivity, "yposedplugin2")
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
        ReceiverHelper.parseReceivers(this,getFileStreamPath("yposedplugin2-debug.apk"))
        registerReceiver(mReceiver, IntentFilter("com.jamesfchen.titan.br_test"))
//        printAllCalsses(getFileStreamPath("yposedplugin2-debug.apk"))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mReceiver);
    }

    var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("cjf_attack", "接收到插件广播")
        }
    }

    fun stringFromJava() = "string  from java"
    external fun plthook_init()
    external fun gothook_init()
}
