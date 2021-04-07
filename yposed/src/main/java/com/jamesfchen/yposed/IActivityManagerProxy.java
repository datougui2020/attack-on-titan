package com.jamesfchen.yposed;

import android.app.IActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 三月/15/2021  星期一
 */
public class IActivityManagerProxy implements InvocationHandler {
    IActivityManager origin;

    public IActivityManagerProxy(IActivityManager origin) {
        this.origin = origin;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("startActivity".equals(method.getName())) {
            //    int startActivity(in IApplicationThread caller, in String callingPackage, in Intent intent,
            //            in String resolvedType, in IBinder resultTo, in String resultWho, int requestCode,
            //            int flags, in ProfilerInfo profilerInfo, in Bundle options);
            String callingPackage = (String) args[1];
            Intent raw = (Intent) args[2];
            Log.e("cjf_attack", "startActivity:" + Arrays.toString(args));
            String stubPackage ="com.jamesfchen.titan";//StubActivity所在的包
            Intent stubIntent = new Intent();
            ComponentName componentName = new ComponentName(stubPackage, StubActivity.class.getName());
            stubIntent.setComponent(componentName);
            stubIntent.putExtra("extra_raw_intent",raw);
            args[2] = stubIntent;
        }else if ("startService".equals(method.getName())) {
        } else if ("stopService".equals(method.getName())) {
        } else if ("stopServiceToken".equals(method.getName())) {
        } else if ("bindService".equals(method.getName())) {
        } else if ("unbindService".equals(method.getName())) {
        } else if ("getIntentSender".equals(method.getName())) {
        } else if ("overridePendingTransition".equals(method.getName())){
        }
        return method.invoke(origin,args);
    }
}
