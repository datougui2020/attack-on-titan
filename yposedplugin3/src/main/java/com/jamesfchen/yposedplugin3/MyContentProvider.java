package com.jamesfchen.yposedplugin3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyContentProvider extends ContentProvider {
    @Override
    public boolean onCreate() {
        Log.e("cjf_attack", "yposedplugin3 onCreate");
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e("cjf_attack", "yposedplugin3 query");
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.e("cjf_attack", "yposedplugin3 getType");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e("cjf_attack", "yposedplugin3 insert");
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("cjf_attack", "yposedplugin3 delete");
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e("cjf_attack", "yposedplugin3 update");
        return 0;
    }
}
