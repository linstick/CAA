package com.luoruiyong.caa.edit;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.utils.DialogHelper;

import java.security.Permission;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Author: luoruiyong
 * Date: 2019/4/7/007
 * Description:
 **/
public class BaseCreateFragment extends Fragment {

    private static final int REQUEST_STORAGE_PERMISSION_CODE = 1;
    private static final int REQUEST_LOCATION_PERMISSION_CODE = 2;
    protected static final int REQUEST_CHOOSE_PICTURE_CODE = 6;
    protected static final int REQUEST_CHOOSE_COVER_CODE = 7;

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

    protected void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            onStoragePermissionGranted();
            return;
        }
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION_CODE);
    }

    protected void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            onLocationPermissionGranted();
            return;
        }
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION_CODE);
    }

    protected void onStoragePermissionGranted() {

    }


    protected void onLocationPermissionGranted() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStoragePermissionGranted();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // 拒绝
                        showPermissionDescriptionDialog(getString(R.string.common_dialog_tip_permission_storage));
                    } else  {
                        // 拒绝并勾选不再询问
                        showSettingGuideDialog(getString(R.string.common_dialog_str_storage_permission));
                    }
                }
                break;
            case REQUEST_LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onLocationPermissionGranted();
                } else {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        // 拒绝
                        showPermissionDescriptionDialog(getString(R.string.common_dialog_tip_permission_location));
                    } else  {
                        // 拒绝并勾选不再询问
                        showSettingGuideDialog(getString(R.string.common_dialog_str_location_permission));
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
}
