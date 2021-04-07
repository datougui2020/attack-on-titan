package com.jamesfchen.common;

import android.app.ActivityThread;
import android.app.LoadedApk;
import android.content.pm.ApplicationInfo;
import android.content.res.CompatibilityInfo;
import android.util.ArrayMap;

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

        Object[] toAddElementArray = new Object[] { o };
        // 把原始的elements复制进去
        System.arraycopy(dexElements, 0, newElements, 0, dexElements.length);
        // 插件的那个element复制进去
        System.arraycopy(toAddElementArray, 0, newElements, dexElements.length, toAddElementArray.length);

        // 替换
        dexElementArray.set(pathListObj, newElements);
    }

    public static Map<String, Object> sLoadedApk = new HashMap<String, Object>();
    public static void loadApk(ClassLoader classLoader, File apkFile) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
        Field mPackagesField = ActivityThread.class.getDeclaredField("mPackages");
        mPackagesField.setAccessible(true);
        ArrayMap<String, WeakReference<LoadedApk>> mPackages= (ArrayMap<String, WeakReference<LoadedApk>>) mPackagesField.get(currentActivityThread);

        Method getPackageInfoNoCheckMethod = ActivityThread.class.getDeclaredMethod("getPackageInfoNoCheck", ApplicationInfo.class, CompatibilityInfo.class);
        getPackageInfoNoCheckMethod.setAccessible(true);
        Field default_compatibility_infoField = CompatibilityInfo.class.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
        default_compatibility_infoField.setAccessible(true);
        Object defaultCompatibilityInfo = default_compatibility_infoField.get(null);
        ApplicationInfo applicationInfo=generateApplicationInfo(apkFile);
        LoadedApk loadedApk = (LoadedApk) getPackageInfoNoCheckMethod.invoke(currentActivityThread,applicationInfo , defaultCompatibilityInfo);

        String odexPath = Utils.getPluginOptDexDir(applicationInfo.packageName).getPath();
        String libDir = Utils.getPluginLibDir(applicationInfo.packageName).getPath();

        DexClassLoader dexClassLoader = new DexClassLoader(apkFile.getPath(), odexPath, libDir, ClassLoader.getSystemClassLoader());
        Field mClassLoaderField = LoadedApk.class.getDeclaredField("mClassLoader");
        mClassLoaderField.setAccessible(true);
        mClassLoaderField.set(loadedApk, dexClassLoader);

        sLoadedApk.put(applicationInfo.packageName, loadedApk);
        WeakReference weakReference = new WeakReference(loadedApk);
        mPackages.put(applicationInfo.packageName, weakReference);
    }

    /**
     * 这个方法的最终目的是调用
     * android.content.pm.PackageParser#generateActivityInfo(android.content.pm.PackageParser.Activity, int, android.content.pm.PackageUserState, int)
     */
    public static ApplicationInfo generateApplicationInfo(File apkFile)
            throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchFieldException {

        // 找出需要反射的核心类: android.content.pm.PackageParser
        Class<?> packageParserClass = Class.forName("android.content.pm.PackageParser");

        // 我们的终极目标: android.content.pm.PackageParser#generateApplicationInfo(android.content.pm.PackageParser.Package,
        // int, android.content.pm.PackageUserState)
        // 要调用这个方法, 需要做很多准备工作; 考验反射技术的时候到了 - -!
        // 下面, 我们开始这场Hack之旅吧!

        // 首先拿到我们得终极目标: generateApplicationInfo方法
        // API 23 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // public static ApplicationInfo generateApplicationInfo(Package p, int flags,
        //    PackageUserState state) {
        // 其他Android版本不保证也是如此.
        Class<?> packageParser$PackageClass = Class.forName("android.content.pm.PackageParser$Package");
        Class<?> packageUserStateClass = Class.forName("android.content.pm.PackageUserState");
        Method generateApplicationInfoMethod = packageParserClass.getDeclaredMethod("generateApplicationInfo",
                packageParser$PackageClass,
                int.class,
                packageUserStateClass);

        // 接下来构建需要得参数

        // 首先, 我们得创建出一个Package对象出来供这个方法调用
        // 而这个需要得对象可以通过 android.content.pm.PackageParser#parsePackage 这个方法返回得 Package对象得字段获取得到
        // 创建出一个PackageParser对象供使用
        Object packageParser = packageParserClass.newInstance();
        // 调用 PackageParser.parsePackage 解析apk的信息
        Method parsePackageMethod = packageParserClass.getDeclaredMethod("parsePackage", File.class, int.class);

        // 实际上是一个 android.content.pm.PackageParser.Package 对象
        Object packageObj = parsePackageMethod.invoke(packageParser, apkFile, 0);

        // 第三个参数 mDefaultPackageUserState 我们直接使用默认构造函数构造一个出来即可
        Object defaultPackageUserState = packageUserStateClass.newInstance();

        // 万事具备!!!!!!!!!!!!!!
        ApplicationInfo applicationInfo = (ApplicationInfo) generateApplicationInfoMethod.invoke(packageParser,
                packageObj, 0, defaultPackageUserState);
        String apkPath = apkFile.getPath();

        applicationInfo.sourceDir = apkPath;
        applicationInfo.publicSourceDir = apkPath;

        return applicationInfo;
    }
}
