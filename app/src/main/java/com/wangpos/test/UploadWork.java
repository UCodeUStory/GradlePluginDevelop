package com.wangpos.test;

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

        Log.i("info","upload success");



        Data resultData = new Data.Builder().putString("result","--^_^--").build();
        setOutputData(resultData);
        return WorkerResult.SUCCESS;
    }
}
