package com.luoruiyong.caa.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.ImageViewLayoutV2;

public class DiscoverItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView mUserAvatarIv;
    public TextView mNicknameTv;
    public TextView mPublishTimeTv;
    public TextView mCollegeTv;
    public ImageView mMoreIv;
    public TextView mContentTv;
    public TextView mLocationTv;
    public TextView mTopicTv;
    public TextView mLikeTv;
    public TextView mCommentTv;
    public ImageViewLayoutV2 mImageViewLayout;

    public DiscoverItemViewHolder(View itemView) {
        super(itemView);
        mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
        mNicknameTv = itemView.findViewById(R.id.tv_nickname);
        mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
        mCollegeTv = itemView.findViewById(R.id.tv_college);
        mMoreIv = itemView.findViewById(R.id.iv_more);
        mContentTv = itemView.findViewById(R.id.tv_content);
        mLocationTv = itemView.findViewById(R.id.tv_location);
        mTopicTv = itemView.findViewById(R.id.tv_topic);
        mLikeTv = itemView.findViewById(R.id.tv_like);
        mCommentTv = itemView.findViewById(R.id.tv_comment);
        mImageViewLayout = itemView.findViewById(R.id.image_view_layout);
    }

    public void bindData(DiscoverData data) {
//                mUserAvatarIv.setImageUrl(data.getAvatarUrl());
        mNicknameTv.setText(data.getNickname());
//                mPublishTimeTv.setText(data.getPublishTime());

        if (!TextUtils.isEmpty(data.getTopic())) {
            mTopicTv.setVisibility(View.VISIBLE);
            mTopicTv.setText(data.getTopic());
        }

        if (!TextUtils.isEmpty(data.getCollege())) {
            mCollegeTv.setVisibility(View.VISIBLE);
            mCollegeTv.setText(data.getCollege());
        }
        mContentTv.setText(data.getContent());

        if (!TextUtils.isEmpty(data.getLocation())) {
            mLocationTv.setVisibility(View.VISIBLE);
            mLocationTv.setText(data.getLocation());
        }

        mLikeTv.setText(data.getLikeCount() == 0 ? ResourcesUtils.getString(R.string.common_str_like) : data.getLikeCount() + "");
        mCommentTv.setText(data.getCommentCount() == 0 ? ResourcesUtils.getString(R.string.common_str_comment) : data.getCommentCount() + "");

        mImageViewLayout.setPictureUrls(data.getPictureList());
    }
}