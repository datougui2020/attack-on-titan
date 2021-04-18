package com.jamesfchen.yposed;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class H {
    static int count = 0;

    public static void a(Uri uri, Context ctx,String s) {
        ContentValues values = new ContentValues();
        values.put("name", s+"name" + count++);
        ctx.getContentResolver().insert(uri, values);
        Cursor cursor = ctx.getContentResolver().query(uri, null, null, null, null);
        while (cursor.moveToNext()) {
            int count = cursor.getColumnCount();
            StringBuilder sb = new StringBuilder("column: ");
            for (int i = 0; i < count; i++) {
                sb.append(cursor.getString(i) + ", ");
            }
            Log.e("cjf_attack", sb.toString());
        }
    }
}
