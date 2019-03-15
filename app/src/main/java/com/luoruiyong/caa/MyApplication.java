package com.luoruiyong.caa;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.luoruiyong.caa.user.User;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class MyApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getAppContext() {
        return sContext;
    }
}
