package com.luoruiyong.caa.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.TagSimpleData;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class TopicItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView mTagCoverIv;
    public TextView mTagNameTv;
    public TextView mVisitedCountTv;
    public TextView mJoinedCountTv;
    public ImageView mMoreIv;
    public View mHeaderDividerView;
    public TagInnerItemContainer mInnerContainerLayout;

    public TopicItemViewHolder(View itemView) {
        super(itemView);
        mTagCoverIv = itemView.findViewById(R.id.iv_tag_cover);
        mTagNameTv = itemView.findViewById(R.id.tv_tag_name);
        mVisitedCountTv = itemView.findViewById(R.id.tv_visit_count);
        mJoinedCountTv = itemView.findViewById(R.id.tv_join_count);
        mMoreIv = itemView.findViewById(R.id.iv_more);
        mHeaderDividerView = itemView.findViewById(R.id.view_header_divider);
        mInnerContainerLayout = itemView.findViewById(R.id.inner_container_layout);
    }

    public void bindData(TagSimpleData data) {
//                mTagCoverIv.setImageUrl(data.getCoverUrl());
        mTagNameTv.setText(data.getName());
        mVisitedCountTv.setText(data.getVisitedCount() + "");
        mJoinedCountTv.setText(data.getJoinCount() + "");
        mHeaderDividerView.setVisibility(ListUtils.isEmpty(data.getDiscoverList()) ? View.GONE : View.VISIBLE);
        mInnerContainerLayout.setItems(data.getDiscoverList());
    }
}

