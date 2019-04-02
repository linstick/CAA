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

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.detail.DetailActivity;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.ImageViewLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeActivityFragment extends BaseSwipeFragment<ActivitySimpleData> {

    private static final String KEY_ACTIVITY_TYPE = "key_activity_type";
    public static final int TYPE_ALL = 0;

    private int mActivityType;

    public static SwipeActivityFragment newInstance() {
        return newInstance(TYPE_ALL);
    }

    public static SwipeActivityFragment newInstance(int activityType) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_ACTIVITY_TYPE, activityType);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleArguments();

        // for test
        for (int i = 0; i < 30; i++) {
            if (mActivityType == TYPE_ALL) {
                mList.add(new ActivitySimpleData(i, (int)(Math.random() * 6 + 1)));
            } else {
                mList.add(new ActivitySimpleData(i, mActivityType));
            }
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

    private void handleArguments() {
        Bundle bundle = getArguments();
        mActivityType = bundle == null ? TYPE_ALL : bundle.getInt(KEY_ACTIVITY_TYPE, TYPE_ALL);
    }

    @Override
    protected void initListAdapter(List<ActivitySimpleData> list) {
        mAdapter = new ListAdapter(list);
    }

    @Override
    protected void doRefresh() {
        Toast.makeText(getContext(), "doRefresh: type = " + mActivityType, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doLoadMore() {
        Toast.makeText(getContext(), "doLoadMore: type = " + mActivityType, Toast.LENGTH_SHORT).show();
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        private List<ActivitySimpleData> mList;

        public ListAdapter(List<ActivitySimpleData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ActivitySimpleData data = mList.get(position);
            holder.bindData(data);
            holder.itemView.setOnClickListener(this);
            holder.mUserAvatarIv.setOnClickListener(this);
            holder.mNicknameTv.setOnClickListener(this);
            holder.mActivityTypeTv.setOnClickListener(this);
            holder.mTopicTv.setOnClickListener(this);
            holder.mCollectTv.setOnClickListener(this);
            holder.mCommentTv.setOnClickListener(this);
            holder.mMoreIv.setOnClickListener(this);
            holder.mImageViewLayout.setOnImageClickListener(this);
            holder.itemView.setTag(position);
            holder.mUserAvatarIv.setTag(position);
            holder.mNicknameTv.setTag(position);
            holder.mActivityTypeTv.setTag(position);
            holder.mTopicTv.setTag(position);
            holder.mCollectTv.setTag(position);
            holder.mCommentTv.setTag(position);
            holder.mMoreIv.setTag(position);
            holder.mImageViewLayout.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            ActivitySimpleData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
//                    Toast.makeText(getContext(), "click item layout", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), DetailActivity.class));
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
//                    Toast.makeText(getContext(), "click user info", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getContext(), UserProfileActivity.class));
                    break;
                case R.id.tv_activity_type:
                    Toast.makeText(getContext(), "click activity_type", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_topic:
                    startActivity(new Intent(getContext(), TopicActivity.class));
//                    Toast.makeText(getContext(), "click topic", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_collect:
                    Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_comment:
                    Toast.makeText(getContext(), "click comment", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_more:
                    showMoreOperateDialog(position, data.getUid());
//                    Toast.makeText(getContext(), "click more", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onImageClick(View parent, int position) {
            ActivitySimpleData data = (ActivitySimpleData) parent.getTag();
            PictureBrowseActivity.startAction(getContext(), data.getPictureList(), position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mPublishTimeTv;
            private TextView mActivityTypeTv;
            private TextView mActivityTitleTv;
            private TextView mActivityContentTv;
            private TextView mLocationTv;
            private TextView mTopicTv;
            private TextView mCollectTv;
            private TextView mCommentTv;
            private TextView mMoreIv;
            private ImageViewLayout mImageViewLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
                mNicknameTv = itemView.findViewById(R.id.tv_nickname);
                mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
                mActivityTypeTv = itemView.findViewById(R.id.tv_activity_type);
                mActivityTitleTv = itemView.findViewById(R.id.tv_activity_title);
                mActivityContentTv = itemView.findViewById(R.id.tv_activity_content);
                mLocationTv = itemView.findViewById(R.id.tv_location);
                mTopicTv = itemView.findViewById(R.id.tv_topic);
                mCollectTv = itemView.findViewById(R.id.tv_collect);
                mCommentTv = itemView.findViewById(R.id.tv_comment);
                mMoreIv = itemView.findViewById(R.id.tv_more);
                mImageViewLayout = itemView.findViewById(R.id.image_view_layout);
            }

            public void bindData(ActivitySimpleData data) {
//                mUserAvatarIv.setImageUrl(data.getAvatarUrl());
                mNicknameTv.setText(data.getNickname());
//                mPublishTimeTv.setText(data.getPublishTime());
                if (data.getType() != mActivityType) {
                    mActivityTypeTv.setVisibility(View.VISIBLE);
                    mActivityTypeTv.setText(Enviroment.getActivityTypeNameById(data.getType()));
                }
                mActivityTitleTv.setText(data.getTitle());
                mActivityContentTv.setText(data.getContent());

                if (!TextUtils.isEmpty(data.getLocation())) {
                    mLocationTv.setVisibility(View.VISIBLE);
                    mLocationTv.setText(data.getLocation());
                }

                if (!TextUtils.isEmpty(data.getTopic())) {
                    mTopicTv.setVisibility(View.VISIBLE);
                    mTopicTv.setText(data.getTopic());
                }

                mCollectTv.setText(data.getCollectCount() == 0 ? "Collect" : data.getCollectCount() + "");
                mCommentTv.setText(data.getCommentCount() == 0 ? "Comment" : data.getCommentCount() + "");

                mImageViewLayout.setPictureUrls(data.getPictureList());
            }
        }
    }
}
