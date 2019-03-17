package com.luoruiyong.caa.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/18/018
 **/
public class UniversalFunctionContainer extends ScrollView implements View.OnClickListener{

    private List<String> mFunctionList;
    private List<ViewHolder> mViewHolderList;
    private OnFunctionClickListener mListener;

    public UniversalFunctionContainer(Context context) {
        this(context, null);
    }

    public UniversalFunctionContainer(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UniversalFunctionContainer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnFunctionClickListener(OnFunctionClickListener listener) {
        mListener = listener;
    }

    public void setFunctionList(List<String> list) {
        setFunctionList(list, null);
    }

    public void setFunctionList(List<String> list, List<Integer> redPointCountList) {
        removeAllViews();
        mFunctionList = list;
        if (ListUtils.isEmpty(mFunctionList) || (!ListUtils.isEmpty(redPointCountList) && ListUtils.getSize(list) != ListUtils.getSize(redPointCountList))) {
            mViewHolderList.clear();
            return;
        }
        mViewHolderList = new ArrayList<>();
        LinearLayout container = new LinearLayout(getContext());
        ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        container.setLayoutParams(params);
        container.setOrientation(LinearLayout.VERTICAL);
        addView(container);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (int i = 0; i < mFunctionList.size(); i++) {
            View view = inflater.inflate(R.layout.item_function_list, container, false);
            view.setOnClickListener(this);
            view.setTag(i);

            ViewHolder holder = new ViewHolder(view);
            holder.mFunctionTv.setText(mFunctionList.get(i));
            mViewHolderList.add(holder);
            container.addView(view);
        }
        updateRedPoints(redPointCountList);
    }

    public void updateRedPoint(int position, int count) {
        if (!ListUtils.isIndexBetween(mViewHolderList, position)) {
            return;
        }
        ViewHolder holder = mViewHolderList.get(position);
        holder.mRedPointTv.setVisibility(count <= 0 ? GONE : VISIBLE);
        holder.mRedPointTv.setText(String.valueOf(count));
    }

    public void updateRedPoints(List<Integer> list) {
        if (ListUtils.getSize(list) != ListUtils.getSize(mFunctionList)) {
            return;
        }
        for (int i = 0; i < ListUtils.getSize(list); i++) {
            updateRedPoint(i, list.get(i));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_item_layout:
                if (mListener != null) {
                    mListener.onFunctionClick((Integer) v.getTag());
                }
                break;
            default:
                break;
        }
    }

    class ViewHolder {
        private TextView mFunctionTv;
        private TextView mRedPointTv;

        public ViewHolder(View rootView) {
            mFunctionTv = rootView.findViewById(R.id.tv_function);
            mRedPointTv = rootView.findViewById(R.id.tv_red_point);
        }
    }

    public interface OnFunctionClickListener {
        void onFunctionClick(int position);
    }
}
