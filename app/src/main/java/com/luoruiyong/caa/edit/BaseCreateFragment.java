package com.luoruiyong.caa.edit;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;

/**
 * Author: luoruiyong
 * Date: 2019/4/7/007
 * Description:
 **/
public class BaseCreateFragment extends Fragment {
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
