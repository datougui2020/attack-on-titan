package android.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.os.Handler;

import java.io.File;

public final class LoadedApk {
    private ClassLoader mClassLoader;
    public LoadedApk(ActivityThread activityThread, ApplicationInfo aInfo, CompatibilityInfo compatInfo,
                     ClassLoader baseLoader,boolean securityViolation,
                     boolean includeCode,boolean registerPackage){ throw new RuntimeException("Stub!");}
    public LoadedApk(ActivityThread activityThread){throw new RuntimeException("Stub!");}

    Application getApplication(){throw new RuntimeException("Stub!");}
    public String getPackageName() { throw new RuntimeException("Stub!"); }
    public ApplicationInfo getApplicationInfo() {
        throw new RuntimeException("Stub!");
    }

    public ClassLoader getClassLoader() {
        throw new RuntimeException("Stub!");
    }

    public String getAppDir() {
        throw new RuntimeException("Stub!");
    }

    public String getLibDir() {
        throw new RuntimeException("Stub!");
    }

    public String getResDir() {
        throw new RuntimeException("Stub!");
    }

    public String[] getSplitAppDirs() {
        throw new RuntimeException("Stub!");
    }

    public String[] getSplitResDirs() {
        throw new RuntimeException("Stub!");
    }

    public String[] getOverlayDirs() {
        throw new RuntimeException("Stub!");
    }

    public String getDataDir() {
        throw new RuntimeException("Stub!");
    }

    public File getDataDirFile() {
        throw new RuntimeException("Stub!");
    }

    public AssetManager getAssets(final ActivityThread mainThread) {
        throw new RuntimeException("Stub!");
    }

    public Resources getResources(final ActivityThread mainThread) {
        throw new RuntimeException("Stub!");
    }

    public IIntentReceiver getReceiverDispatcher(final BroadcastReceiver r, final Context context, final Handler handler, final Instrumentation instrumentation, final boolean registered) {
        throw new RuntimeException("Stub!");
    }

    public IIntentReceiver forgetReceiverDispatcher(final Context context, final BroadcastReceiver r) {
        throw new RuntimeException("Stub!");
    }
}
