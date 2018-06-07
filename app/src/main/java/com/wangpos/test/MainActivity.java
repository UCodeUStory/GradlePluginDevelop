package com.wangpos.test;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Presenter presenter;

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


        Work



    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }



    private void toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


}
