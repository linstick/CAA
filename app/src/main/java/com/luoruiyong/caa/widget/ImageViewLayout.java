package com.luoruiyong.caa.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.utils.DisplayUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ImageViewLayout extends ViewGroup implements View.OnClickListener{

    private final int CHILD_MARGIN_PX = DisplayUtils.dp2px(2);
    private final int DEFAULT_MAX_CHILD_COUNT = 5;

    private int mMaxCount = DEFAULT_MAX_CHILD_COUNT;
    private List<String> mUrls;
    private OnImageClickListener mListener;

    public ImageViewLayout(Context context) {
        super(context);
    }

    public ImageViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPictureUrls(List<String> list) {
        removeAllViews();
        mUrls = list;
        if (mUrls == null || mUrls.size() == 0) {
            return;
        }
        int count = Math.min(list.size(), mMaxCount);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.child_activity_image, this, false);
            ImageView imageView = view.findViewById(R.id.iv_picture);
            imageView.setTag(i);
            imageView.setOnClickListener(this);
            if (i + 1 == count && count < list.size()) {
                TextView totalTipTv = view.findViewById(R.id.tv_total_tip);
                totalTipTv.setText("total " + list.size());
                totalTipTv.setVisibility(VISIBLE);
            }
            addView(view);
        }
    }

    public void setMaxChildCount(int count) {
        mMaxCount = count;
        for (int i = mMaxCount; i < getChildCount(); i++) {
            removeViewAt(i);
        }
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    private int measureChildWith5(int widgetWidth) {
        int otherWidth = (widgetWidth - CHILD_MARGIN_PX) / 3 * 2;
        int otherHeight = otherWidth / 2;
        int centerWidth = widgetWidth - otherHeight * 2 - CHILD_MARGIN_PX * 2;
        int centerHeight = centerWidth;

        applyLayoutParams(getChildAt(0), otherWidth, otherHeight);
        applyLayoutParams(getChildAt(1), otherHeight, otherWidth);
        applyLayoutParams(getChildAt(2), otherHeight, otherWidth);
        applyLayoutParams(getChildAt(3), centerWidth, centerHeight);
        applyLayoutParams(getChildAt(4), otherWidth, otherHeight);
        return otherWidth + otherHeight + CHILD_MARGIN_PX;
    }

    private int measureChildWith4(int widgetWidth) {
        int width = (widgetWidth - CHILD_MARGIN_PX) / 2;
        int height = width / 8 * 5;

        for (int i = 0; i < getChildCount(); i++) {
            applyLayoutParams(getChildAt(i), width, height);
        }
        return height * 2 + CHILD_MARGIN_PX;
    }

    private int measureChildWith3(int widgetWidth) {
        int firstWidth = (widgetWidth - CHILD_MARGIN_PX) / 3 * 2;
        int otherWidth = firstWidth / 2;
        int firstHeight = firstWidth / 8 * 5;
        int otherHeight = (firstHeight - CHILD_MARGIN_PX) / 2;

        applyLayoutParams(getChildAt(0), firstWidth, firstHeight);
        applyLayoutParams(getChildAt(1), otherWidth, otherHeight);
        applyLayoutParams(getChildAt(2), otherWidth, otherHeight);
        return firstHeight;
    }

    private int measureChildWith2(int widgetWidth) {
        int width = (widgetWidth - CHILD_MARGIN_PX) / 2;
        int height = width / 8 * 5;

        applyLayoutParams(getChildAt(0), width, height);
        applyLayoutParams(getChildAt(1), width, height);
        return height;
    }

    private int measureChildWith1(int widgetWidth) {
        int width = (widgetWidth - CHILD_MARGIN_PX) / 7 * 4;
        int height = width / 8 * 5;
        applyLayoutParams(getChildAt(0), width, height);
        return height;
    }

    private void applyLayoutParams(View view, int width, int height) {
        ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private void layoutChildWith5() {
        int left, top, width, height;
        LayoutParams params = getChildAt(0).getLayoutParams();

        left = 0;
        top = 0;
        width = params.width;
        height = params.height;
        getChildAt(0).layout(left, top, left + width, top + height);

        left = width + CHILD_MARGIN_PX;
        params = getChildAt(1).getLayoutParams();
        width = params.width;
        height = params.height;
        getChildAt(1).layout(left, top, left + width, top + height);

        left = 0;
        top = getChildAt(0).getLayoutParams().height + CHILD_MARGIN_PX;
        params = getChildAt(2).getLayoutParams();
        width = params.width;
        height = params.height;
        getChildAt(2).layout(left, top, left + width, top + height);

        left = width + CHILD_MARGIN_PX;
        params = getChildAt(3).getLayoutParams();
        width = params.width;
        height = params.height;
        getChildAt(3).layout(left, top, left + width, top + height);

        top += height + CHILD_MARGIN_PX;
        params = getChildAt(4).getLayoutParams();
        width = params.width;
        height = params.height;
        getChildAt(4).layout(left, top, left + width, top + height);

    }

    private void layoutChildWith4() {
        int left = 0;
        int top = 0;
        LayoutParams params = getChildAt(0).getLayoutParams();
        int width = params.width;
        int height = params.height;

        getChildAt(0).layout(left, top, left + width, top + height);
        left += width + CHILD_MARGIN_PX;
        getChildAt(1).layout(left, top, left + width, top + height);
        left = 0;
        top += height + CHILD_MARGIN_PX;
        getChildAt(2).layout(left, top, left + width, top + height);
        left += width + CHILD_MARGIN_PX;
        getChildAt(3).layout(left, top, left + width, top + height);
    }

    private void layoutChildWith3() {
        int left = 0;
        int top = 0;
        LayoutParams params = getChildAt(0).getLayoutParams();
        int firstWidth = params.width;
        int firstHeight = params.height;
        getChildAt(0).layout(left, top, left + firstWidth, top + firstHeight);

        left = firstWidth + CHILD_MARGIN_PX;
        params = getChildAt(1).getLayoutParams();
        int otherWidth = params.width;
        int otherHeight = params.height;
        getChildAt(1).layout(left, top, left + otherWidth, top + otherHeight);

        top += otherHeight + CHILD_MARGIN_PX;
        getChildAt(2).layout(left, top, left + otherWidth, top + otherHeight);
    }

    private void layoutChildWith2() {
        int left = 0;
        int top = 0;
        LayoutParams params = getChildAt(0).getLayoutParams();
        int width = params.width;
        int height = params.height;
        getChildAt(0).layout(left, top, left + width, top + height);

        left += width + CHILD_MARGIN_PX;
        getChildAt(1).layout(left, top, left + width, top + height);
    }

    private void layoutChildWith1() {
        LayoutParams params = getChildAt(0).getLayoutParams();
        getChildAt(0).layout(0, 0, params.width, params.height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height;
        switch ( getChildCount()) {
            case 0:
                height = 0;
                break;
            case 1:
                height = measureChildWith1(width);
                break;
            case 2:
                height = measureChildWith2(width);
                break;
            case 3:
                height = measureChildWith3(width);
                break;
            case 4:
                height = measureChildWith4(width);
                break;
            case 5:
            default:
                height = measureChildWith5(width);
                break;
        }
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (getChildCount()) {
            case 0:
                break;
            case 1:
                layoutChildWith1();
                break;
            case 2:
                layoutChildWith2();
                break;
            case 3:
                layoutChildWith3();
                break;
            case 4:
                layoutChildWith4();
                break;
            case 5:
            default:
                layoutChildWith5();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onImageClick((Integer) v.getTag());
        }
    }

    public interface OnImageClickListener {
        void onImageClick(int position);
    }
}
