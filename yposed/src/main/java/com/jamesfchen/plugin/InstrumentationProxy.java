package com.jamesfchen.plugin;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;

import java.lang.reflect.Method;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 三月/15/2021  星期一
 */
public class InstrumentationProxy extends Instrumentation {
    Instrumentation instrumentation;
    public InstrumentationProxy(Instrumentation instrumentation) {
        this.instrumentation = instrumentation;
    }

//    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode) {
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class);

            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who,
                    contextThread, token, target, intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("do not support!!! pls adapt it");
        }
//        return instrumentation.execStartActivity(who, contextThread, token, target, intent, requestCode);
    }

    //    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target, Intent intent, int requestCode, Bundle options) {
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);

            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("do not support!!! pls adapt it");
        }
//        return instrumentation.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
    }

    //    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Fragment target, Intent intent, int requestCode, Bundle options) {
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, Fragment.class,
                    Intent.class, int.class, Bundle.class);

            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("do not support!!! pls adapt it");
        }
//        return instrumentation.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
    }

    //    @Override
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, String target, Intent intent, int requestCode, Bundle options) {
        try {
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity",
                    Context.class, IBinder.class, IBinder.class, String.class,
                    Intent.class, int.class, Bundle.class);

            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(instrumentation, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("do not support!!! pls adapt it");
        }
//        return instrumentation.execStartActivity(who, contextThread, token, target, intent, requestCode, options);
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return instrumentation.newActivity(cl, className, intent);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return instrumentation.newApplication(cl, className, context);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        instrumentation.callActivityOnCreate(activity, icicle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        instrumentation.callActivityOnCreate(activity, icicle, persistentState);
    }

    @Override
    public Context getContext() {
        return instrumentation.getContext();
    }

    @Override
    public Context getTargetContext() {
        return instrumentation.getTargetContext();
    }

    @Override
    public ComponentName getComponentName() {
        return instrumentation.getComponentName();
    }
}

