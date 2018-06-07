package com.wangpos.test;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wangpos.test.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Presenter presenter;

    private Button btnRequestNetWrok;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("info","onCreate start");
        Log.i("info","onCreate end");


//        /Users/qiyue/GitProject/GradlePlugin/app/build/intermediates/classes/debug

        presenter = new Presenter();

        if (BuildConfig.isDebug){
            toast(BuildConfig.FLAVOR+"debug版本");
        }




        //        //执行定时任务：
//        PeriodicWorkRequest locationWork = new PeriodicWorkRequest.Builder(UploadWork
//                .class, 15, TimeUnit.MINUTES).addTag(UploadWork.TAG)
//                .setConstraints(constraints).build();
//        WorkManager.getInstance().cancelWorkById(oneTimeWorkRequest.getId());

        /**
         * 你也可以让多个任务按顺序执行：

         WorkManager.getInstance(this)
         .beginWith(Work.from(LocationWork.class))
         .then(Work.from(LocationUploadWorker.class))
         .enqueue();
         你还可以让多个任务同时执行：

         WorkManager.getInstance(this).enqueue(Work.from(LocationWork.class,
         LocationUploadWorker.class));
         */



        btnRequestNetWrok = (Button) findViewById(R.id.btn_request_network);
        btnRequestNetWrok.setOnClickListener(this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_request_network:
                exeWorkByNetWork();
                break;
        }
    }

    /**
     * 模拟一个网络请求
     */
    private void exeWorkByNetWork() {
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(
                UploadWork.class
        ).setConstraints(constraints).build();

        WorkManager.getInstance().getStatusById(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkStatus>() {
                    @Override
                    public void onChanged(@Nullable WorkStatus workStatus) {
                        if (workStatus != null) {
                            String result = workStatus.getOutputData().getString("result", "");
                            toast(result);

                        }
                    }
                });

        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
    }




}
