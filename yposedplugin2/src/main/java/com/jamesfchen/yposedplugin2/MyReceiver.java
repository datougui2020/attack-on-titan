package com.jamesfchen.yposedplugin2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("cjf_attack", "yposedplugin2 接收到广播");
        context.sendBroadcast(new Intent("com.jamesfchen.titan.br_test"));
    }
}
