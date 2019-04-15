package com.luoruiyong.caa.common.viewholder;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

public class ActivityItemViewHolder extends RecyclerView.ViewHolder {

    public SimpleDraweeView mUserAvatarIv;
    public TextView mNicknameTv;
    public TextView mPublishTimeTv;
    public TextView mActivityTypeTv;
    public TextView mActivityTitleTv;
    public TextView mActivityContentTv;
    public TextView mLocationTv;
    public TextView mTopicTv;
    public TextView mCollectTv;
    public TextView mCommentTv;
    public TextView mMoreIv;
    public ImageViewLayout mImageViewLayout;
    public ViewStub mExtrasVs;

    public ActivityItemViewHolder(View itemView) {
        super(itemView);
        mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
        mNicknameTv = itemView.findViewById(R.id.tv_nickname);
        mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
        mActivityTypeTv = itemView.findViewById(R.id.tv_activity_type);
        mActivityTitleTv = itemView.findViewById(R.id.tv_activity_title_label);
        mActivityContentTv = itemView.findViewById(R.id.tv_activity_content);
        mLocationTv = itemView.findViewById(R.id.tv_location);
        mTopicTv = itemView.findViewById(R.id.tv_topic);
        mCollectTv = itemView.findViewById(R.id.tv_collect);
        mCommentTv = itemView.findViewById(R.id.tv_comment);
        mMoreIv = itemView.findViewById(R.id.tv_more);
        mImageViewLayout = itemView.findViewById(R.id.image_view_layout);
        mExtrasVs = itemView.findViewById(R.id.vs_activity_extras);
    }

    public void bindData(ActivityData data) {
        bindData(data, -1);
    }

    public void bindData(ActivityData data, int type) {
        mUserAvatarIv.setImageURI(data.getAvatarUrl());
        mNicknameTv.setText(data.getNickname());
        mPublishTimeTv.setText(data.getPublishTime());

        if (data.getType() != type) {
            mActivityTypeTv.setText(Enviroment.getActivityTypeNameById(data.getType()));
        } else {
            mActivityTypeTv.setVisibility(View.GONE);
        }
        mActivityTitleTv.setText(data.getTitle());
        mActivityContentTv.setText(data.getContent());

        if (!TextUtils.isEmpty(data.getLocation())) {
            mLocationTv.setVisibility(View.VISIBLE);
            mLocationTv.setText(data.getLocation());
        }

        if (!TextUtils.isEmpty(data.getTopic())) {
            mTopicTv.setVisibility(View.VISIBLE);
            mTopicTv.setText(String.format(ResourcesUtils.getString(R.string.common_str_topic), data.getTopic()));
        }

        mCollectTv.setText(data.getCollectCount() == 0 ? ResourcesUtils.getString(R.string.common_str_collect) : data.getCollectCount() + "");
        mCollectTv.setSelected(data.isHasCollect());
        mCommentTv.setText(data.getCommentCount() == 0 ? ResourcesUtils.getString(R.string.common_str_comment) : data.getCommentCount() + "");

        mImageViewLayout.setPictureUrls(data.getPictureList());
    }
}
