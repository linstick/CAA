package com.luoruiyong.caa.edit;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.utils.DialogHelper;

import java.security.Permission;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Author: luoruiyong
 * Date: 2019/4/7/007
 * Description:
 **/
public class BaseCreateFragment extends BaseFragment {

    protected static final int REQUEST_CHOOSE_PICTURE_CODE = 6;
    protected static final int REQUEST_CHOOSE_COVER_CODE = 7;

    private Dialog mLoadingDialog;

    protected void showLoadingDialog(@StringRes int resId) {
        showLoadingDialog(getString(resId));
    }

    protected void showLoadingDialog(String loadingTip) {
        showLoadingDialog(loadingTip, false, null);
    }

    protected void showLoadingDialog(String loadingTip, boolean cancelable, CommonDialog.Builder.OnClickListener cancelListener) {
        mLoadingDialog = DialogHelper.showLoadingDialog(getContext(), loadingTip, cancelable, cancelListener);
    }

    protected void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }


    protected void showConfirmLeftDialog() {
        new CommonDialog.Builder(getContext())
                .type(CommonDialog.TYPE_NORMAL)
                .title(getString(R.string.common_str_tip))
                .message(getString(R.string.common_tip_no_save))
                .positive(getString(R.string.common_str_left))
                .negative(getString(R.string.common_str_cancel))
                .onPositive(new CommonDialog.Builder.OnClickListener() {
                    @Override
                    public void onClick(String extras) {
                        finish();
                    }
                })
                .build()
                .show();
    }

    protected void finish() {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }
}
