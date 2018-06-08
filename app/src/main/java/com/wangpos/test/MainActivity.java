package com.wangpos.test;

import android.arch.lifecycle.Observer;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wangpos.test.base.BaseActivity;
import com.wangpos.test.work.UploadWork;

import java.util.concurrent.TimeUnit;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.State;
import androidx.work.WorkManager;
import androidx.work.WorkStatus;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private Presenter presenter;

    private Button btnRequestNetWrok;
    private Button btnPeriod;

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

//        WorkManager.getInstance().cancelWorkById(oneTimeWorkRequest.getId());





        btnRequestNetWrok = (Button) findViewById(R.id.btn_request_network);
        btnRequestNetWrok.setOnClickListener(this);
        btnPeriod = (Button) findViewById(R.id.btn_Period_work);
        btnPeriod.setOnClickListener(this);

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
            case R.id.btn_Period_work:
                exePeriodWork();
                break;
        }
    }

    /**
     * 模拟一个网络请求
     */
    private void exePeriodWork() {
        //notice 创建约束条件
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
        //notice 输入数据
        Data inputData = new Data.Builder().putBoolean("isTest", true).build();

        //notice 执行定时任务：设置工作的时间间隔
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(UploadWork
                .class, 10, TimeUnit.MINUTES).addTag(UploadWork.TAG)
                .setConstraints(constraints).setInputData(inputData).build();



        WorkManager.getInstance().cancelWorkById(periodicWorkRequest.getId());
        //notice 设置结果回调
        WorkManager.getInstance().getStatusById(periodicWorkRequest.getId())
                .observe(this, new Observer<WorkStatus>() {
                    @Override
                    public void onChanged(@Nullable WorkStatus workStatus) {

                        Log.i("result","state="+workStatus.getState());
                        if (workStatus != null && workStatus.getState() == State.SUCCEEDED) {
                            //notice 取出回调数据
                            String result = workStatus.getOutputData().getString("result", "");
                            Log.i("result","workStatus="+workStatus.getState());
                            toast(result);

                        }
                    }
                });
        //notice 执行
        WorkManager.getInstance().enqueue(periodicWorkRequest);


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
    }


    /**
     * 模拟一个网络请求
     */
    private void exeWorkByNetWork() {
        //notice 创建约束条件
        Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        //notice 输入数据
        Data inputData = new Data.Builder().putBoolean("isTest", true).build();

        //notice 构建请求类型，一次的请求
        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(
                UploadWork.class
        ).setConstraints(constraints).setInputData(inputData).build();


        //notice 设置结果回调
        WorkManager.getInstance().getStatusById(oneTimeWorkRequest.getId())
                .observe(this, new Observer<WorkStatus>() {
                    @Override
                    public void onChanged(@Nullable WorkStatus workStatus) {

                        if (workStatus != null && workStatus.getState() == State.SUCCEEDED) {
                            //notice 取出回调数据
                            String result = workStatus.getOutputData().getString("result", "");
                            Log.i("result","workStatus="+workStatus.getState());
                            toast(result);

                        }
                    }
                });
        //notice 执行
        WorkManager.getInstance().enqueue(oneTimeWorkRequest);


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
    }


    private void cancel(){
//        WorkManager.getInstance().cancelWorkById(oneTimeWorkRequest.getId());
    }


}
