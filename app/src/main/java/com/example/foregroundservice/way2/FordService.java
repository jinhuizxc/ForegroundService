package com.example.foregroundservice.way2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.foregroundservice.service.SocketService;
import com.orhanobut.logger.Logger;

/**
 * Android调用startForeground 但不显示通知
 */
public class FordService extends Service {

    static FordService instance;
    public static int NOTIFICATION_ID = 1;

    public static final String CHANNEL_ID = "CHANNEL_ID";

    public static final String CHANNEL_NAME = "Test";  // 通道的用户可见名称
    // 建立通知
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("FordService onCreate");
        instance = this;

        // 设置显示通知
//        NotificationChannel channel;
//        if (android.os.Build.VERSION.SDK_INT >= 26) {
//            channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
//                    NotificationManager.IMPORTANCE_HIGH);
//            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//            manager.createNotificationChannel(channel);
//
//            Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).build();
//            startForeground(NOTIFICATION_ID, notification);
//        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(NOTIFICATION_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else if (Build.VERSION.SDK_INT > 18 && Build.VERSION.SDK_INT < 25) {
            //Android4.3 - Android7.0，隐藏Notification上的图标
            Intent innerIntent = new Intent(this, ForegroundEnablingService.class);
            startService(innerIntent);
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);//设置提示灯
                notificationChannel.setLightColor(Color.RED);//设置提示灯颜色
                notificationChannel.setShowBadge(true);//显示logo
                notificationChannel.setDescription("socketService");// 设置描述
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
                notificationManager.createNotificationChannel(notificationChannel);  // 创建通知渠道
                // 自定义通知栏
//                Notification notification = new Notification.Builder(this)
//                        .setChannelId(CHANNEL_ID)
//                        .build();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
                builder.setChannelId(CHANNEL_ID);
                builder.build();
                builder.setAutoCancel(true);

                Intent innerIntent = new Intent(this, ForegroundEnablingService.class);
                startService(innerIntent);
//                startForeground(NOTIFICATION_ID, builder.build());
            }
        }

        if (startService(new Intent(this, ForegroundEnablingService.class)) == null)
            throw new RuntimeException("Couldn't find " + ForegroundEnablingService.class.getSimpleName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("FordService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("FordService onDestroy");
        instance = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
