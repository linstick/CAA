package com.luoruiyong.caa.widget.imageviewlayout.clickmanager;

import com.luoruiyong.caa.widget.dynamicinputview.DynamicInputView;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

/**
 * Author: luoruiyong
 * Date: 2019/4/7/007
 * Description:
 **/
public interface IClickManager {
    boolean onImageClick(ImageViewLayout view, int position);
    void onImageLongClick(DynamicInputView wrapper, ImageViewLayout view, int position);
}
