package com.jamesfchen.plugin;

import android.app.ActivityThread;
import android.content.Context;
import android.content.pm.PackageParser;
import android.content.pm.ProviderInfo;
import android.util.Log;

import com.jamesfchen.common.PackageHelper;
import com.jamesfchen.common.Reflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ProviderHelper {

    public static void installProviders(Context ctx, File apkFile) {
        try {
            List<ProviderInfo> outputs = new ArrayList<>();
            PackageHelper.parseProviders(apkFile, outputs, (cmp, info) -> {
                info.applicationInfo.packageName = ctx.getPackageName();
                ProviderInfo providerInfo = (ProviderInfo) info;
                Log.e("cjf_attack", providerInfo.authority + " " + providerInfo.packageName + " " + providerInfo.name);
            });
            ActivityThread activityThread = ActivityThread.currentActivityThread();
            Reflector.with(activityThread).method("installContentProviders", Context.class, List.class).call(ctx, outputs);
        } catch (PackageParser.PackageParserException | Reflector.ReflectedException e) {
            Log.e("cjf_attack", Log.getStackTraceString(e));
            e.printStackTrace();
        }

    }
}
