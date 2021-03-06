package com.luoruiyong.caa.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class DialogHelper {

    public static void showConfirmDialog(Context context, String message) {
        showConfirmDialog(context, message, context.getString(R.string.common_str_confirm));
    }

    public static void showConfirmDialog(Context context, String message, String positive) {
        showConfirmDialog(context, context.getString(R.string.common_dialog_str_tips_title), message, positive);
    }

    public static void showConfirmDialog(Context context, String title, String message, String positive) {
        showNormalDialog(context, title, message, positive, null, null);
    }

    public static void showNormalDialog(Context context, String message, CommonDialog.Builder.OnClickListener mPositiveListener) {
        showNormalDialog(context, context.getString(R.string.common_dialog_str_tips_title), message, context.getString(R.string.common_str_confirm), context.getString(R.string.common_str_cancel), mPositiveListener);
    }

    public static void showNormalDialog(Context context, String title, String message, String positive, String negative, CommonDialog.Builder.OnClickListener mPositiveListener) {
        new CommonDialog.Builder(context)
                .type(CommonDialog.TYPE_NORMAL)
                .title(title)
                .message(message)
                .positive(positive)
                .onPositive(mPositiveListener)
                .negative(negative)
                .build()
                .show();
    }

    public static void showInputDialog(Context context,  CommonDialog.Builder.OnClickListener mPositiveListener) {
        showInputDialog(context, context.getString(R.string.common_dialog_str_input_title), mPositiveListener);
    }

    public static void showInputDialog(Context context, String title, CommonDialog.Builder.OnClickListener mPositiveListener) {
        showInputDialog(context, title, "", mPositiveListener);
    }

    public static void showInputDialog(Context context, String title, String inputHint, CommonDialog.Builder.OnClickListener mPositiveListener) {
        showInputDialog(context, title, inputHint, InputType.TYPE_CLASS_TEXT, mPositiveListener);
    }

    public static void showInputDialog(Context context, String title, String inputHint, int inputType, CommonDialog.Builder.OnClickListener mPositiveListener) {
        showInputDialog(context, title, inputHint, inputType, context.getString(R.string.common_str_confirm), context.getString(R.string.common_str_cancel), mPositiveListener);
    }

    public static void showInputDialog(Context context, String title, String inputHint, int inputType, String positive, String negative, CommonDialog.Builder.OnClickListener mPositiveListener) {
        new CommonDialog.Builder(context)
                .type(CommonDialog.TYPE_INPUT)
                .title(title)
                .inputHint(inputHint)
                .inputType(inputType)
                .positive(positive)
                .onPositive(mPositiveListener)
                .negative(negative)
                .build()
                .show();
    }

    public static void showListDialog(Context context, String singleItem, CommonDialog.Builder.OnItemClickListener listener) {
        List<String> items = new ArrayList<>();
        items.add(singleItem);
        showListDialog(context, items, listener);
    }

    public static void showListDialog(Context context, List<String> items, CommonDialog.Builder.OnItemClickListener mItemListener) {
        showListDialog(context, null, items, mItemListener);
    }

    public static void showListDialog(Context context, String[] items, CommonDialog.Builder.OnItemClickListener mItemListener) {
        showListDialog(context, null, Arrays.asList(items), mItemListener);
    }

    public static void showListDialog(Context context, String title, List<String> items, CommonDialog.Builder.OnItemClickListener mItemListener) {
        new CommonDialog.Builder(context)
                .type(CommonDialog.TYPE_LIST)
                .title(title)
                .items(items)
                .onItem(mItemListener)
                .build()
                .show();
    }

    public static void showLoadingDialog(Context context, String tip) {
        showLoadingDialog(context, tip, true);
    }

    public static Dialog showLoadingDialog(Context context, String tip, boolean cancelable) {
        return showLoadingDialog(context, tip, cancelable, null);
    }

    public static Dialog showLoadingDialog(Context context, String tip, boolean cancelable, CommonDialog.Builder.OnClickListener cancelCallback) {
        Dialog dialog = new CommonDialog.Builder(context)
                .type(CommonDialog.TYPE_LOADING)
                .loadingTip(tip)
                .cancelable(cancelable)
                .negative(cancelCallback == null ? null : ResourcesUtils.getString(R.string.common_str_cancel))
                .onNegative(cancelCallback)
                .build();
        dialog.show();
        return dialog;
    }
}
