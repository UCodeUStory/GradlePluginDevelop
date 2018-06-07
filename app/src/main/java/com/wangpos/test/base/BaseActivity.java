package com.wangpos.test.base;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by qiyue on 2018/6/7.
 */

public class BaseActivity extends AppCompatActivity {



    protected void toast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }


}
