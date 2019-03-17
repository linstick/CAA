package com.luoruiyong.caa.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/17/017
 **/
public class TagInnerItemContainer extends LinearLayout implements View.OnClickListener{

    private final int DEFAULT_MAX_CHILDREN_COUNT = 3;

    private List<String> mItems;
    private int mMaxChildrenCount = DEFAULT_MAX_CHILDREN_COUNT;
    private OnItemClickListener mListener;

    public TagInnerItemContainer(Context context) {
        this(context, null);
    }

    public TagInnerItemContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagInnerItemContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setItems(List<String> list) {
        this.mItems = list;
        if (ListUtils.isEmpty(mItems)) {
            removeAllViews();
            return;
        }
        int count = Math.min(list.size(), mMaxChildrenCount);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < count; i++) {
            View view = inflater.inflate(R.layout.child_tag_inner_list, this, false);
            view.setTag(i);
            view.setOnClickListener(this);
            ((TextView) view.findViewById(R.id.tv_content)).setText(mItems.get(i));
            addView(view);
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item_container:
                if (mListener != null) {
                    mListener.onItemClick(this, (Integer) v.getTag());
                }
                break;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
