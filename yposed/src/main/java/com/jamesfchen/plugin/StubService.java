package com.jamesfchen.plugin;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * Copyright ® $ 2021
 * All right reserved.
 *
 * @author: jamesfchen
 * @email: hawksjamesf@gmail.com
 * @since: 四月/09/2021  星期五
 */
public class StubService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int r = ServiceManager.INSTANCE.createService(intent, flags, startId);
        return r == -1 ? super.onStartCommand(intent, flags, startId) : r;

    }
}
