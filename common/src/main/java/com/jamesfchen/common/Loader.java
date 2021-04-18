package com.jamesfchen.common;

import android.app.ActivityThread;
import android.app.LoadedApk;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.content.res.CompatibilityInfo;
import android.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;
import dalvik.system.DexFile;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 四月/07/2021  星期三
 */
public final class Loader {
    public static void hotfix(ClassLoader classLoader, File apkFile, File optDexFile) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, IOException, InvocationTargetException, InstantiationException {

    }

    public static void loadDex(ClassLoader classLoader, File apkFile, File optDexFile) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, IOException, InvocationTargetException, InstantiationException {
        // 获取 BaseDexClassLoader : pathList
        Field pathListField = DexClassLoader.class.getSuperclass().getDeclaredField("pathList");
        pathListField.setAccessible(true);
        Object pathListObj = pathListField.get(classLoader);

        // 获取 PathList: Element[] dexElements
        Field dexElementArray = pathListObj.getClass().getDeclaredField("dexElements");
        dexElementArray.setAccessible(true);
        Object[] dexElements = (Object[]) dexElementArray.get(pathListObj);

        // Element 类型
        Class<?> elementClass = dexElements.getClass().getComponentType();

        // 创建一个数组, 用来替换原始的数组
        Object[] newElements = (Object[]) Array.newInstance(elementClass, dexElements.length + 1);

        // 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile) 这个构造函数
        Constructor<?> constructor = elementClass.getConstructor(File.class, boolean.class, File.class, DexFile.class);
        Object o = constructor.newInstance(apkFile, false, apkFile, DexFile.loadDex(apkFile.getCanonicalPath(), optDexFile.getAbsolutePath(), 0));

        Object[] toAddElementArray = new Object[]{o};
        // 把原始的elements复制进去
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        // 插件的那个element复制进去
        System.arraycopy(toAddElementArray, 0, newElements, dexElements.length, toAddElementArray.length);

        // 替换
        dexElementArray.set(pathListObj, newElements);
    }

    public static Map<String, LoadedApk> sLoadedApk = new HashMap<>();

    public static void loadApk(ClassLoader classLoader, File apkFile) throws NoSuchFieldException, IllegalAccessException {
        ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
        Field mPackagesField = ActivityThread.class.getDeclaredField("mPackages");
        mPackagesField.setAccessible(true);
        ArrayMap<String, WeakReference<LoadedApk>> mPackages = (ArrayMap<String, WeakReference<LoadedApk>>) mPackagesField.get(currentActivityThread);

        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = generateApplicationInfo(apkFile);
        } catch (PackageParser.PackageParserException e) {
            e.printStackTrace();
            Log.e("cjf_attack", Log.getStackTraceString(e));
        }
        LoadedApk loadedApk = currentActivityThread.getPackageInfoNoCheck(applicationInfo, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO);

        String odexPath = Utils.getPluginOptDexDir(applicationInfo.packageName).getPath();
        String libDir = Utils.getPluginLibDir(applicationInfo.packageName).getPath();

        DexClassLoader dexClassLoader = new DexClassLoader(apkFile.getPath(), odexPath, libDir, ClassLoader.getSystemClassLoader());
        Field mClassLoaderField = LoadedApk.class.getDeclaredField("mClassLoader");
        mClassLoaderField.setAccessible(true);
        mClassLoaderField.set(loadedApk, dexClassLoader);

        sLoadedApk.put(applicationInfo.packageName, loadedApk);
        WeakReference<LoadedApk> weakReference = new WeakReference<>(loadedApk);
        mPackages.put(applicationInfo.packageName, weakReference);
    }

    public static ApplicationInfo generateApplicationInfo(File apkFile) throws PackageParser.PackageParserException {
        PackageParser pkgParser = new PackageParser();
        PackageParser.Package pkg = pkgParser.parsePackage(apkFile, 0);
        PackageUserState pkgUserState = new PackageUserState();
        ApplicationInfo applicationInfo = PackageParser.generateApplicationInfo(pkg, 0, pkgUserState);
        String apkPath = apkFile.getPath();
        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;
        return applicationInfo;
    }
}
