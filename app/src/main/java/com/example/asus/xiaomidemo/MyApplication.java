package com.example.asus.xiaomidemo;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by asus on 2017/10/27.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
//        AppUtil.getInstalledApp(this);
    }
}
