package com.example.foregroundservice.way2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.foregroundservice.R;

/**
 * 参考: https://github.com/lyldding/foregroundservice
 *
 * https://stackoverflow.com/questions/10962418/how-to-startforeground-without-showing-notification
 *
 */
public class WayTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
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

    /**
     * 正常启动ForegroundService ，调用startForeground并指明NOTIFICATION_ID 。
     * 启动ForegroundEnablingService，调用startForeground并指明相同NOTIFICATION_ID
     * ，再关闭该Service，即可隐藏通知。
     */
    private void startService() {
        Intent intent = new Intent(this, FordService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void stopService() {
        Intent intent = new Intent(this, FordService.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService();
    }
}
