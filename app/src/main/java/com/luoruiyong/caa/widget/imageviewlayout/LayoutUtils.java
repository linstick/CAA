package com.luoruiyong.caa.widget.imageviewlayout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Author: luoruiyong
 * Date: 2019/4/5/005
 **/
public class LayoutUtils {

    public static void applyLayoutParams(View view, int size) {
        applyLayoutParams(view, size, size);
    }

    public static void applyLayoutParamsToAllChildView(ViewGroup container, int size) {
        applyLayoutParamsToAllChildView(container, size, size);
    }

    public static void applyLayoutParamsToAllChildView(ViewGroup container, int width, int height) {
        int count = container.getChildCount();
        for (int i = 0; i < count; i++) {
            applyLayoutParams(container.getChildAt(i), width, height);
        }
    }

    public static void applyLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(params);
    }

    public static void applyLayout(View view, int left, int top) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        view.layout(left, top, left + params.width, top + params.height);
    }
}
