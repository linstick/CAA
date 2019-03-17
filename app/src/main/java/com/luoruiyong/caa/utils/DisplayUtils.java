package com.luoruiyong.caa.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.Window;

import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;

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

    public static int getStatusBarHeight(Activity activity) {
        return getStatusBarHeight(activity.getWindow());
    }

    public static int getStatusBarHeight(Window window) {
        Rect localRect = new Rect();
        window.getDecorView().getWindowVisibleDisplayFrame(localRect);
        int mStatusBarHeight = localRect.top;
        if (0 == mStatusBarHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                mStatusBarHeight = ResourcesUtils.getResources().getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        if (0 == mStatusBarHeight) {
            int resourceId = ResourcesUtils.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                mStatusBarHeight = ResourcesUtils.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return mStatusBarHeight;
    }


}
