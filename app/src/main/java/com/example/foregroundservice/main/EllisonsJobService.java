package com.example.foregroundservice.main;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

/**
 * JobService的使用介绍
 * https://blog.csdn.net/allisonchen/article/details/79218713
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class EllisonsJobService extends JobService {

    public static final int ELLISONS_JOB_ID = 0;
    public static final int ELLISONS_JOB_OVERDIDE_DEADLINE = 1000;
    private static final String TAG = "EllisonsJobService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.w(TAG, "EllisonsJobService onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "EllisonsJobService destroyed.");
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.w(TAG, "EllisonsJobService onStartJob()");

        Helpers.doHardWork(this, params);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.w(TAG, "EllisonsJobService stopped & wait to restart params:" + params + " reason:");
        return false;
    }
}
