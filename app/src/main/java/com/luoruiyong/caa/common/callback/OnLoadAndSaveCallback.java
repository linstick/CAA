package com.luoruiyong.caa.common.callback;

import android.graphics.Bitmap;

/**
 * Author: luoruiyong
 * Date: 2019/5/26/026
 * Description: 图片下载并保存结果回调
 **/
public interface OnLoadAndSaveCallback {
    void onSuccess(Bitmap bitmap, String path);
    void onFail(String error);
}
