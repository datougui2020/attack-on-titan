package com.jamesfchen.plugin;

import android.app.ActivityThread;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageParser;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jamesfchen.common.PackageHelper;
import com.jamesfchen.common.Reflector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 四月/16/2021  星期五
 */
public class ServiceManager {
    public static ServiceManager INSTANCE = new ServiceManager();
    private Map<String, Service> mServiceMap = new HashMap<String, Service>();
    private static Map<ComponentName, ServiceInfo> mServiceInfoMap = new HashMap<>();

    public int createService(Intent intent, int flags, int startId) {
        Intent raw = intent.getParcelableExtra("extra_raw_intent");
        ServiceInfo serviceInfo = null;
        for (ComponentName componentName : mServiceInfoMap.keySet()) {
            if (componentName.equals(raw.getComponent())) {
                serviceInfo = mServiceInfoMap.get(componentName);
                break;
            }
        }
        if (serviceInfo == null) {
            Log.e("cjf_attack", "没有找到serviceInfo");
            return -1;
        }
        Service service = mServiceMap.get(serviceInfo.name);
        if (service == null) {
            service = createServiceInner(serviceInfo);
        }
        return service.onStartCommand(raw, flags, startId);
    }

    private Service createServiceInner(ServiceInfo serviceInfo) {
        IBinder token = new Binder();
        try {
            Object serviceData = Reflector.on("android.app.ActivityThread$CreateServiceData").constructor().newInstance();
            Reflector.with(serviceData).field("token").set(token);
            serviceInfo.applicationInfo.packageName = Hooker.SELF_PACKAGE;
            Reflector.with(serviceData).field("info").set(serviceInfo);
            Reflector.with(serviceData).field("compatInfo").set(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO);
//            Reflector.with(serviceData).set("intent", token);
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Reflector.with(activityThread).method("handleCreateService", serviceData.getClass()).call(serviceData);
//            Method handleCreateServiceMethod = activityThread.getClass().getDeclaredMethod("handleCreateService", serviceData.getClass());
//            handleCreateServiceMethod.setAccessible(true);
//            handleCreateServiceMethod.invoke(activityThread, serviceData);
            Map mServices = Reflector.with(activityThread).field("mServices").get();
            Service service = (Service) mServices.get(token);
            mServices.remove(token);
            mServiceMap.put(serviceInfo.name, service);
            return service;
        } catch (Reflector.ReflectedException e) {
            Log.e("cjf_attack", Log.getStackTraceString(e));
            e.printStackTrace();
            return null;
        }
    }

    public static void parseServices(File apkFile) {
        try {
            PackageHelper.parseServices(apkFile, mServiceInfoMap);
        } catch (PackageParser.PackageParserException | Reflector.ReflectedException e) {
            e.printStackTrace();
            Log.e("cjf_attack", Log.getStackTraceString(e));
        }

    }

    public int stopService(Intent raw) {
        ServiceInfo serviceInfo = null;
        for (ComponentName componentName : mServiceInfoMap.keySet()) {
            if (componentName.equals(raw.getComponent())) {
                serviceInfo = mServiceInfoMap.get(componentName);
                break;
            }
        }
        if (serviceInfo == null) {
            Log.e("cjf_attack", "stop失败 没有找到serviceInfo");
            return -1;
        }
        Service service = mServiceMap.get(serviceInfo.name);
        if (service == null) {
            Log.e("cjf_attack", "stop失败 service为null");
            return 0;
        }
        service.onDestroy();
        mServiceMap.remove(serviceInfo.name);
        if (mServiceMap.isEmpty()) {
            Context appContext = Hooker.sApplication.getBaseContext();
            appContext.stopService(new Intent().setComponent(new ComponentName(appContext.getPackageName(), StubService.class.getName())));
        }
        return 1;
    }
}
