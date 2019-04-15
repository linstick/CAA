package com.luoruiyong.caa.widget.imageviewlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ImageBean;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.layout.GridLayoutStrategy;
import com.luoruiyong.caa.widget.imageviewlayout.layout.ILayoutStrategy;
import com.luoruiyong.caa.widget.imageviewlayout.layout.SpecialLayoutStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ImageViewLayout extends ViewGroup implements View.OnClickListener, View.OnLongClickListener{
    private final boolean DEFAULT_NEED_SHOW_TOTAL_TIP = true;
    private final boolean DEFAULT_SUPPORT_ALL_CHILD_DELETE = false;
    private final boolean DEFAULT_SUPPORT_ALL_CHILD_DELETE_BESIDES_LAST = false;
    private final int DEFAULT_MAX_CHILD_COUNT = 5;
    private final int DEFAULT_LAYOUT_STRATEGY = LayoutStrategy.SPECIAL;

    private int mMaxChildViewCount;
    private List<ImageBean> mList;
    private OnImageClickListener mClickListener;
    private OnImageLongClickListener mLongClickListener;
    private OnImageDeletedListener mDeleteListener;
    private ILayoutStrategy mLayoutStrategy;
    private boolean mNeedShowTotalTip;
    private boolean mSupportAllChildDelete;
    private boolean mSupportAllChildDeleteBesidesLast;

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
        mSupportAllChildDelete = typedArray.getBoolean(
                R.styleable.ImageViewLayout_supportAllChildDelete,
                DEFAULT_SUPPORT_ALL_CHILD_DELETE);
        mSupportAllChildDeleteBesidesLast = typedArray.getBoolean(
                R.styleable.ImageViewLayout_supportAllChildDeleteBesidesLast,
                DEFAULT_SUPPORT_ALL_CHILD_DELETE_BESIDES_LAST);
        int urlsResId = typedArray.getResourceId(R.styleable.ImageViewLayout_imageUrls, -1);
        if (urlsResId != -1) {
            setPictureUrls(Arrays.asList(ResourcesUtils.getStringArray(urlsResId)));
        }
        int layoutStrategy = typedArray.getInt(R.styleable.ImageViewLayout_layoutStrategy, DEFAULT_LAYOUT_STRATEGY);
        setLayoutStrategy(layoutStrategy);
        typedArray.recycle();
    }

    public void setPictureDataList(List<ImageBean> list) {
        mList = list;
        notifyChildViewChanged();
    }

    public void setPictureUrls(List<String> list) {
        mList = new ArrayList<>();
        if (list != null) {
            for (String url : list) {
                mList.add(new ImageBean(url));
            }
        }
        notifyChildViewChanged();
    }

    public List<String> getPictureUrls() {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < ListUtils.getSize(mList); i++) {
            ImageBean data = mList.get(i);
            if (data.getType() == ImageBean.TYPE_RESOURCE_ID) {
                result.add(String.valueOf(data.getResId()));
            } else if (data.getType() == ImageBean.TYPE_LOCAL_FILE) {
                result.add(data.getPath());
            } else if (data.getType() == ImageBean.TYPE_REMOTE_FILE) {
                result.add(data.getUrl());
            } else {
                result.add("");
            }
        }
        return result;
    }

    public void setMaxChildViewCount(int count) {
        mMaxChildViewCount = count;
    }

    public void setSupportAllChildDelete(boolean supportAllChildDelete) {
        this.mSupportAllChildDelete = supportAllChildDelete;
    }

    public void setSupportAllChildDeleteBesidesLast(boolean supportAllChildDeleteBesidesLast) {
        this.mSupportAllChildDeleteBesidesLast = supportAllChildDeleteBesidesLast;
    }

    public void setOnImageClickListener(OnImageClickListener listener) {
        mClickListener = listener;
    }

    public void setOnImageLongClickListener(OnImageLongClickListener listener) {
        mLongClickListener = listener;
    }

    public void setOnImageDeletedListener(OnImageDeletedListener listener) {
        mDeleteListener = listener;
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
        if (ListUtils.isEmpty(mList)) {
            return;
        }
        int count = Math.min(mList.size(), mMaxChildViewCount);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.child_item_image, this, false);
            SimpleDraweeView imageView = view.findViewById(R.id.iv_picture);
            imageView.setTag(i);
            imageView.setOnClickListener(this);

            ImageBean data = mList.get(i);
            if (data.getType() == ImageBean.TYPE_RESOURCE_ID) {
                imageView.setImageResource(data.getResId());
            } else if (data.getType() == ImageBean.TYPE_LOCAL_FILE) {
                imageView.setImageURI(Uri.fromFile(new File(data.getPath())));
            } else if (data.getType() == ImageBean.TYPE_REMOTE_FILE) {
                imageView.setImageURI(Uri.parse(data.getUrl()));
            }

            if (mLongClickListener != null) {
                imageView.setOnLongClickListener(this);
            }

            // 删除功能
            if (mSupportAllChildDelete || (mSupportAllChildDeleteBesidesLast && i + 1 != mList.size())) {
                ImageView deleteIv = view.findViewById(R.id.iv_delete);
                deleteIv.setVisibility(VISIBLE);
                deleteIv.setTag(i);
                deleteIv.setOnClickListener(this);
            }

            // 总数展示
            if (mNeedShowTotalTip && i + 1 == count && count < mList.size()) {
                TextView totalTipTv = view.findViewById(R.id.tv_total_tip);
                totalTipTv.setText(String.format(ResourcesUtils.getString(R.string.common_str_total), mList.size()));
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
        int position = (int) v.getTag();
        switch (v.getId()) {
            case R.id.iv_picture:
                if (mClickListener != null) {
                    mClickListener.onImageClick(this, position);
                }
                break;
            case R.id.iv_delete:
                if (ListUtils.isIndexBetween(mList, position)) {
                    mList.remove(position);
                    notifyChildViewChanged();
                    if (mDeleteListener != null) {
                        mDeleteListener.onItemDeleted(this, position);
                    }
                }
                break;
            default:
                break;
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

    public interface OnImageDeletedListener {
        void onItemDeleted(View parent, int position);
    }

    public class LayoutStrategy {
        public final static int GRID = 0;
        public final static int SPECIAL = 1;
    }
}
