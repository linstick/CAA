package com.luoruiyong.caa.base;

import android.support.v7.app.AppCompatActivity;
import android.view.ViewStub;

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
        initErrorViewIfNeed();
        mTipView.showError(info);
    }

    protected void showErrorView(int imageResId, String info) {
        initErrorViewIfNeed();
        mTipView.showError(imageResId, info);
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
                    doRefreshClick();
                }
            });
        }
    }

    protected void doRefreshClick() {

    }
}
