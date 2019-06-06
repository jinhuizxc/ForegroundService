package com.example.foregroundservice.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.foregroundservice.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG, "MainActivity onCreate() PID:" + android.os.Process.myPid() + " TID:" + android.os.Process.myTid());
        setContentView(R.layout.activity_main_new);
    }

    public void onClick_Schedule(View view) {
        Log.w(TAG, "MainActivity onClick_Schedule()");
        Helpers.schedule(this);
    }

    public void onClick_Finished(View view) {
        Log.w(TAG, "MainActivity onClick_Finished()");
        Helpers.jobFinished();
    }

    public void onClick_Cancel(View view) {
        Log.w(TAG, "MainActivity onClick_Cancel()");
        Helpers.cancelJob(this);
    }

    public void onClick_Enqueue(View view) {
        Log.w(TAG, "MainActivity onClick_Enqueue()");
        Helpers.enqueueJob();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "MainActivity onDestroy()");
    }
}
