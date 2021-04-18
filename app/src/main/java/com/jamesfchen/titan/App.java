package com.jamesfchen.titan;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jamesfchen.common.Loader;
import com.jamesfchen.common.Utils;
import com.jamesfchen.yposed.Hooker;
import com.jamesfchen.yposed.ProviderHelper;
import com.jamesfchen.yposed.ServiceManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexClassLoader;
import lab.galaxy.yahfa.HookMain;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Nov/20/2020  Fri
 */
public class App extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Utils.init(base);
//        Utils.extractAssets(base, "yposedplugin-debug.apk");
//        adfasf();
//        System.loadLibrary("gadget");
        haha();
        AsyncTask.execute(() -> {
            try {
                Utils.extractAssets(base, "yposedplugin2-debug.apk");
                Utils.extractAssets(base, "yposedplugin3-debug.apk");

                File dexFile = getFileStreamPath("yposedplugin2-debug.apk");
                File optDexFile = getFileStreamPath("yposedplugin2-debug.dex");
                Loader.loadDex(getClassLoader(), dexFile, optDexFile);
                Loader.loadApk(getClassLoader(), getFileStreamPath("yposedplugin3-debug.apk"));

                ProviderHelper.installProviders(base, getFileStreamPath("yposedplugin2-debug.apk"));
//                ProviderHelper.installProviders(base, getFileStreamPath("yposedplugin3-debug.apk"));
                ServiceManager.parseServices(getFileStreamPath("yposedplugin2-debug.apk"));
//                ServiceManager.parseServices(getFileStreamPath("yposedplugin3-debug.apk"));

            } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | IOException | InvocationTargetException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }

    private void haha() {
//        Hook.init(getClassLoader(),this);
        Hooker.init(getClassLoader(), this);
    }

    private void adfasf() {
        try {
            ClassLoader classLoader = getClassLoader();
            File pluginFile = getFileStreamPath("yposedplugin-debug.apk");
            Log.d("HookInfo", "initHotFix: " + pluginFile.getAbsolutePath());
            DexClassLoader dexClassLoader = new DexClassLoader(pluginFile.getAbsolutePath(), getCodeCacheDir().getAbsolutePath(), null, classLoader);
            HookMain.doHookDefault(dexClassLoader, classLoader);

//            BaseDexClassLoader dexClassLoader = null;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                ByteBuffer buffer = ReadFileToByteBufferDirect(new File("/sdcard/yposedplugin.dex"));
//                dexClassLoader = new InMemoryDexClassLoader(buffer, classLoader);
//            } else {
//                dexClassLoader = new PathClassLoader("/sdcard/yposedplugin.dex",
//                        classLoader);
//            }

        } catch (Exception e) {
        }

    }
}
