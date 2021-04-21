package com.jamesfchen.plugin;

import android.content.IContentProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IContentProviderProxy implements InvocationHandler {
    IContentProvider origin;

    public IContentProviderProxy(IContentProvider origin) {
        this.origin = origin;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(origin, args);
    }
}
