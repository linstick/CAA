package com.luoruiyong.caa.widget.imageviewlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.layout.GridLayoutStrategy;
import com.luoruiyong.caa.widget.imageviewlayout.layout.ILayoutStrategy;
import com.luoruiyong.caa.widget.imageviewlayout.layout.SpecialLayoutStrategy;

import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ImageViewLayout extends ViewGroup implements View.OnClickListener, View.OnLongClickListener{

    private final boolean DEFAULT_NEED_SHOW_TOTAL_TIP = true;
    private final int DEFAULT_MAX_CHILD_COUNT = 5;
    private final int DEFAULT_LAYOUT_STRATEGY = LayoutStrategy.SPECIAL;

    private int mMaxChildViewCount;
    private List<String> mUrls;
    private OnImageClickListener mClickListener;
    private OnImageLongClickListener mLongClickListener;
    private ILayoutStrategy mLayoutStrategy;
    private boolean mNeedShowTotalTip;

    public ImageViewLayout(Context context) {
        this(context, null);
    }

    public ImageViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet atts) {
        TypedArray typedArray = context.obtainStyledAttributes(atts, R.styleable.ImageViewLayout);
        mMaxChildViewCount = typedArray.getInt(R.styleable.ImageViewLayout_maxChildCount, DEFAULT_MAX_CHILD_COUNT);
        mNeedShowTotalTip = typedArray.getBoolean(R.styleable.ImageViewLayout_needShowTotalTip, DEFAULT_NEED_SHOW_TOTAL_TIP);
        int urlsResId = typedArray.getResourceId(R.styleable.ImageViewLayout_imageUrls, -1);
        if (urlsResId != -1) {
            mUrls = Arrays.asList(ResourcesUtils.getStringArray(urlsResId));
            notifyChildViewChanged();
        }
        int layoutStrategy = typedArray.getInt(R.styleable.ImageViewLayout_layoutStrategy, DEFAULT_LAYOUT_STRATEGY);
        setLayoutStrategy(layoutStrategy);
        typedArray.recycle();
    }

    public void setPictureUrls(List<String> list) {
        mUrls = list;
        notifyChildViewChanged();
    }

    public List<String> getPictureUrls() {
        return mUrls;
    }

    public void deleteImage(int position) {
        if (!ListUtils.isIndexBetween(mUrls, position)) {
            return;
        }
        mUrls.remove(position);
        notifyChildViewChanged();
    }

    public void setMaxChildViewCount(int count) {
        mMaxChildViewCount = count;
        // 配置发生改变，需要重新布局
        notifyChildViewChanged();
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mClickListener = listener;
    }

    public void setOnImageLongClickListener(OnImageLongClickListener listener) {
        mLongClickListener = listener;
    }

    public void setLayoutStrategy(ILayoutStrategy strategy) {
        mLayoutStrategy = strategy;
        // 配置发生改变，需要重新布局
        notifyChildViewChanged();
    }

    public void setLayoutStrategy(int layoutStrategy) {
        switch (layoutStrategy) {
            case LayoutStrategy.GRID:
                mLayoutStrategy = new GridLayoutStrategy();
                break;
            case LayoutStrategy.SPECIAL:
            default:
                mLayoutStrategy = new SpecialLayoutStrategy();
                break;

        }
    }

    public void setNeedShowTotalTip(boolean showTotalTip) {
        mNeedShowTotalTip = showTotalTip;
        notifyChildViewChanged();
    }

    public void notifyChildViewChanged() {
        removeAllViews();
        if (ListUtils.isEmpty(mUrls)) {
            return;
        }
        int count = Math.min(mUrls.size(), mMaxChildViewCount);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.child_item_image, this, false);
            ImageView imageView = view.findViewById(R.id.iv_picture);
            imageView.setTag(i);
            imageView.setOnClickListener(this);
            if (mLongClickListener != null) {
                imageView.setOnLongClickListener(this);
            }
            if (mNeedShowTotalTip && i + 1 == count && count < mUrls.size()) {
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
        if (mClickListener != null) {
            mClickListener.onImageClick(this, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClickListener != null) {
            mLongClickListener.onImageLongClick(this, (Integer) v.getTag());
        }
        return true;
    }

    public interface OnImageClickListener {
        void onImageClick(View parent, int position);
    }

    public interface OnImageLongClickListener {
        void onImageLongClick(View parent, int position);
    }

    public class LayoutStrategy {
        public final static int GRID = 0;
        public final static int SPECIAL = 1;
    }
}
