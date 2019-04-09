package com.luoruiyong.caa.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;

import com.luoruiyong.caa.widget.ErrorTipView;

public class BaseActivity extends AppCompatActivity {

    protected ViewStub mErrorTipViewStub;
    protected ErrorTipView mErrorTipView;

    protected void setUpErrorViewStub(ViewStub viewStub) {
        mErrorTipViewStub = viewStub;
    }

    protected void showErrorView() {
        initErrorViewIfNeed();
        mErrorTipView.setVisibility(View.VISIBLE);
    }

    protected void showErrorView(String info) {
        showErrorView(-1, info);
    }

    protected void showErrorView(int imageResId, String info) {
        initErrorViewIfNeed();
        if (imageResId != -1) {
            mErrorTipView.setErrorImage(imageResId);
        }
        mErrorTipView.setErrorInfo(info);
        mErrorTipView.setVisibility(View.VISIBLE);
    }

    protected void hideErrorView() {
        if (mErrorTipView != null) {
            mErrorTipView.setVisibility(View.GONE);
        }
    }

    private void initErrorViewIfNeed() {
        if (mErrorTipView == null) {
            mErrorTipView = (ErrorTipView) mErrorTipViewStub.inflate();
            mErrorTipView.setOnRefreshCallback(new ErrorTipView.OnRefreshClickCallBack() {
                @Override
                public void onRefreshClick() {
                    doRefresh();
                }
            });
        }
    }

    protected void doRefresh() {

    }
}
