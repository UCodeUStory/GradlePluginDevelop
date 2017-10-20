package com.wangpos.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("qiyue","--------start---------");
        new Test();
        Log.i("qiyue","---------end--------");
//        /Users/qiyue/GitProject/GradlePlugin/app/build/intermediates/classes/debug
    }
}
