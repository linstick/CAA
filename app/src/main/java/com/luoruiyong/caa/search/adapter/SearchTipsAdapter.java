package com.luoruiyong.caa.search.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luoruiyong.caa.R;

import java.util.List;

public class SearchTipsAdapter extends RecyclerView.Adapter<SearchTipsAdapter.ViewHolder> implements View.OnClickListener{

    private List<String> mList;
    private OnItemClickListener mListener;

    public SearchTipsAdapter(List<String> mList) {
        this.mList = mList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_tips, parent, false);
        view.setOnClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = mList.get(position);
        holder.bindData(data);
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public void onClick(View v) {
        String data = (String) v.getTag();
        if (mListener != null) {
            mListener.onItemClick(data);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String data);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTipTv;
        private TextView mLabelTv;

        public ViewHolder(View itemView) {
            super(itemView);
            mTipTv = itemView.findViewById(R.id.tv_tip);
            mLabelTv = itemView.findViewById(R.id.tv_label);
        }

        public void bindData(String data) {
            mTipTv.setText(data);
            itemView.setTag(data);
        }
    }
}
