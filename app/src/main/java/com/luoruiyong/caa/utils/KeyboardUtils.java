package com.luoruiyong.caa.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Author: luoruiyong
 * Date: 2019/4/6/006
 * Description:
 **/
public class KeyboardUtils {

    public static void showKeyboard(View targetView) {
        if (targetView == null || targetView.getContext() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) targetView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            targetView.requestFocus();
            imm.showSoftInput(targetView, 0);
        }
    }

    public static void hideKeyboard(View view) {
        if (view == null || view.getContext() == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
