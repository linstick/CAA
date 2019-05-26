package com.luoruiyong.caa.base;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewStub;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.widget.TipView;

public class BaseActivity extends AppCompatActivity {

    protected ViewStub mErrorTipViewStub;
    protected TipView mTipView;

    protected void setUpErrorViewStub(ViewStub viewStub) {
        mErrorTipViewStub = viewStub;
    }

    protected void showErrorView() {
        initErrorViewIfNeed();
        mTipView.showError();
    }

    protected void showErrorView(String info) {
        showErrorView(info, getString(R.string.common_str_refresh));
    }

    protected void showErrorView(String info, String refreshText) {
        showErrorView(R.drawable.bg_load_fail, info, refreshText);
    }

    protected void showErrorView(int imageResId, String info) {
        showErrorView(imageResId, info, getString(R.string.common_str_refresh));
    }

    protected void showErrorView(int imageResId, String info, String refreshText) {
        initErrorViewIfNeed();
        mTipView.showError(imageResId, info, refreshText);
    }

    protected void showLoadingView() {
        initErrorViewIfNeed();
        mTipView.showProgressBar();
    }

    protected void hideTipView() {
        if (mTipView != null) {
            mTipView.hide();
        }
    }

    private void initErrorViewIfNeed() {
        if (mTipView == null) {
            mTipView = (TipView) mErrorTipViewStub.inflate();
            mTipView.setOnRefreshCallback(new TipView.OnRefreshClickCallBack() {
                @Override
                public void onRefreshClick() {
                    BaseActivity.this.onRefreshClick();
                }
            });
        }
    }

    protected void onRefreshClick() {
        mTipView.showProgressBar();
    }

    protected void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void toast(@StringRes int resId) {
        toast(getString(resId));
    }

    protected void longToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    protected void longToast(@StringRes int resId) {
        longToast(getString(resId));
    }
}
