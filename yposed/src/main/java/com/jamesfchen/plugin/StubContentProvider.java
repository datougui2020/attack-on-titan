package com.jamesfchen.plugin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 *
 *  public boolean canRunHere(ProcessRecord app) {
 *         return (info.multiprocess || info.processName.equals(app.processName))
 *                 && uid == app.info.uid;
 *     }
 *  ContentProvider配置的multiprocess为true or 调用者进程 等于 ContentProvider所在进程 and  调用者进程uid 等于 ContentProvider所在进程的uid
 *
 *  如果ContentProvider multiprocess为false 并且没有设置独立进程，那么同一App下的各个Activity都是持有同一个ContentProvider对象，仅为同一App单例
 *  如果ContentProvider 设置独立进程，同一个App下的Activity与不同App下持有一个ContentProvider，各个App单例
 *  如果ContentProvider的multiprocess为true 并且设置独立进程，那么同一个App下 各个Activity都是不同的ContentProvider
 *
 */
public class StubContentProvider extends ContentProvider {
    @Override
    public void attachInfo(Context context, ProviderInfo info) {
        Log.e("cjf_attack", "StubContentProvider attachInfo");
        super.attachInfo(context, info);
    }
    @Override
    public boolean onCreate() {
        Log.e("cjf_attack", "StubContentProvider onCreate");
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("cjf_attack", "StubContentProvider query");
        return getContext().getContentResolver().query(getRealUri(uri), projection, selection, selectionArgs, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e("cjf_attack", "StubContentProvider insert");
        return getContext().getContentResolver().insert(getRealUri(uri), values);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private Uri getRealUri(Uri raw) {
        String rawAuth = raw.getAuthority();
        if (!"com.jamesfchen.titan.stub_provider".equals(rawAuth)) {
            Log.e("cjf_attack", "StubContentProvider getRealUri " + rawAuth);
        }
        String uriString = raw.toString();
        uriString = uriString.replaceAll(rawAuth + '/', "");
        Uri newUri = Uri.parse(uriString);
        Log.e("cjf_attack", "StubContentProvider getRealUri " + newUri);
        return newUri;
    }
}
