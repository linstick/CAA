package com.luoruiyong.caa.utils;

import android.util.TypedValue;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class DisplayUtils {


    public static int dp2px(int dpValue) {
        float scale = ResourcesUtils.getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, ResourcesUtils.getDisplayMetrics());
    }

    public static int px2dp(float pxValue) {
        float scale = ResourcesUtils.getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getDensityDpi() {
        return ResourcesUtils.getDisplayMetrics().densityDpi;
    }

    public static int getScreenWidth() {
        return ResourcesUtils.getDisplayMetrics().widthPixels;
    }

    public static int getScreeHeight() {
        return ResourcesUtils.getDisplayMetrics().heightPixels;
    }

}
