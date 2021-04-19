package com.jamesfchen.hook;

import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Copyright ® $ 2020
 * All right reserved.
 *
 * @author: hawksjamesf
 * @email: hawksjamesf@gmail.com
 * @since: Nov/27/2020  Fri
 */
public class PackageManagerProxy implements InvocationHandler {
    private IPackageManager mPackageManager;
    public PackageManagerProxy(IPackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Log.d("hook", "method name:" + method.getName());
        if ("getPackageInfo".equals(method.getName())) {
            return new PackageInfo();
        }
        return method.invoke(mPackageManager, args);
    }
}

