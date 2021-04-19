package com.jamesfchen.hook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageParser;
import android.util.Log;

import com.jamesfchen.common.PackageHelper;
import com.jamesfchen.common.Reflector;
import com.jamesfchen.common.Utils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexClassLoader;

public class ReceiverHelper {
    public static Map<ActivityInfo, List<? extends IntentFilter>> sCache = new HashMap<>();

    public static void parseReceivers(Context context, File fileStreamPath) {
        try {
            PackageHelper.parseReceivers(fileStreamPath, sCache);
            DexClassLoader cl = null;
            for (ActivityInfo activityInfo : ReceiverHelper.sCache.keySet()) {
                List<? extends IntentFilter> intentFilters = ReceiverHelper.sCache.get(activityInfo);
                if (cl == null) {
                    cl = new DexClassLoader(fileStreamPath.getPath(), Utils.getPluginOptDexDir(activityInfo.packageName).getPath(),
                            Utils.getPluginLibDir(activityInfo.packageName).getPath(), Hooker.sApplication.getClassLoader());
                }
                for (IntentFilter intentFilter : intentFilters) {
                    BroadcastReceiver receiver = (BroadcastReceiver) cl.loadClass(activityInfo.name).newInstance();
                    context.registerReceiver(receiver, intentFilter);
                }
            }
        } catch (PackageParser.PackageParserException | Reflector.ReflectedException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Log.e("cjf_attack", Log.getStackTraceString(e));
            e.printStackTrace();
        }

    }
}
