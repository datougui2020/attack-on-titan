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
