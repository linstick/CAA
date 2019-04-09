package com.luoruiyong.caa.base;

import android.support.v4.app.Fragment;
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
                    doRefresh();
                }
            });
        }
    }

    protected void doRefresh() {

    }
}
