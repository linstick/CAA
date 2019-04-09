package com.luoruiyong.caa.common.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.widget.TopSmoothScroller;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeDiscoverFragment extends BaseSwipeFragment<DiscoverData> {

    private static final String KEY_TYPE = "key_type";
    private static final String KEY_TOPIC_ID = "key_topic_id";
    private static final String KEY_ITEM_POSITION = "key_item_position";

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SELF = 1;
    public static final int TYPE_TOPIC_HOT = 2;
    public static final int TYPE_TOPIC_LASTED = 3;

    private int mType = TYPE_ALL;
    private int mTopicId = -1;
    private int mPosition = 0;

    public static SwipeDiscoverFragment newInstance(int type) {
        return newInstance(type, -1);
    }

    public static SwipeDiscoverFragment newInstance(int type, int topicId) {
        return newInstance(type, topicId, 0);
    }

    public static SwipeDiscoverFragment newInstance(int type, int topicId, int position) {
        SwipeDiscoverFragment fm = new SwipeDiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putInt(KEY_TOPIC_ID, topicId);
        bundle.putInt(KEY_ITEM_POSITION, position);
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

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(KEY_TYPE);
            mTopicId = bundle.getInt(KEY_TOPIC_ID);
            mPosition = bundle.getInt(KEY_ITEM_POSITION);
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
                    PageUtils.gotoActivityDetailPage(getContext(), data);
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    PageUtils.gotoUserProfilePage(getContext(), data.getUid());
                    break;
                case R.id.iv_more:
                    showMoreOperateDialog(position, data.getUid());
                    break;
                case R.id.tv_topic:
                    PageUtils.gotoTopicPage(getContext(), data.getTopicId());
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
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, true);
        }
    }
}
