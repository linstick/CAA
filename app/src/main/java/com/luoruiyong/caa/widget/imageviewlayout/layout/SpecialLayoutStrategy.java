package com.luoruiyong.caa.widget.imageviewlayout.layout;

import android.view.View;
import android.view.ViewGroup;

/**
 * Author: luoruiyong
 * Date: 2019/4/5/005
 **/
public class SpecialLayoutStrategy implements ILayoutStrategy {

    private int mChildViewMarginPx = DEFAULT_CHILD_VIEW_MARGIN_PX;

    @Override
    public void setChildViewMarginPx(int pxValue) {
        mChildViewMarginPx = Math.min(0, pxValue);
    }

    private int measureWith7Or8Or9(ViewGroup container, int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        LayoutUtils.applyLayoutParamsToAllChildView(container, size);
        return size * 3 + mChildViewMarginPx * 2;
    }

    private int measureWith4Or6(ViewGroup container, int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        LayoutUtils.applyLayoutParamsToAllChildView(container, size);
        return size * 2 + mChildViewMarginPx;
    }

    private int measureWith2Or3(ViewGroup container,int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        LayoutUtils.applyLayoutParamsToAllChildView(container, size);
        return size;
    }

    private int measureWith5(ViewGroup container, int widgetWidth) {
        int firstWidth = (widgetWidth - mChildViewMarginPx) / 2;
        int secondWidth = (widgetWidth - mChildViewMarginPx * 2) / 3;
        int height = secondWidth;
        LayoutUtils.applyLayoutParams(container.getChildAt(0), firstWidth, height);
        LayoutUtils.applyLayoutParams(container.getChildAt(1), firstWidth, height);
        LayoutUtils.applyLayoutParams(container.getChildAt(2), secondWidth, height);
        LayoutUtils.applyLayoutParams(container.getChildAt(3), secondWidth, height);
        LayoutUtils.applyLayoutParams(container.getChildAt(4), secondWidth, height);
        return height * 2 + mChildViewMarginPx;
    }

    private int measureWith1(ViewGroup container, int widgetWidth) {
        int size = widgetWidth / 7 * 3;
        LayoutUtils.applyLayoutParams(container.getChildAt(0), size);
        return size;
    }

    private int getCommonSize(int widgetWidth) {
        return (widgetWidth - mChildViewMarginPx * 2) / 3;
    }

    private void layoutChildWith5(ViewGroup container) {
        int left = 0, top = 0;
        View view = container.getChildAt(0);
        LayoutUtils.applyLayout(view, left, top);

        left = view.getRight() + mChildViewMarginPx;
        view = container.getChildAt(1);
        LayoutUtils.applyLayout(view, left, top);

        left = 0;
        top = view.getBottom() + mChildViewMarginPx;
        view = container.getChildAt(2);
        LayoutUtils.applyLayout(view, left, top);

        left = view.getRight() + mChildViewMarginPx;
        view = container.getChildAt(3);
        LayoutUtils.applyLayout(view, left, top);

        left = view.getRight() + mChildViewMarginPx;
        view = container.getChildAt(4);
        LayoutUtils.applyLayout(view, left, top);
    }

    private void layoutChildByColumnCount(ViewGroup container, int columnCount) {
        if (columnCount <= 0) {
            return;
        }
        int count = container.getChildCount();
        int left = 0, top = 0;
        View view;
        for (int i = 0; i < count; i++) {
            view = container.getChildAt(i);
            LayoutUtils.applyLayout(view, left, top);
            if ((i + 1) % columnCount == 0) {
                left = 0;
                top = view.getBottom() + mChildViewMarginPx;
            } else {
                left = view.getRight() + mChildViewMarginPx;
            }
        }
    }

    @Override
    public int measure(ViewGroup container) {
        int width = container.getMeasuredWidth();
        int height;
        switch (container.getChildCount()) {
            case 0:
                height = 0;
                break;
            case 1:
                height = measureWith1(container, width);
                break;
            case 2:
            case 3:
                height = measureWith2Or3(container, width);
                break;
            case 4:
            case 6:
                height = measureWith4Or6(container, width);
                break;
            case 5:
                height = measureWith5(container, width);
                break;
            case 7:
            case 8:
            case 9:
            default:
                height = measureWith7Or8Or9(container, width);
                break;
        }
        return height;
    }

    @Override
    public void layout(ViewGroup container) {
        switch (container.getChildCount()) {
            case 0:
                break;
            case 1:
            case 2:
            case 4:
                layoutChildByColumnCount(container, 2);
                break;
            case 5:
                layoutChildWith5(container);
                break;
            case 3:
            case 6:
            case 7:
            case 8:
            case 9:
            default:
                layoutChildByColumnCount(container, 3);
                break;
        }
    }
}
