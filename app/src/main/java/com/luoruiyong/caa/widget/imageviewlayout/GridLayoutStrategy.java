package com.luoruiyong.caa.widget.imageviewlayout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Author: luoruiyong
 * Date: 2019/4/5/005
 * Description: All the children views are with the same size.
 **/
public class GridLayoutStrategy implements IGridLayoutStrategy {

    private int mChildViewMarginPx = DEFAULT_CHILD_VIEW_MARGIN_PX;
    private int mColumn = DEFAULT_GRID_COLUMN;

    public GridLayoutStrategy() {
    }

    public GridLayoutStrategy(int column) {
        this.mColumn = column;
    }

    @Override
    public void setColumn(int column) {
        this.mColumn = column;
    }

    @Override
    public void setChildViewMarginPx(int pxValue) {
        mChildViewMarginPx = Math.min(0, pxValue);
    }

    @Override
    public int measure(ViewGroup container) {
        int childCount = container.getChildCount();
        int widgetWidth = container.getMeasuredWidth();
        int size = (widgetWidth - mChildViewMarginPx * (mColumn - 1)) / mColumn;
        LayoutUtils.applyLayoutParamsToAllChildView(container, size);
        int floor = (childCount - 1) / mColumn + 1;
        return size * floor + mChildViewMarginPx * (floor - 1);
    }

    @Override
    public void layout(ViewGroup container) {
        int count = container.getChildCount();
        int left = 0, top = 0;
        View view;
        for (int i = 0; i < count; i++) {
            view = container.getChildAt(i);
            LayoutUtils.applyLayout(view, left, top);
            if ((i + 1) % mColumn == 0) {
                left = 0;
                top = view.getBottom() + mChildViewMarginPx;
            } else {
                left = view.getRight() + mChildViewMarginPx;
            }
        }
    }
}
