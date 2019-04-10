package com.luoruiyong.caa.utils;

import android.util.Log;

import com.luoruiyong.caa.Enviroment;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class LogUtils {

    public static final int VERBOSE = 0;
    public static final int INFO = 1;
    public static final int WARN = 2;
    public static final int DEBUG = 3;
    public static final int ERROR = 4;

    public static void v(String tag, String info) {
        if (VERBOSE >= Enviroment.LOG_GRADE) {
            Log.v(tag, info);
        }
    }

    public static void i(String tag, String info) {
        if (INFO >= Enviroment.LOG_GRADE) {
            Log.i(tag, info);
        }
    }

    public static void w(String tag, String info) {
        if (WARN >= Enviroment.LOG_GRADE) {
            Log.w(tag, info);
        }
    }

    public static void d(String tag, String info) {
        if (DEBUG >= Enviroment.LOG_GRADE) {
            Log.d(tag, info);
        }
    }

    public static void e(String tag, String info) {
        if (ERROR >= Enviroment.LOG_GRADE) {
            Log.e(tag, info);
        }
    }

}
