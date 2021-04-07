package lab.galaxy.yahfa;

import com.jamesfchen.yposedplugin.Hook_PackageManager_getPackageInfo;
import com.jamesfchen.yposedplugin.NetClient_sendRequest;

public class HookInfo {
    public static final String TAG = "HookInfo";
    public static String[] hookItemNames = {
            Hook_PackageManager_getPackageInfo.class.getName(),
            NetClient_sendRequest.class.getName(),
    };
}
