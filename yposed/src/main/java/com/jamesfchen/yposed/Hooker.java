package com.jamesfchen.yposed;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.Application;
import android.app.IActivityManager;
import android.app.LoadedApk;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.res.CompatibilityInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Singleton;

import com.jamesfchen.common.Utils;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 三月/15/2021  星期一
 */
public class Hooker {

    private static void printAttrib(Class<?> clz) {
        Log.e("cjf_attack", "clz:" + clz.getName());
        Class<?>[] declaredClasses = clz.getDeclaredClasses();
        for (Class<?> c : declaredClasses) {
            Log.e("cjf_attack", "inner clz:" + c.getName());
        }
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field f : declaredFields) {
            f.setAccessible(true);
            Log.e("cjf_attack", "field name：" + f.getName() + " ");

        }
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Method m : declaredMethods) {
            m.setAccessible(true);
            Log.e("cjf_attack", "method name：" + m.getName() + " ");

        }
    }
    public static Application sApplication;
    public static void init(ClassLoader classLoader,Application app) {
        sApplication = app;

        IPackageManager pkgMgrOrigin = ActivityThread.getPackageManager();
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        IPackageManager packageManagerProxy = (IPackageManager) Proxy.newProxyInstance(classLoader, new Class[]{IPackageManager.class}, new PackageManagerProxy(pkgMgrOrigin));
        try {
            Field sPackageManagerField = activityThread.getClass().getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            sPackageManagerField.set(activityThread, packageManagerProxy);

            IBinder iBinderOrigin = ServiceManager.getService("package");

            IBinder iBinderProxy = (IBinder) Proxy.newProxyInstance(classLoader, new Class[]{IBinder.class}, new IBinderProxy(iBinderOrigin, packageManagerProxy, classLoader));
            Field sCacheField = ServiceManager.class.getDeclaredField("sCache");
            sCacheField.setAccessible(true);
            Map<String, IBinder> sCache = (Map<String, IBinder>) sCacheField.get(null);
            sCache.remove("package");
            sCache.put("package", iBinderProxy);

            hook_ams(classLoader);
            hook_instrumentation(classLoader);
        } catch (Exception e) {
            Log.e("cjf_attack", Log.getStackTraceString(e));
            e.printStackTrace();
        }
    }

    static void hook_ams(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException {
        Field gDefaultField;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //android 10 hook不了
            gDefaultField = ActivityManager.class.getDeclaredField("IActivityManagerSingleton");
        } else {
            gDefaultField = ActivityManagerNative.class.getDeclaredField("gDefault");
        }
        gDefaultField.setAccessible(true);
        Singleton<IActivityManager> defaultSingleton = (Singleton<IActivityManager>) gDefaultField.get(null);
        IActivityManager origin = defaultSingleton.get();
        IActivityManager activityManagerProxy = (IActivityManager) Proxy.newProxyInstance(classLoader, new Class[]{IActivityManager.class},
                new IActivityManagerProxy(origin));

        // Hook IActivityManager from ActivityManagerNative
        Field mInstanceField = Singleton.class.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);
        mInstanceField.set(defaultSingleton, activityManagerProxy);
    }

    static void hook_instrumentation(ClassLoader classLoader) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        ActivityThread activityThread = ActivityThread.currentActivityThread();
        Field mInstrumentationField = activityThread.getClass().getDeclaredField("mInstrumentation");
        mInstrumentationField.setAccessible(true);


//        Method getHandlerMethod = activityThread.getClass().getMethod("getHandler");
//        getHandlerMethod.setAccessible(true);
//        Handler mH = (Handler) getHandlerMethod.invoke(activityThread);
        Field handlerField = activityThread.getClass().getDeclaredField("mH");
        handlerField.setAccessible(true);
        Handler mH = (Handler) handlerField.get(activityThread);
//        Field mCallbackField = mH.getClass().getDeclaredField("mCallback");
//        mCallbackField.setAccessible(true);
//        mCallbackField.set(mH,instrumentationProxy);
        Field mCallBackField = Handler.class.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);

        InstrumentationProxy instrumentationProxy = new InstrumentationProxy(activityThread.getInstrumentation(),mH);
        mInstrumentationField.set(activityThread,instrumentationProxy);
        mCallBackField.set(mH, instrumentationProxy);
    }

}
