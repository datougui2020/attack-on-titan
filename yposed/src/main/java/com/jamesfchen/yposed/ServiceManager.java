package com.jamesfchen.yposed;

import android.app.ActivityThread;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.jamesfchen.common.Reflector;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
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
    private static Map<ComponentName, ServiceInfo> mServiceInfoMap = new HashMap<ComponentName, ServiceInfo>();

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

            Reflector.with(serviceData).field("compatInfo").set(Reflector.on("android.content.res.CompatibilityInfo").field("DEFAULT_COMPATIBILITY_INFO").get());
//            Reflector.with(serviceData).set("intent", token);
            ActivityThread activityThread = ActivityThread.currentActivityThread();
//            Reflector.with(activityThread).method("handleCreateService",serviceData.getClass()).call(activityThread,serviceData);
            Method handleCreateServiceMethod = activityThread.getClass().getDeclaredMethod("handleCreateService", serviceData.getClass());
            handleCreateServiceMethod.setAccessible(true);
            handleCreateServiceMethod.invoke(activityThread, serviceData);
            Map mServices = Reflector.with(activityThread).field("mServices").get();
            Service service = (Service) mServices.get(token);
            mServices.remove(token);
            mServiceMap.put(serviceInfo.name, service);
            return service;
        } catch (Reflector.ReflectedException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            Log.e("cjf_attack", Log.getStackTraceString(e));
            e.printStackTrace();
            return null;
        }
    }

    public static void parseServices(File apkFile) {
        PackageParser pkgParser = new PackageParser();
        PackageUserState packageUserState = new PackageUserState();
        try {
//        int userId = UserHandle.getCallingUserId();
            int userId = Reflector.on("android.os.UserHandle").method("getCallingUserId").call();
            PackageParser.Package pkg = pkgParser.parsePackage(apkFile, PackageManager.GET_SERVICES);
            ArrayList<PackageParser.Service> services = pkg.services;

            for (PackageParser.Service service : services) {
                ServiceInfo info = pkgParser.generateServiceInfo(service, 0, packageUserState, userId);
                Log.e("cjf_attack",info.packageName+" "+service.className);
                mServiceInfoMap.put(new ComponentName(info.packageName, info.name), info);
            }
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
