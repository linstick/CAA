package com.luoruiyong.caa.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewStub;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.widget.TipView;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class BaseFragment extends Fragment {

    private static final int REQUEST_STORAGE_PERMISSION_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 2;

    private OnPermissionCallback mStoragePermissionCallback;
    private OnPermissionCallback mLocationPermissionCallback;

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

    protected void requestStoragePermission(OnPermissionCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            if (callback != null ) {
                callback.onGranted();
            }
            return;
        }
        mStoragePermissionCallback = callback;
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_CODE);
    }

    protected void requestLocationPermission(OnPermissionCallback callback) {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (callback != null) {
                callback.onGranted();
            }
            return;
        }
        mLocationPermissionCallback = callback;
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mStoragePermissionCallback != null) {
                        mStoragePermissionCallback.onGranted();
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 拒绝
                        showPermissionDescriptionDialog(getString(R.string.common_dialog_tip_permission_storage));
                    } else  {
                        // 拒绝并勾选不再询问
                        showSettingGuideDialog(getString(R.string.common_dialog_str_storage_permission));
                    }
                    if (mStoragePermissionCallback != null) {
                        mStoragePermissionCallback.onDenied();
                    }
                }
                break;
            case REQUEST_LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(mLocationPermissionCallback != null) {
                        mLocationPermissionCallback.onGranted();
                    }
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        // 拒绝
                        showPermissionDescriptionDialog(getString(R.string.common_dialog_tip_permission_location));
                    } else  {
                        // 拒绝并勾选不再询问
                        showSettingGuideDialog(getString(R.string.common_dialog_str_location_permission));
                    }
                    if (mLocationPermissionCallback != null) {
                        mLocationPermissionCallback.onDenied();
                    }
                }
                break;
        }
    }

    private void showPermissionDescriptionDialog(String text) {
        new CommonDialog.Builder(getActivity())
                .title(getString(R.string.common_dialog_tip_permission_description))
                .message(text)
                .positive(getString(R.string.common_dialog_tip_go_to_setting))
                .negative(getString(R.string.common_str_cancel))
                .onPositive(new CommonDialog.Builder.OnClickListener() {
                    @Override
                    public void onClick(String extras) {
                        gotoSettingPage();
                    }
                })
                .build()
                .show();
    }

    private void showSettingGuideDialog(String name) {
        new CommonDialog.Builder(getActivity())
                .title(getString(R.string.common_dialog_str_tips_title))
                .message(String.format(getString(R.string.common_dialog_tip_setting_guide), name))
                .positive(getString(R.string.common_dialog_tip_go_to_setting))
                .negative(getString(R.string.common_str_cancel))
                .onPositive(new CommonDialog.Builder.OnClickListener() {
                    @Override
                    public void onClick(String extras) {
                        gotoSettingPage();
                    }
                })
                .build()
                .show();
    }

    private void gotoSettingPage() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    protected void onRefreshClick() {

    }

    protected void doRefresh() {

    }
}
