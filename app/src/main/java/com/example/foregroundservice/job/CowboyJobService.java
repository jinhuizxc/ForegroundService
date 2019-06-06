package com.example.foregroundservice.job;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.orhanobut.logger.Logger;


/**
 * created by z on 2018/11/19
 * 系统5.0以上使用的服务
 *
 * JobService示例
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CowboyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //此处进行自定义的任务
        Logger.d("CowboyJobService jobParameters = " + jobParameters);
        Toast.makeText(this, "do something!", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.d("CowboyJobService onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("CowboyJobService onDestroy");
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}

