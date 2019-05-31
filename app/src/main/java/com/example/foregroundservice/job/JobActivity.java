package com.example.foregroundservice.job;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.foregroundservice.R;

import java.util.concurrent.TimeUnit;

/**
 *   Android8.0如何隐藏掉通知栏“xxx正在运行”
 *  https://blog.csdn.net/qq_35145640/article/details/81510943
 */
public class JobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
                startPollingService(JobActivity.this);
            }
        });

        stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                stopService();
            }
        });

    }


    //开启JobService服务：CowboyJobService.class为上边创建的JobService
    public static void startPollingService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(context, CowboyJobService.class));
            builder.setMinimumLatency(0)
                    .setBackoffCriteria(JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS, JobInfo.BACKOFF_POLICY_LINEAR);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
            } else {
                builder.setPeriodic(TimeUnit.MINUTES.toMillis(5));
            }

            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
            if (jobScheduler != null) {
                jobScheduler.schedule(builder.build());
            }
        } else {
            //系统5.0以下的可继续使用Service

        }
    }

}
