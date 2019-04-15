package com.luoruiyong.caa.base;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewStub;

import com.luoruiyong.caa.widget.TipView;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class BaseFragment extends Fragment {

    protected ViewStub mErrorTipViewStub;
    protected TipView mTipView;
    private boolean mRefreshNeedHide;

    protected void setUpErrorViewStub(ViewStub viewStub) {
        mErrorTipViewStub = viewStub;
    }

    protected void setUpErrorView(TipView view) {
        mTipView = view;
        mTipView.setRefreshNeedHide(mRefreshNeedHide);
        mTipView.setOnRefreshCallback(new TipView.OnRefreshClickCallBack() {
            @Override
            public void onRefreshClick() {
                BaseFragment.this.onRefreshClick();
            }
        });
    }

    protected void setRefreshNeedHide(boolean needHide) {
       if (mTipView != null) {
           mTipView.setRefreshNeedHide(needHide);
       } else {
           mRefreshNeedHide = needHide;
       }
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
        if (mErrorTipViewStub == null) {
            return;
        }
        if (mTipView == null) {
            mTipView = (TipView) mErrorTipViewStub.inflate();
            mTipView.setRefreshNeedHide(mRefreshNeedHide);
            mTipView.setOnRefreshCallback(new TipView.OnRefreshClickCallBack() {
                @Override
                public void onRefreshClick() {
                    BaseFragment.this.onRefreshClick();
                }
            });
        }
    }

    protected void doRefresh() {

    }

    protected void onRefreshClick() {

    }
}
