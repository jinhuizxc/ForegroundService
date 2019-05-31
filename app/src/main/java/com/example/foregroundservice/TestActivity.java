package com.example.foregroundservice;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.foregroundservice.service.SocketService;
import com.example.foregroundservice.utils.NotificationUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.orhanobut.logger.Logger;

/**
 * 测试service，测试通知;
 *
 * 目标: Android8.0如何隐藏掉通知栏“xxx正在运行” 取消！
 *
 * 是通知的问题, 就取消通知即可;
 *
 * 后台服务保活startForeground不显示前台通知
 * https://blog.csdn.net/hizhangyuping/article/details/80276947
 */
public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private SocketService.SocketClientBinder binder;
    private SocketService socketService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();


        // 检查是否打开了通知, 必须开启，否则可能8.0, 9.0手机收不到通知；
//        NotificationUtils.checkNotification(this);
//        checkOpenNotification();

    }

    private void checkOpenNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (!NotificationUtils.isNotificationEnabled(this)) {
                new CircleDialog.Builder()
                        .setTitle("您还未开启系统通知，可能会影响消息的接收，要去开启吗？")
                        .setTitleColor(getResources().getColor(R.color.black))
                        .setWidth(0.8f)
                        .setCancelable(false)
                        .setPositive("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // 跳转权限设置
                                NotificationUtils.gotoSet(TestActivity.this);
                            }
                        })
                        .setNegative("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        })
                        .show(getSupportFragmentManager());
            } else {
                Logger.d("onNext: " + "已开启通知权限");
            }
        }


    }


    private void initView() {
        Button startService = (Button) findViewById(R.id.start);
        Button stopService = (Button) findViewById(R.id.stop);

        startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });

    }

    private void startService() {
        Intent intent = new Intent(this, SocketService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void stopService() {
        Intent intent = new Intent(this, SocketService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }
}
