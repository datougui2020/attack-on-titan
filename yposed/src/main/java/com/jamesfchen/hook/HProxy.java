package com.jamesfchen.hook;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;

public class HProxy implements Handler.Callback {
    public static final int LAUNCH_ACTIVITY = 100;
    public static final int RESUME_ACTIVITY = 107;
    public static final int CREATE_SERVICE = 114;
    public static final int SERVICE_ARGS = 115;
    public static final int STOP_SERVICE = 116;
    public static final int BIND_SERVICE = 121;
    public static final int UNBIND_SERVICE = 122;
    public static final int INSTALL_PROVIDER = 145;
    Handler mH;
    public HProxy(Handler mH) {
        this.mH = mH;
    }
    @Override
    public boolean handleMessage(@NonNull Message msg) {
        try {
            Object obj = msg.obj;
            if (msg.what == LAUNCH_ACTIVITY) {
                Log.e("cjf_attack", "LAUNCH_ACTIVITY");
                Field intent = obj.getClass().getDeclaredField("intent");
                intent.setAccessible(true);
                Intent stubIntent = (Intent) intent.get(obj);
                Intent raw = stubIntent.getParcelableExtra("extra_raw_intent");
                if (raw != null && raw.getComponent() !=null) {//来自于StubActivity
                    stubIntent.setComponent(raw.getComponent());

                    Field activityInfoField = obj.getClass().getDeclaredField("activityInfo");
                    activityInfoField.setAccessible(true);

                    ActivityInfo activityInfo = (ActivityInfo) activityInfoField.get(obj);

                    activityInfo.applicationInfo.packageName = raw.getPackage() == null ?
                            raw.getComponent().getPackageName() : raw.getPackage();
                }
                // Caused by: java.lang.RuntimeException: Unable to instantiate application android.app.Application: java.lang.IllegalStateException: Unable to get package info for com.hawksjamesf.yposedplugin; is package not installed?
            }else if (msg.what == RESUME_ACTIVITY){
            }else if (msg.what == CREATE_SERVICE){
                Log.e("cjf_attack", "CREATE_SERVICE");
            }else if (msg.what == STOP_SERVICE){
                Log.e("cjf_attack", "STOP_SERVICE");
            }else if (msg.what == BIND_SERVICE){
                Log.e("cjf_attack", "BIND_SERVICE");
            }else if (msg.what == UNBIND_SERVICE){
                Log.e("cjf_attack", "UNBIND_SERVICE");
            }else if (msg.what == INSTALL_PROVIDER){
                Log.e("cjf_attack", "INSTALL_PROVIDER");
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        mH.handleMessage(msg);
        return true;
    }
}
