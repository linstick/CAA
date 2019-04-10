package com.luoruiyong.caa.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.TopicSimpleData;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;

import java.util.ArrayList;
import java.util.List;

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

    public void bindData(TopicSimpleData data) {
//                mTagCoverIv.setImageUrl(data.getCoverUrl());
        mTagNameTv.setText(String.format(ResourcesUtils.getString(R.string.common_str_topic), data.getName()));
        mVisitedCountTv.setText(data.getVisitedCount() + "");
        mJoinedCountTv.setText(data.getJoinCount() + "");
        mHeaderDividerView.setVisibility(ListUtils.isEmpty(data.getDiscoverList()) ? View.GONE : View.VISIBLE);
        List<String> list = new ArrayList<>();
        List<DiscoverData> discoverData = data.getDiscoverList();
        for (int i = 0; i < ListUtils.getSize(discoverData); i++) {
            list.add(discoverData.get(i).getContent());
        }
        mInnerContainerLayout.setItems(list);
    }
}

