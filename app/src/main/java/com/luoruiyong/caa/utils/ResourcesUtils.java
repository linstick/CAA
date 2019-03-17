package com.luoruiyong.caa.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.luoruiyong.caa.MyApplication;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ResourcesUtils {

    public static String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }

    public static int getColor(@ColorRes int resId) {
        return ContextCompat.getColor(MyApplication.getAppContext(), resId);
    }

    public static Drawable getDrawable(@DrawableRes int resId) {
        return ContextCompat.getDrawable(MyApplication.getAppContext(), resId);
    }

    public static float getDimen(@DimenRes int resId) {
        return getResources().getDimension(resId);
    }

    public static DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    public static Resources getResources() {
        return MyApplication.getAppContext().getResources();
    }
}
