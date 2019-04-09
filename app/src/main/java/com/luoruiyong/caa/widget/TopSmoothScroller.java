package com.luoruiyong.caa.widget;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class TopSmoothScroller extends LinearSmoothScroller {
    public TopSmoothScroller(Context context) {
        super(context);
    }

    @Override
    protected int getHorizontalSnapPreference() {
        return SNAP_TO_START;
    }

    @Override
    protected int getVerticalSnapPreference() {
        return SNAP_TO_START;
    }
}
