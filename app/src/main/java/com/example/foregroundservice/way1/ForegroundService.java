package com.example.foregroundservice.way1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.orhanobut.logger.Logger;

/**
 * 将通知代码注释后异常:
 * Process: it.com.foregroundservice, PID: 29427
 * android.app.RemoteServiceException: Context.startForegroundService() did not then call Service.startForeground()
 * at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1780)
 * at android.os.Handler.dispatchMessage(Handler.java:105)
 * at android.os.Looper.loop(Looper.java:251)
 * at android.app.ActivityThread.main(ActivityThread.java:6572)
 * at java.lang.reflect.Method.invoke(Native Method)
 * at com.android.internal.os.Zygote$MethodAndArgsCaller.run(Zygote.java:240)
 * at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:767)
 */
public class ForegroundService extends Service {

    private static final String TAG = "FordService";
    private static final String CHANNEL_ID = "11111";
    private static final String CHANNEL_NAME = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");

        if (Build.VERSION.SDK_INT < 18) {
            startForeground(1, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
            Logger.d("Build.VERSION.SDK_INT < 18: ");
        } else {
            Logger.d("Build.VERSION.SDK_INT > 18: ");
            // 设置显示通知
            NotificationChannel channel;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_HIGH);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.createNotificationChannel(channel);

                Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
                startForeground(1, notification);
                stopForeground(true);
            }
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Logger.d("onDestroy: ");
        super.onDestroy();
    }
}
