package com.example.foregroundservice.main;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class Helpers {


    private static final String TAG = "Helpers";
    private static JobService mJob;
    private static JobParameters mJobParams;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void schedule(Context context) {
        Log.w(TAG, "Helpers schedule()");

        final JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        final JobInfo.Builder builder = new JobInfo.Builder(EllisonsJobService.ELLISONS_JOB_ID,
                new ComponentName(context, EllisonsJobService.class));

        builder.setOverrideDeadline(EllisonsJobService.ELLISONS_JOB_OVERDIDE_DEADLINE);
        scheduler.schedule(builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void cancelJob(Context context) {
        Log.w(TAG, "Helpers cancelJob()");
        final JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        scheduler.cancel(EllisonsJobService.ELLISONS_JOB_ID);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void jobFinished() {
        Log.w(TAG, "Helpers jobFinished()");
        mJob.jobFinished(mJobParams, false);
    }

    public static void enqueueJob() {
        Log.w(TAG, "Helpers enqueueJob()");
    }

    public static void doHardWork(JobService job, JobParameters params) {
        Log.w(TAG, "Helpers doHardWork()");
        mJob = job;
        mJobParams = params;
    }
}
