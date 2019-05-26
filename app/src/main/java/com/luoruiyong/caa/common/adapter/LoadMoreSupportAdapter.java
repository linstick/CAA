package com.luoruiyong.caa.common.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.viewholder.LoadMoreViewHolder;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class LoadMoreSupportAdapter<Item> extends RecyclerView.Adapter{

    public static final int ITEM_TYPE_LOAD_MORE_TIP = 0;
    public static final int ITEM_TYPE_NORMAL = 1;

    private List<Item> mList;
    private String mLoadMoreTip;
    private View.OnClickListener mListener;

    public LoadMoreSupportAdapter(List<Item> mList) {
        this.mList = mList;
    }

    public void setLoadMoreTip(String text) {
        mLoadMoreTip = text;
    }

    public void setLoadMoreTipClickListener(View.OnClickListener listener) {
        mListener = listener;
    }

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
            holder.itemView.setOnClickListener(mListener);
            ((TextView) holder.itemView).setText(mLoadMoreTip);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List payloads) {
        if (holder instanceof LoadMoreViewHolder) {
            holder.itemView.setOnClickListener(mListener);
            ((TextView) holder.itemView).setText(mLoadMoreTip);
        }
    }

    @Override
    public int getItemCount() {
        int count = ListUtils.getSize(mList);
        return count == 0 ? 0 : count + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (ListUtils.getSize(mList) == position) {
            return ITEM_TYPE_LOAD_MORE_TIP;
        }
        return ITEM_TYPE_NORMAL;
    }
}
