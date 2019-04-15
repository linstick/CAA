package com.luoruiyong.caa.widget.imageviewlayout.layout;

import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.caa.utils.DisplayUtils;

/**
 * Author: luoruiyong
 * Date: 2019/4/5/005
 **/
public interface ILayoutStrategy {
    int DEFAULT_CHILD_VIEW_MARGIN_PX = DisplayUtils.dp2px(5);

    void setChildViewMarginPx(int pxValue);

    int measure(ViewGroup container);

    void layout(ViewGroup container);
}
