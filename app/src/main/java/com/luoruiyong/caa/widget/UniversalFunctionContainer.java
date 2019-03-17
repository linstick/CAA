package com.luoruiyong.caa.widget;

import android.content.Context;
import android.media.Image;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.Function;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/18/018
 **/
public class UniversalFunctionContainer extends ScrollView implements View.OnClickListener{

    private List<Function> mFunctionList;
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

    public void setFunctionList(List<Function> list) {
        removeAllViews();
        mFunctionList = list;
        if (ListUtils.isEmpty(mFunctionList)) {
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
            holder.mType = mFunctionList.get(i).getType();
            holder.mFunctionTv.setText(mFunctionList.get(i).getName());
            mViewHolderList.add(holder);
            container.addView(view);
        }

        initUiByType();
    }

    private void initUiByType() {
        if (ListUtils.isEmpty(mViewHolderList)) {
            return;
        }
        for (ViewHolder holder : mViewHolderList) {
            switch (holder.mType) {
                case Function.TYPE_ONLY_WITH_RIGHT_SIGN:
                case Function.TYPE_WITH_RED_POINT_AND_RIGHT_SIGN:
                case Function.TYPE_WITH_LITTLE_RED_POINT_AND_RIGHT_SIGN:
                    holder.mRightSignIv.setVisibility(VISIBLE);
                    break;
                case Function.TYPE_ONLY_WITH_RIGHT_INFO:
                case Function.TYPE_ONLY_WITH_LITTLE_RED_POINT:
                case Function.TYPE_WITH_NONE:
                default:
                    break;
            }
        }
    }

    public void showRedPoint(int position, int count) {
        ViewHolder holder = mViewHolderList.get(position);
        holder.mRedPointTv.setVisibility(count <= 0 ? GONE : VISIBLE);
        holder.mRedPointTv.setText(String.valueOf(count));
    }

    public void hideRedPoint(int position) {
        mViewHolderList.get(position).mRedPointTv.setVisibility(GONE);
    }

    public void showRightInfo(int position, String info) {
        ViewHolder holder = mViewHolderList.get(position);
        holder.mRightInfoTv.setVisibility(VISIBLE);
        holder.mRightInfoTv.setText(info);
    }

    public void hideRightInfo(int position) {
        mViewHolderList.get(position).mRightInfoTv.setVisibility(GONE);
    }

    public void showLittleRedPoint(int position) {
        mViewHolderList.get(position).mLittleRedPointIv.setVisibility(VISIBLE);
    }

    public void hideLittleRedPoint(int position) {
        mViewHolderList.get(position).mLittleRedPointIv.setVisibility(GONE);
    }

    public void showRedPoints(List<Integer> list) {
        if (ListUtils.getSize(list) != ListUtils.getSize(mFunctionList)) {
            return;
        }
        for (int i = 0; i < ListUtils.getSize(list); i++) {
            showRedPoint(i, list.get(i));
        }
    }

    public void hideRedPoints() {
        for (int i = 0; i < ListUtils.getSize(mViewHolderList); i++) {
            hideRedPoint(i);
        }
    }

    public void showRightInfos(List<String> infos) {
        if (ListUtils.getSize(infos) != ListUtils.getSize(mFunctionList)) {
            return;
        }
        for (int i = 0; i < ListUtils.getSize(infos); i++) {
            showRightInfo(i, infos.get(i));
        }
    }

    public void hideRightInfos() {
        for (int i = 0; i < ListUtils.getSize(mViewHolderList); i++) {
            hideRightInfo(i);
        }
    }

    public void showLittleRedPoints() {
        for (int i = 0; i < ListUtils.getSize(mViewHolderList); i++) {
            showLittleRedPoint(i);
        }
    }

    public void hideLittleRedPoints() {
        for (int i = 0; i < ListUtils.getSize(mViewHolderList); i++) {
            hideLittleRedPoint(i);
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

        private int mType;

        private TextView mFunctionTv;
        private TextView mRedPointTv;
        private TextView mRightInfoTv;
        private ImageView mRightSignIv;
        private ImageView mLittleRedPointIv;

        public ViewHolder(View rootView) {
            mFunctionTv = rootView.findViewById(R.id.tv_function);
            mRedPointTv = rootView.findViewById(R.id.tv_red_point);
            mRightInfoTv = rootView.findViewById(R.id.tv_right_info);
            mRightSignIv = rootView.findViewById(R.id.iv_right_sign);
            mLittleRedPointIv = rootView.findViewById(R.id.iv_little_red_point);
        }
    }

    public interface OnFunctionClickListener {
        void onFunctionClick(int position);
    }
}
