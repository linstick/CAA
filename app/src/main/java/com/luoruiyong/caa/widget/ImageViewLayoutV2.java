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
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ImageViewLayoutV2 extends ViewGroup implements View.OnClickListener{

    private final int CHILD_MARGIN_PX = DisplayUtils.dp2px(4);
    private final int DEFAULT_MAX_CHILD_COUNT = 5;

    private int mMaxCount = DEFAULT_MAX_CHILD_COUNT;
    private List<String> mUrls;
    private OnImageClickListener mListener;

    public ImageViewLayoutV2(Context context) {
        super(context);
    }

    public ImageViewLayoutV2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewLayoutV2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPictureUrls(List<String> list) {
        removeAllViews();
        mUrls = list;
        if (mUrls == null || mUrls.size() == 0) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(VISIBLE);
        int count = Math.min(list.size(), mMaxCount);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.child_item_image, this, false);
            ImageView imageView = view.findViewById(R.id.iv_picture);
            imageView.setTag(i);
            imageView.setOnClickListener(this);
            if (i + 1 == count && count < list.size()) {
                TextView totalTipTv = view.findViewById(R.id.tv_total_tip);
                totalTipTv.setText(String.format(ResourcesUtils.getString(R.string.common_str_total), list.size()));
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

    private int measureChildWith6(int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        applyAllLayoutParams(size);
        return size * 2 + CHILD_MARGIN_PX;
    }

    private int measureChildWith5(int widgetWidth) {
        int firstWidth = (widgetWidth - CHILD_MARGIN_PX) / 2;
        int secondWidth = (widgetWidth - CHILD_MARGIN_PX * 2) / 3;
        int height = secondWidth;
        applyLayoutParams(getChildAt(0), firstWidth, height);
        applyLayoutParams(getChildAt(1), firstWidth, height);
        applyLayoutParams(getChildAt(2), secondWidth, height);
        applyLayoutParams(getChildAt(3), secondWidth, height);
        applyLayoutParams(getChildAt(4), secondWidth, height);
        return height * 2 + CHILD_MARGIN_PX;
    }

    private int measureChildWith4(int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        applyAllLayoutParams(size);
        return size * 2 + CHILD_MARGIN_PX;
    }

    private int measureChildWith3(int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        applyAllLayoutParams(size);
        return size;
    }

    private int measureChildWith2(int widgetWidth) {
        int size = getCommonSize(widgetWidth);
        applyAllLayoutParams(size);
        return size;
    }

    private int measureChildWith1(int widgetWidth) {
        int size = widgetWidth / 7 * 3;
        applyAllLayoutParams(size);
        return size;
    }

    private int getCommonSize(int widgetWidth) {
        return (widgetWidth - CHILD_MARGIN_PX * 2) / 3;
    }

    private void applyAllLayoutParams(int size) {
        applyAllLayoutParams(size, size);
    }

    private void applyAllLayoutParams(int width, int height) {
        for (int i = 0; i < getChildCount(); i++) {
            applyLayoutParams(getChildAt(i), width, height);
        }
    }

    private void applyLayoutParams(View view, int width, int height) {
        LayoutParams params = new ViewPager.LayoutParams();
        params.width = width;
        params.height = height;
        view.setLayoutParams(params);
    }

    private void layoutChildWith6() {
        int left = 0, top = 0;
        View view = getChildAt(0);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(1);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(2);
        applyLayout(view, left, top);

        left = 0;
        top = view.getBottom() + CHILD_MARGIN_PX;
        view = getChildAt(3);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(4);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(5);
        applyLayout(view, left, top);
    }

    private void layoutChildWith5() {
        int left = 0, top = 0;
        View view = getChildAt(0);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(1);
        applyLayout(view, left, top);

        left = 0;
        top = view.getBottom() + CHILD_MARGIN_PX;
        view = getChildAt(2);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(3);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(4);
        applyLayout(view, left, top);
    }

    private void layoutChildWith4() {
        int left = 0, top = 0, height;
        View view = getChildAt(0);
        applyLayout(view, left, top);

        height = view.getBottom();

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(1);
        applyLayout(view, left, top);

        left = 0;
        top = height + CHILD_MARGIN_PX;
        view = getChildAt(2);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(3);
        applyLayout(view, left, top);
    }

    private void layoutChildWith3() {
        int left = 0, top = 0;
        View view = getChildAt(0);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(1);
        applyLayout(view, left, top);

        left = view.getRight() + CHILD_MARGIN_PX;
        view = getChildAt(2);
        applyLayout(view, left, top);
    }

    private void layoutChildWith2() {
        View view = getChildAt(0);
        applyLayout(view, 0, 0);

        applyLayout(getChildAt(1), view.getRight() + CHILD_MARGIN_PX, 0);
    }

    private void layoutChildWith1() {
        applyLayout(getChildAt(0), 0, 0);
    }

    private void applyLayout(View view, int left, int top) {
        LayoutParams params = view.getLayoutParams();
        view.layout(left, top, left + params.width, top + params.height);
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
                height = measureChildWith5(width);
                break;
            case 6:
            default:
                height = measureChildWith6(width);
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
                layoutChildWith5();
                break;
            case 6:
            default:
                layoutChildWith6();
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onImageClick(this, (Integer) v.getTag());
        }
    }

    public interface OnImageClickListener {
        void onImageClick(View parent, int position);
    }
}
