package com.luoruiyong.caa.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.luoruiyong.caa.R;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class TipViewHolder extends RecyclerView.ViewHolder {

    public TextView mTipTv;

    public TipViewHolder(View itemView) {
        super(itemView);
        mTipTv = itemView.findViewById(R.id.tv_tip);
    }

    public void bindData(String data) {
        mTipTv.setText(data);
    }
}
