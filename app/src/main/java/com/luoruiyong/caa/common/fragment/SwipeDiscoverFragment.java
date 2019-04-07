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

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
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
public class SwipeDiscoverFragment extends BaseSwipeFragment<DiscoverData> {

    private static final String KEY_TYPE = "key_type";
    private static final String KEY_TOPIC_ID = "key_topic_id";

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SELF = 1;
    public static final int TYPE_TOPIC_HOT = 2;
    public static final int TYPE_TOPIC_LASTED = 3;

    private int mType = TYPE_ALL;
    private int mTopicId = -1;

    public static SwipeDiscoverFragment newInstance(int type) {
        return newInstance(type, -1);
    }

    public static SwipeDiscoverFragment newInstance(int type, int topicId) {
        SwipeDiscoverFragment fm = new SwipeDiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putInt(KEY_TOPIC_ID, topicId);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleArguments();

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

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(KEY_TYPE);
            mTopicId = bundle.getInt(KEY_TOPIC_ID);
        }
    }

    @Override
    protected void initListAdapter(List<DiscoverData> list) {
        mAdapter = new ListAdapter(list);
    }

    @Override
    protected void doRefresh() {
        Toast.makeText(getContext(), "doRefresh: type = " + mType + " topic id = " + mTopicId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doLoadMore() {
        Toast.makeText(getContext(), "doLoadMore: type = " + mType + " topic id = " + mTopicId, Toast.LENGTH_SHORT).show();
    }

    private class ListAdapter extends RecyclerView.Adapter<DiscoverItemViewHolder> implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        private List<DiscoverData> mList;

        public ListAdapter(List<DiscoverData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public DiscoverItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_list, parent, false);
            return new DiscoverItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull DiscoverItemViewHolder holder, int position) {
            DiscoverData data = mList.get(position);
            holder.bindData(data);
            holder.itemView.setOnClickListener(this);
            holder.mUserAvatarIv.setOnClickListener(this);
            holder.mNicknameTv.setOnClickListener(this);
            holder.mMoreIv.setOnClickListener(this);
            holder.mTopicTv.setOnClickListener(this);
            holder.mLikeTv.setOnClickListener(this);
            holder.mCommentTv.setOnClickListener(this);
            holder.mImageViewLayout.setOnImageClickListener(this);
            holder.itemView.setTag(position);
            holder.mUserAvatarIv.setTag(position);
            holder.mNicknameTv.setTag(position);
            holder.mMoreIv.setTag(position);
            holder.mTopicTv.setTag(position);
            holder.mLikeTv.setTag(position);
            holder.mCommentTv.setTag(position);
            holder.mImageViewLayout.setTag(data);
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
                case R.id.ll_item_layout:
                    DetailActivity.startAction(getContext(), data);
                    break;
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
            PictureBrowseActivity.startAction(getContext(), data.getPictureList(), position, true);
        }
    }
}
