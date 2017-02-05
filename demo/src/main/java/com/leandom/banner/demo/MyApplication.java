package com.leandom.banner.demo;

import android.app.Application;

/**
 * Created by xp on 2016/11/11.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this, true);
//        BlockCanary.install(this, new AppBlockCanaryContext()).start();
    }
}
