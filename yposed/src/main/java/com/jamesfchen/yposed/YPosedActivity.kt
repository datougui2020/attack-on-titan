package com.jamesfchen.yposed

import android.app.Activity
import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Button
import com.jamesfchen.plugin.ReceiverHelper


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
            startActivity(
                Intent().setComponent(
                    ComponentName(
                        "com.jamesfchen.titan",
                        "com.jamesfchen.yposed.YPosedActivity"
                    )
                )
            )
//            startActivity(Intent().setComponent(
//                ComponentName(
//                    "com.jamesfchen.yposedplugin2",
//                    "com.jamesfchen.yposedplugin2.MyActivity"
//                )
//            ))

        }
        startService(Intent(this, TestService::class.java))
        findViewById<Button>(R.id.bt_send).setOnClickListener {
            var uri = Uri.parse("content://com.jamesfchen.yposedplugin2.my_provider")
//            H.a(uri, this@YPosedActivity, "yposedplugin2")
            uri = Uri.parse("content://com.jamesfchen.yposedplugin3.my_provider")
            H.a(uri, this@YPosedActivity, "yposedplugin3")
            sendBroadcast(Intent("com.jamesfchen.yposedplugin2.MyReceiver"))

//            NetClient.getInstance().sendRequest()
//            stopService(Intent(this, TestService::class.java))
//            stopService(
//                Intent().setComponent(
//                    ComponentName(
//                        "com.jamesfchen.yposedplugin2",
//                        "com.jamesfchen.yposedplugin2.MyService"
//                    )
//                )
//            )
//            stopService(
//                Intent().setComponent(
//                    ComponentName(
//                        "com.jamesfchen.yposedplugin2",
//                        "com.jamesfchen.yposedplugin2.MyService"
//                    )
//                )
//            )
        }
        plthook_init()
        ReceiverHelper.parseReceivers(this, getFileStreamPath("yposedplugin2-debug.apk"))
        registerReceiver(mReceiver, IntentFilter("com.jamesfchen.titan.br_test"))
//        printAllCalsses(getFileStreamPath("yposedplugin2-debug.apk"))
        Looper.myQueue().addIdleHandler(mGcIdler)
    }
    val mGcIdler = object : MessageQueue.IdleHandler {
        override fun queueIdle(): Boolean {
            Log.i("cjf_attack", "idle")
            return false
        }

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
