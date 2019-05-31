package com.example.foregroundservice.way2;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.orhanobut.logger.Logger;

public class ForegroundEnablingService extends Service {

//    private static final int NOTIFICATION_ID = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("ForegroundEnablingService onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("ForegroundEnablingService onStartCommand");

        if (FordService.instance == null)
            throw new RuntimeException(FordService.class.getSimpleName() + " not running");

        //Set both services to foreground using the same notification id, resulting in just one notification
        startForeground(FordService.instance);
        startForeground(this);

        //Cancel this service's notification, resulting in zero notifications
        stopForeground(true);

        //Stop this service so we don't waste RAM.
        //Must only be called *after* doing the work or the notification won't be hidden.
        stopSelf();
        return START_NOT_STICKY;
    }



    private static void startForeground(Service service) {
        Notification notification = new Notification.Builder(service).getNotification();
        service.startForeground(FordService.NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("ForegroundEnablingService onDestroy");
    }
}
