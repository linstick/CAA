package com.luoruiyong.caa.base;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.viewholder.LoadMoreViewHolder;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public abstract class LoadMoreSupportAdapter extends RecyclerView.Adapter {
    public static final int ITEM_TYPE_LOAD_MORE_TIP = 0;
    public static final int ITEM_TYPE_NORMAL = 1;
    private String mLoadMoreTip;
    private View.OnClickListener mLoadMoreTipClickListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_LOAD_MORE_TIP) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_load_more_tip, parent, false);
            return new LoadMoreViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            holder.itemView.setOnClickListener(mLoadMoreTipClickListener);
            ((TextView) holder.itemView).setText(mLoadMoreTip);
        }
    }

    public void setLoadMoreTip(String text) {
        mLoadMoreTip = text;
    }

    public void setOnLoadMoreClickListener(View.OnClickListener listener) {
        mLoadMoreTipClickListener = listener;
    }
}
