package com.luoruiyong.caa;

import android.Manifest;
import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class MyApplication extends Application {

    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        Fresco.initialize(this);
    }

    public static Context getAppContext() {
        return sContext;
    }
}
