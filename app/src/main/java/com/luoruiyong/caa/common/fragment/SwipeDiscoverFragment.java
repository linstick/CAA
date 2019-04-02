package com.luoruiyong.caa.common.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.widget.ImageViewLayout;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeDiscoverFragment extends BaseSwipeFragment<DiscoverData> {

    private static final String KEY_ACTIVITY_TYPE = "key_discover_type";
    public static final int TYPE_ALL = 0;
    public static final int TYPE_SCHOOL_MATE = 1;

    private int mDiscoverType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for test
        for (int i = 0; i < 30; i++) {
            mList.add(new DiscoverData(i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = DEFAULT_ITEM_MARGIN_PX;
                }
                if (parent.getChildAdapterPosition(view) == ListUtils.getSize(mList) - 1) {
                    outRect.bottom = DEFAULT_ITEM_MARGIN_PX;
                }
            }
        });
        return view;
    }

    @Override
    protected void initListAdapter(List<DiscoverData> list) {
        mAdapter = new ListAdapter(list);
    }

    @Override
    protected void doRefresh() {
        Toast.makeText(getContext(), "doRefresh: type = " + mDiscoverType, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doLoadMore() {
        Toast.makeText(getContext(), "doLoadMore: type = " + mDiscoverType, Toast.LENGTH_SHORT).show();
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        private List<DiscoverData> mList;

        public ListAdapter(List<DiscoverData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DiscoverData data = mList.get(position);
            holder.bindData(data);
            holder.mUserAvatarIv.setOnClickListener(this);
            holder.mNicknameTv.setOnClickListener(this);
            holder.mMoreIv.setOnClickListener(this);
            holder.mTopicTv.setOnClickListener(this);
            holder.mLikeTv.setOnClickListener(this);
            holder.mCommentTv.setOnClickListener(this);
            holder.mImageViewLayout.setOnImageClickListener(this);
            holder.mUserAvatarIv.setTag(position);
            holder.mNicknameTv.setTag(position);
            holder.mMoreIv.setTag(position);
            holder.mTopicTv.setTag(position);
            holder.mLikeTv.setTag(position);
            holder.mCommentTv.setTag(position);
            holder.mImageViewLayout.setTag(position);
        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            DiscoverData data = mList.get(position);
            switch (v.getId()) {
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    startActivity(new Intent(getContext(), UserProfileActivity.class));
//                    Toast.makeText(getContext(), "click user info", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_more:
                    showMoreOperateDialog(position, data.getUid());
//                    Toast.makeText(getContext(), "click more", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_topic:
                    startActivity(new Intent(getContext(), TopicActivity.class));
//                    Toast.makeText(getContext(), "click topic", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_like:
                    Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_comment:
                    Toast.makeText(getContext(), "click comment", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onImageClick(View parent, int position) {
            DiscoverData data = (DiscoverData) parent.getTag();
            PictureBrowseActivity.startAction(getContext(), data.getPictureList(), position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mPublishTimeTv;
            private TextView mCollegeTv;
            private ImageView mMoreIv;
            private TextView mContentTv;
            private TextView mLocationTv;
            private TextView mTopicTv;
            private TextView mLikeTv;
            private TextView mCommentTv;
            private ImageViewLayout mImageViewLayout;

            public ViewHolder(View itemView) {
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

                mLikeTv.setText(data.getLikeCount() == 0 ? "Collect" : data.getLikeCount() + "");
                mCommentTv.setText(data.getCommentCount() == 0 ? "Comment" : data.getCommentCount() + "");

                mImageViewLayout.setPictureUrls(data.getPictureList());
            }
        }
    }
}
