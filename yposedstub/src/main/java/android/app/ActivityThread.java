package android.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.CompatibilityInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.ArrayMap;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public final class ActivityThread {

    final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap<>();
    final ArrayMap<String, WeakReference<LoadedApk>> mResourcePackages = new ArrayMap<>();
    final ArrayList<Application> mAllApplications = new ArrayList<Application>();
    final ArrayMap<IBinder,ActivityClientRecord> mActivities = new ArrayMap<>();//IBinder为框架层的activity record,其作为token
    final ArrayMap<IBinder, Service> mServices = new ArrayMap<>();//IBinder为框架层的services record,其作为token用来和框架层交互
    final ArrayMap<IBinder, ProviderClientRecord> mLocalProviders = new ArrayMap<IBinder, ProviderClientRecord>();//IBinder为开发者自定义ContentProvider
    final ArrayMap<ComponentName, ProviderClientRecord> mLocalProvidersByName = new ArrayMap<ComponentName, ProviderClientRecord>();//ComponentName为开发者自定义ContentProvider的类名
    //    final ArrayMap<IBinder, ProviderRefCount> mProviderRefCountMap = new ArrayMap<IBinder, ProviderRefCount>();//外部的ContentProvider
    final ArrayMap<ProviderKey, ProviderClientRecord> mProviderMap = new ArrayMap<ProviderKey, ProviderClientRecord>();//保存远程和本地所有的provider
    //    final ArrayMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners = new ArrayMap<Activity, ArrayList<OnActivityPausedListener>>();

    static final class ActivityClientRecord { IBinder token;}
    final class ProviderClientRecord {}
    private static final class ProviderKey{}
    static final class ReceiverData{}
    static final class CreateServiceData{IBinder token;}
    static final class BindServiceData{IBinder token;}
    static final class ServiceArgsData{IBinder token;}
    static final class AppBindData{}
    private class ApplicationThread extends IApplicationThread.Stub{}
    //    private class ApplicationThread extends ApplicationThreadNative { }

    public static ActivityThread currentActivityThread() { throw new RuntimeException("Stub!"); }
    public static boolean isSystem() { throw new RuntimeException("Stub!"); }
    public static String currentOpPackageName() { throw new RuntimeException("Stub!"); }
    public static String currentPackageName() { throw new RuntimeException("Stub!"); }
    public static String currentProcessName() { throw new RuntimeException("Stub!"); }
    public static Application currentApplication() { throw new RuntimeException("Stub!"); }
    public static IPackageManager getPackageManager() { throw new RuntimeException("Stub!"); }
    final Handler getHandler() { throw new RuntimeException("Stub!"); }
    public final LoadedApk getPackageInfo(String packageName,CompatibilityInfo compatInfo,int flags){ throw new RuntimeException("Stub!"); }
    public final LoadedApk getPackageInfo(String packageName,CompatibilityInfo compatInfo,int flags,int userId){ throw new RuntimeException("Stub!"); }
    public final LoadedApk getPackageInfo(ApplicationInfo ai,CompatibilityInfo compatInfo,int flags){ throw new RuntimeException("Stub!"); }
    public final LoadedApk getPackageInfo(ApplicationInfo ai,CompatibilityInfo compatInfo,ClassLoader baseLoader,
                                          boolean securityViolation,boolean includeCode,boolean registerPackage){ throw new RuntimeException("Stub!"); }
    public ApplicationThread getApplicationThread() { throw new RuntimeException("Stub!"); }
    public Instrumentation getInstrumentation() {
        throw new RuntimeException("Stub!");
    }
    public Looper getLooper() { throw new RuntimeException("Stub!"); }
    public Application getApplication() {
        throw new RuntimeException("Stub!");
    }
    public String getProcessName() {
        throw new RuntimeException("Stub!");
    }
    public final ActivityInfo resolveActivityInfo(final Intent intent) { throw new RuntimeException("Stub!"); }
    public final Activity getActivity(final IBinder token) {
        throw new RuntimeException("Stub!");
    }
    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo) { throw new RuntimeException("Stub!"); }

    private void handleLaunchActivity(ActivityClientRecord r,Intent customIntent,String reason){throw new RuntimeException("Stub!");}
    private void handleInstallProvider(ProviderInfo info){throw new RuntimeException("Stub!");}
    private void handleReceiver(ReceiverData data){throw new RuntimeException("Stub!");}
    private void handleCreateService(CreateServiceData data){throw new RuntimeException("Stub!");}
    private void handleBindService(BindServiceData data){throw new RuntimeException("Stub!");}
    private void handleUnbindService(BindServiceData data){throw new RuntimeException("Stub!");}
    private void handleServiceArgs(ServiceArgsData data){throw new RuntimeException("Stub!");}
    private void handleStopService(IBinder token){throw new RuntimeException("Stub!");}
    final void handleResumeActivity(IBinder token,boolean clearHide,boolean isForward,boolean reallyResume,int seq,String reason){throw new RuntimeException("Stub!");}
    private void handlePauseActivity(IBinder token,boolean finished,boolean userLeaving,int configChanges,boolean dontReport,int seq){throw new RuntimeException("Stub!");}
    private void handleStopActivity(IBinder token,boolean show,int configChanges,int seq){throw new RuntimeException("Stub!");}
    private void handleDestroyActivity(IBinder token,boolean finishing,int configChanges,boolean getNonConfigInstance){throw new RuntimeException("Stub!");}

    void handleApplicationInfoChanged(final ApplicationInfo ai){throw new RuntimeException("Stub!");}
//    void handleActivityConfigurationChanged(ActivityConfigChangeData data,int displayId){throw new RuntimeException("Stub!");}
    private void handleBindApplication(AppBindData data){throw new RuntimeException("Stub!");}

}
