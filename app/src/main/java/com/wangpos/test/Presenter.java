package com.wangpos.test;

import android.util.Log;

/**
 * Created by qiyue on 2017/11/6.
 */

public class Presenter {


    public void onResume(){

    }





    @OnResume(1)
    public void test(){
        Log.i("info","test is invoke");
    }

    @OnResume(1)
    public void test2(){
        Log.i("info","test2 is invoke");
    }
}
