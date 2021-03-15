package android.os;

import java.util.Map;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Nov/27/2020  Fri
 */
public class ServiceManager {
    public static IBinder getService(String name) {
        throw new RuntimeException("Stub!");
    }
    public static void addService(final String name, final IBinder service) {
        throw new RuntimeException("Stub!");
    }

    public static void addService(final String name, final IBinder service, final boolean allowIsolated) {
        throw new RuntimeException("Stub!");
    }

    public static IBinder checkService(final String name) {
        throw new RuntimeException("Stub!");
    }

    public static String[] listServices() throws RemoteException {
        throw new RuntimeException("Stub!");
    }

    public static void initServiceCache(final Map<String, IBinder> cache) {
        throw new RuntimeException("Stub!");
    }
}
