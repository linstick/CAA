package com.luoruiyong.caa.common.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.detail.DetailActivity;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeActivityFragment extends BaseSwipeFragment<ActivitySimpleData> {

    private static final String KEY_ACTIVITY_TYPE = "key_activity_type";
    public static final int TYPE_ALL = 0;
    public static final int TYPE_SELF = Enviroment.getActivityTypeMap().size();
    public static final int TYPE_COLLECT = TYPE_SELF + 1;

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
            if (mActivityType == TYPE_ALL || mActivityType == TYPE_SELF || mActivityType == TYPE_COLLECT) {
                mList.add(new ActivitySimpleData(i, (int)(Math.random() * 6 + 1)));
            } else {
                mList.add(new ActivitySimpleData(i, mActivityType));
            }
        }
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

    private class ListAdapter extends RecyclerView.Adapter<ActivityItemViewHolder> implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        private List<ActivitySimpleData> mList;

        public ListAdapter(List<ActivitySimpleData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ActivityItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
            return new ActivityItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ActivityItemViewHolder holder, int position) {
            ActivitySimpleData data = mList.get(position);
            holder.bindData(data, mActivityType);
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
            holder.mImageViewLayout.setTag(data);
        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            ActivitySimpleData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    DetailActivity.startAction(getContext(), data);
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
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, true);
        }
    }
}
