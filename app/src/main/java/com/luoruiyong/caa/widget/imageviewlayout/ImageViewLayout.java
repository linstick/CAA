package com.luoruiyong.caa.widget.imageviewlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ImageViewLayout extends ViewGroup implements View.OnClickListener{

    private final int DEFAULT_MAX_CHILD_COUNT = 5;
    private final ILayoutStrategy DEFAULT_LAYOUT_STRATEGY = new SpecialLayoutStrategy();

    private int mMaxChildViewCount;
    private List<String> mUrls;
    private OnImageClickListener mListener;
    private ILayoutStrategy mLayoutStrategy;

    public ImageViewLayout(Context context) {
        this(context, null);
    }

    public ImageViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        mMaxChildViewCount = DEFAULT_MAX_CHILD_COUNT;
        mLayoutStrategy = DEFAULT_LAYOUT_STRATEGY;
    }

    public void setPictureUrls(List<String> list) {
        mUrls = list;
        notifyChildViewChanged();
    }

    public void setMaxChildViewCount(int count) {
        mMaxChildViewCount = count;
        // 配置发生改变，需要重新布局
        notifyChildViewChanged();
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mListener = listener;
    }

    public void setLayoutStrategy(ILayoutStrategy strategy) {
        mLayoutStrategy = strategy;
        // 配置发生改变，需要重新布局
        notifyChildViewChanged();
    }

    public void notifyChildViewChanged() {
        removeAllViews();
        if (ListUtils.isEmpty(mUrls)) {
            setVisibility(View.GONE);
            return;
        }
        setVisibility(VISIBLE);
        int count = Math.min(mUrls.size(), mMaxChildViewCount);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.child_item_image, this, false);
            ImageView imageView = view.findViewById(R.id.iv_picture);
            imageView.setTag(i);
            imageView.setOnClickListener(this);
            if (i + 1 == count && count < mUrls.size()) {
                TextView totalTipTv = view.findViewById(R.id.tv_total_tip);
                totalTipTv.setText(String.format(ResourcesUtils.getString(R.string.common_str_total), mUrls.size()));
                totalTipTv.setVisibility(VISIBLE);
            }
            addView(view);
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = mLayoutStrategy.measure(this);
        for (int i = 0; i < getChildCount(); i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mLayoutStrategy.layout(this);
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
