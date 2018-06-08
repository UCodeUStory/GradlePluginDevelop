package com.wangpos.test.work;

import android.support.annotation.NonNull;
import android.util.Log;

import androidx.work.Data;
import androidx.work.Worker;

/**
 * Created by qiyue on 2018/6/6.
 */

public class UploadWork extends Worker {

    public static final String TAG = UploadWork.class.getSimpleName();

    @NonNull
    @Override
    public WorkerResult doWork() {

        Log.i(TAG,"模拟上传中......");

        boolean isTest = getInputData().getBoolean("isTest",false);

//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        Data resultData = new Data.Builder().putString("result","--^_^--"+isTest).build();
        setOutputData(resultData);
        return WorkerResult.SUCCESS;
    }
}
