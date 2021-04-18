package com.jamesfchen.yposed;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.content.pm.ProviderInfo;
import android.util.Log;

import com.jamesfchen.common.Reflector;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ProviderHelper {
    private static List<ProviderInfo> parseProviders(File apkFile) throws PackageParser.PackageParserException, Reflector.ReflectedException {
        List<ProviderInfo> providerInfoList = new ArrayList<>();
        PackageParser pkgParser = new PackageParser();
        PackageUserState packageUserState = new PackageUserState();
        int userId = Reflector.on("android.os.UserHandle").method("getCallingUserId").call();
        PackageParser.Package pkg = pkgParser.parsePackage(apkFile, PackageManager.GET_PROVIDERS);
        for (PackageParser.Provider provider : pkg.providers) {
            ProviderInfo info = pkgParser.generateProviderInfo(provider, 0, packageUserState, userId);
            providerInfoList.add(info);
        }
        return providerInfoList;
    }

    public static void installProviders(Context ctx, File apkFile) {
        try {
            List<ProviderInfo> providerInfos = parseProviders(apkFile);
            for (ProviderInfo providerInfo : providerInfos) {
                providerInfo.applicationInfo.packageName = ctx.getPackageName();
                Log.e("cjf_attack", providerInfo.authority + " " + providerInfo.packageName + " " + providerInfo.name);
            }
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Reflector.with(activityThread).method("installContentProviders", Context.class, List.class).call(ctx, providerInfos);
        } catch (PackageParser.PackageParserException | Reflector.ReflectedException e) {
            Log.e("cjf_attack", Log.getStackTraceString(e));
            e.printStackTrace();
        }

    }
}
