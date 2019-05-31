package com.example.foregroundservice.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.example.foregroundservice.utils.NotificationUtils;
import com.orhanobut.logger.Logger;

/**
 * 测试通知;
 */

public class SocketService extends Service {


    private static final String TAG = "SocketService";

    public static final String CHANNEL_ID = "CHANNEL_ID";

    public static final String CHANNEL_NAME = "Test";  // 通道的用户可见名称

    // 建立通知
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;


    private SocketClientBinder mBinder = new SocketClientBinder();
    private final static int GRAY_SERVICE_ID = 1001;

    //用于Activity和service通讯
    public class SocketClientBinder extends Binder {
        public SocketService getService() {
            return SocketService.this;
        }
    }

    // 灰色保活
    public static class GrayInnerService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Logger.d( "GrayInnerService onCreate");
            startForeground(GRAY_SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            Logger.d("GrayInnerService onDestroy");
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Process: com.zx.android.platform, PID: 29807
     * android.app.RemoteServiceException: Bad notification for startForeground: java.lang.
     * RuntimeException: invalid channel for service notification: Notification(channel=null
     * pri=0 contentView=null vibrate=null sound=null defaults=0x0 flags=0x40 color=0x00000000
     * vis=PRIVATE)
     * at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1737)
     * <p>
     * 解决方法: setChannel()
     */
    @Override
    public void onCreate() {
        super.onCreate();

        Logger.d( "SocketService onCreate");

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            setChannel();
//        }
        //设置service为前台服务，提高优先级
        if (Build.VERSION.SDK_INT < 18) {
            //Android4.3以下 ，隐藏Notification上的图标
            startForeground(GRAY_SERVICE_ID, new Notification());
        } else if (Build.VERSION.SDK_INT > 18 && Build.VERSION.SDK_INT < 25) {
            //Android4.3 - Android7.0，隐藏Notification上的图标
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
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

//                Intent innerIntent = new Intent(this, GrayInnerService.class);
//                startService(innerIntent);

                /**
                 *  Process: it.com.foregroundservice, PID: 19406
                 *     android.app.RemoteServiceException: Context.startForegroundService()
                 *     did not then call Service.startForeground(): ServiceRecord{e786269 u0 it.com.foregroundservice/com.example.foregroundservice.service.SocketService}
                 *
                 */
                // 加了灰色保活显示"XX应用正在运行"
                startForeground(GRAY_SERVICE_ID, builder.build());
                // 清除通知
//                clearNotification();
            }
        }

    }

    /**
     * 获取创建一个NotificationManager的对象
     *
     * @return NotificationManager对象
     */
    public NotificationManager getManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    /**
     * 清空所有的通知
     */
    public void clearNotification() {
       getManager().cancelAll();
    }



    /**
     * START_NOT_STICKY;
     * START_STICKY:
     * START_REDELIVER_INTENT:
     *
     * @param intent
     * @param flags
     * @param startId
     * @return
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.d("SocketService onStartCommand");

        return START_STICKY;
    }


    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("SocketService onDestroy");
    }


    /**************设置8.0以上通知channel****************/
    /**
     * 设置8.0以上设置channel
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setChannel() {
        notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);//设置提示灯
        notificationChannel.setLightColor(Color.RED);//设置提示灯颜色
        notificationChannel.setShowBadge(true);//显示logo
        notificationChannel.setDescription("socketService");// 设置描述
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
        notificationManager.createNotificationChannel(notificationChannel);  // 创建通知渠道
        // 自定义通知栏
        Notification notification = new Notification.Builder(this)
                .setChannelId(CHANNEL_ID)
//                    .setContentTitle(CHANNEL_NAME)
//                    .setContentText("运行中...")
//                    .setWhen(System.currentTimeMillis())
//                    .setSmallIcon(R.mipmap.logo)
//                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .build();
        startForeground(1, notification);
    }


}
