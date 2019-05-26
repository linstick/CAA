package com.luoruiyong.caa.common.callback;

/**
 * Author: luoruiyong
 * Date: 2019/4/16/016
 * Description: 权限请求结果回调
 **/
public interface OnPermissionCallback {
    void onGranted();
    void onDenied();
}
