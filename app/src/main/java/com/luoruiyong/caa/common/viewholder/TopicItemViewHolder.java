package com.luoruiyong.caa.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.utils.TimeUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class TopicItemViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView mTagCoverIv;
    public TextView mTagNameTv;
    public TextView mIntroductionTv;
    public TextView mVisitedCountTv;
    public TextView mJoinedCountTv;
    public TextView mPublishTimeTv;
    public ImageView mMoreIv;
    public View mHeaderDividerView;
    public TagInnerItemContainer mInnerContainerLayout;

    public TopicItemViewHolder(View itemView) {
        super(itemView);
        mTagCoverIv = itemView.findViewById(R.id.iv_tag_cover);
        mTagNameTv = itemView.findViewById(R.id.tv_tag_name);
        mIntroductionTv = itemView.findViewById(R.id.tv_introduction);
        mVisitedCountTv = itemView.findViewById(R.id.tv_visit_count);
        mJoinedCountTv = itemView.findViewById(R.id.tv_join_count);
        mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
        mMoreIv = itemView.findViewById(R.id.iv_more);
        mHeaderDividerView = itemView.findViewById(R.id.view_header_divider);
        mInnerContainerLayout = itemView.findViewById(R.id.inner_container_layout);
    }

    public void bindData(TopicData data) {
        if (!TextUtils.isEmpty(data.getCoverUrl())) {
            mTagCoverIv.setImageURI(data.getCoverUrl());
        } else {
            mTagCoverIv.setActualImageResource(R.drawable.test_image);
        }
        mTagNameTv.setText(String.format(ResourcesUtils.getString(R.string.common_str_topic), data.getName()));
        String introduction = TextUtils.isEmpty(data.getIntroduction()) ? null : String.format(ResourcesUtils.getString(R.string.fm_topic_str_introduction), data.getIntroduction());
        mIntroductionTv.setText(introduction);
        mVisitedCountTv.setText(String.format(ResourcesUtils.getString(R.string.fm_topic_str_visited), data.getVisitedCount()));
        mJoinedCountTv.setText(String.format(ResourcesUtils.getString(R.string.fm_topic_str_joined), data.getJoinCount()));
        mHeaderDividerView.setVisibility(ListUtils.isEmpty(data.getDiscoverList()) ? View.GONE : View.VISIBLE);
        mPublishTimeTv.setText(TimeUtils.format(data.getPublishTime()));
        List<String> list = new ArrayList<>();
        List<DiscoverData> discoverData = data.getDiscoverList();
        for (int i = 0; i < ListUtils.getSize(discoverData); i++) {
            list.add(discoverData.get(i).getContent());
        }
        mInnerContainerLayout.setItems(list);
    }
}

