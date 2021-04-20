package com.jamesfchen.common;

import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;

import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PackageHelper {

    public static void parseProviders(File apkFile, Callback cb) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parseProviders(apkFile, null, cb);
    }

    public static void parseProviders(File apkFile, List<ProviderInfo> outputs) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parseProviders(apkFile, outputs, null);
    }

    public static void parseProviders(File apkFile, List<ProviderInfo> outputs, Callback cb) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_PROVIDERS, ProviderInfo.class, (cmp, info) -> {
            if (cb != null) cb.onDeal(cmp, info);
            if (outputs != null) outputs.add((ProviderInfo) info);
        });
    }

    public static void parseServices(File apkFile, Callback cb) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_SERVICES, ServiceInfo.class, cb);
    }

    public static void parseServices(File apkFile, Map<ComponentName, ServiceInfo> serviceInfoMap) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_SERVICES, ServiceInfo.class, (cmp, info) -> serviceInfoMap.put(new ComponentName(info.packageName, info.name), (ServiceInfo) info));
    }

    public static void parseActivity(File apkFile, Callback cb) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_ACTIVITIES, ActivityInfo.class, cb);
    }

    public static void parseActivity(File apkFile, List<ActivityInfo> outputs) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_ACTIVITIES, ActivityInfo.class, (cmp, info) -> outputs.add((ActivityInfo) info));
    }

    public static void parseReceivers(File apkFile, Callback cb) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_RECEIVERS, ActivityInfo.class, cb);
    }

    public static void parseReceivers(File apkFile, Map<ActivityInfo, List<? extends IntentFilter>> receiverMap) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        parsePlugin(apkFile, PackageManager.GET_RECEIVERS, ActivityInfo.class, (cmp, info) -> receiverMap.put((ActivityInfo) info, cmp.intents));
    }

    public interface Callback {
        void onDeal(PackageParser.Component<? extends PackageParser.IntentInfo> cmp, ComponentInfo info);
    }

    /**
     * 解析插件包
     */
    @Nullable
    private static <T> void parsePlugin(File apkFile, int flag, Class<T> clz, Callback cb) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        int userId = Reflector.on("android.os.UserHandle").method("getCallingUserId").call();
        PackageUserState packageUserState = new PackageUserState();
        PackageParser pkgParser = new PackageParser();
        PackageParser.Package pkg = pkgParser.parsePackage(apkFile, flag);
        if (clz == ServiceInfo.class) {
            ArrayList<PackageParser.Service> services = pkg.services;
            for (PackageParser.Service service : services) {
                ServiceInfo info = PackageParser.generateServiceInfo(service, 0, packageUserState, userId);
                cb.onDeal(service, info);
            }
        } else if (clz == ActivityInfo.class) {
            if (flag == PackageManager.GET_RECEIVERS) {
                for (PackageParser.Activity receiver : pkg.receivers) {
                    ActivityInfo info = PackageParser.generateActivityInfo(receiver, 0, packageUserState, userId);
                    cb.onDeal(receiver, info);
                }
            } else {
                for (PackageParser.Activity ac : pkg.activities) {
                    ActivityInfo info = PackageParser.generateActivityInfo(ac, 0, packageUserState, userId);
                    cb.onDeal(ac, info);
                }
            }
        } else if (clz == ProviderInfo.class) {
            for (PackageParser.Provider provider : pkg.providers) {
                ProviderInfo info = PackageParser.generateProviderInfo(provider, 0, packageUserState, userId);
                cb.onDeal(provider, info);
            }
        }
    }

    @Nullable
    private static <T> void parseHost(int flag, Class<T> clz, Callback cb) {
        PackageManager pkgMgr = Utils.applicationContext.getPackageManager();
        if (clz == ServiceInfo.class) {
            List<PackageInfo> pkgInfos = pkgMgr.getInstalledPackages(PackageManager.GET_SERVICES);
            for (PackageInfo pkgInfo : pkgInfos) {
                for (ServiceInfo info : pkgInfo.services) {
//                    cb.onDeal(service, info);
                }
            }
        } else if (clz == ActivityInfo.class) {

            if (flag == PackageManager.GET_RECEIVERS) {
                List<PackageInfo> pkgInfos = pkgMgr.getInstalledPackages(PackageManager.GET_RECEIVERS);
                for (PackageInfo pkgInfo : pkgInfos) {
                    for (ActivityInfo info : pkgInfo.receivers) {
//                        cb.onDeal(receiver, info);
                    }
                }
            } else {
                List<PackageInfo> pkgInfos = pkgMgr.getInstalledPackages(PackageManager.GET_ACTIVITIES);
                for (PackageInfo pkgInfo : pkgInfos) {
                    for (ActivityInfo ac : pkgInfo.activities) {
//                        cb.onDeal(ac, info);
                    }
                }
            }
        } else if (clz == ProviderInfo.class) {
            List<PackageInfo> pkgInfos = pkgMgr.getInstalledPackages(PackageManager.GET_PROVIDERS);
            for (PackageInfo pkgInfo : pkgInfos) {
                for (ProviderInfo info : pkgInfo.providers) {
//                    cb.onDeal(provider, info);
                }
            }
        }
    }

}
