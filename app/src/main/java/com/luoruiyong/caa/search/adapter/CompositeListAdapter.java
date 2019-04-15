package com.luoruiyong.caa.search.adapter;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.common.viewholder.TipViewHolder;
import com.luoruiyong.caa.common.viewholder.TopicItemViewHolder;
import com.luoruiyong.caa.search.CompositeFragment;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class CompositeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

    private final int TYPE_NONE = 0;
    private final int TYPE_USER = 1;
    private final int TYPE_ACTIVITY = 2;
    private final int TYPE_TOPIC = 3;
    private final int TYPE_DISCOVER = 4;
    private final int TYPE_USER_MORE_TIP = 5;
    private final int TYPE_ACTIVITY_MORE_TIP = 6;
    private final int TYPE_TOPIC_MORE_TIP = 7;
    private final int TYPE_DISCOVER_MORE_TIP = 8;

    private List<User> mUserList;
    private List<ActivityData> mActivityList;
    private List<TopicData> mTopicList;
    private List<DiscoverData> mDiscoverList;

    private OnUserViewClickListener mUserViewListener;
    private OnActivityViewClickListener mActivityViewListener;
    private OnTopicViewClickListener mTopicViewListener;
    private OnDiscoverViewClickListener mDiscoverViewListener;
    private OnMoreViewClickListener mMoreClickListener;

    public CompositeListAdapter(
            List<User> userList,
            List<ActivityData> activityList,
            List<TopicData> topicList,
            List<DiscoverData> discoverList) {
        this.mUserList = userList;
        this.mActivityList = activityList;
        this.mTopicList = topicList;
        this.mDiscoverList = discoverList;
    }

    public void setOnUserViewListener(OnUserViewClickListener listener) {
        mUserViewListener = listener;
    }

    public void setOnActivityViewClickListener(OnActivityViewClickListener listener) {
        mActivityViewListener = listener;
    }

    public void setOnTopicViewClickListener(OnTopicViewClickListener listener) {
        mTopicViewListener = listener;
    }

    public void setOnDiscoverViewClickListener(OnDiscoverViewClickListener listener) {
        mDiscoverViewListener = listener;
    }

    public void setOnMoreViewClickListener(OnMoreViewClickListener listener) {
        mMoreClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case TYPE_USER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user, parent, false);
                holder = new UserViewHolder(view);
                break;
            case TYPE_ACTIVITY:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
                holder = new ActivityItemViewHolder(view);
                break;
            case TYPE_TOPIC:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_list, parent, false);
                holder = new TopicItemViewHolder(view);
                break;
            case TYPE_DISCOVER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_list, parent, false);
                holder = new DiscoverItemViewHolder(view);
                break;
            case TYPE_USER_MORE_TIP:
            case TYPE_ACTIVITY_MORE_TIP:
            case TYPE_TOPIC_MORE_TIP:
            case TYPE_DISCOVER_MORE_TIP:
                holder = createTipViewHolder(parent, viewType);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ActivityItemViewHolder) {
            ActivityItemViewHolder holder = (ActivityItemViewHolder) viewHolder;
            int realPosition = position;
            ActivityData data = mActivityList.get(realPosition);
            holder.bindData(data, -1);
            holder.itemView.setOnClickListener(mActivityViewListener);
            holder.mUserAvatarIv.setOnClickListener(mActivityViewListener);
            holder.mNicknameTv.setOnClickListener(mActivityViewListener);
            holder.mTopicTv.setOnClickListener(mActivityViewListener);
            holder.mCollectTv.setOnClickListener(mActivityViewListener);
            holder.mCommentTv.setOnClickListener(mActivityViewListener);
            holder.mMoreIv.setOnClickListener(mActivityViewListener);
            holder.mImageViewLayout.setOnImageClickListener(mActivityViewListener);
            holder.itemView.setTag(realPosition);
            holder.mUserAvatarIv.setTag(realPosition);
            holder.mNicknameTv.setTag(realPosition);
            holder.mTopicTv.setTag(realPosition);
            holder.mCollectTv.setTag(realPosition);
            holder.mCommentTv.setTag(realPosition);
            holder.mMoreIv.setTag(realPosition);
            holder.mImageViewLayout.setTag(data);
        } else if (viewHolder instanceof UserViewHolder) {
            UserViewHolder holder = (UserViewHolder) viewHolder;
            holder.bindData(mUserList);
            holder.mMoreTv.setOnClickListener(this);
            holder.mMoreTv.setTag(TYPE_USER_MORE_TIP);
            holder.setOnUserViewClickListener(mUserViewListener);
        } else if (viewHolder instanceof  TopicItemViewHolder) {
            int readPosition = position - getTopicOffset();
            TopicData data = mTopicList.get(readPosition);
            TopicItemViewHolder holder = (TopicItemViewHolder) viewHolder;
            holder.bindData(data);
            holder.itemView.setOnClickListener(mTopicViewListener);
            holder.mMoreIv.setOnClickListener(mTopicViewListener);
            holder.mInnerContainerLayout.setOnItemClickListener(readPosition, mTopicViewListener);
            holder.itemView.setTag(readPosition);
            holder.mInnerContainerLayout.setTag(readPosition);
            holder.mMoreIv.setTag(readPosition);
        } else if (viewHolder instanceof DiscoverItemViewHolder) {
            int realPosition = position - getDiscoverOffset();
            DiscoverData data = mDiscoverList.get(realPosition);
            DiscoverItemViewHolder holder = (DiscoverItemViewHolder) viewHolder;
            holder.bindData(data);
            holder.itemView.setOnClickListener(mDiscoverViewListener);
            holder.mUserAvatarIv.setOnClickListener(mDiscoverViewListener);
            holder.mNicknameTv.setOnClickListener(mDiscoverViewListener);
            holder.mMoreIv.setOnClickListener(mDiscoverViewListener);
            holder.mTopicTv.setOnClickListener(mDiscoverViewListener);
            holder.mLikeTv.setOnClickListener(mDiscoverViewListener);
            holder.mCommentTv.setOnClickListener(mDiscoverViewListener);
            holder.mImageViewLayout.setOnImageClickListener(mDiscoverViewListener);
            holder.itemView.setTag(realPosition);
            holder.mUserAvatarIv.setTag(realPosition);
            holder.mNicknameTv.setTag(realPosition);
            holder.mMoreIv.setTag(realPosition);
            holder.mTopicTv.setTag(realPosition);
            holder.mLikeTv.setTag(realPosition);
            holder.mCommentTv.setTag(realPosition);
            holder.mImageViewLayout.setTag(data);
        } else if (viewHolder instanceof TipViewHolder) {
            TipViewHolder holder = (TipViewHolder) viewHolder;
            String data;
            switch ((int) holder.itemView.getTag()) {
                case TYPE_ACTIVITY_MORE_TIP:
                    data = ResourcesUtils.getString(R.string.search_str_more_activity);
                    break;
                case TYPE_TOPIC_MORE_TIP:
                    data = ResourcesUtils.getString(R.string.search_str_more_topic);
                    break;
                case TYPE_DISCOVER_MORE_TIP:
                    data = ResourcesUtils.getString(R.string.search_str_more_discover);
                    break;
                default:
                    data = null;
                    break;
            }
            holder.bindData(data);
            holder.itemView.setOnClickListener(this);
        }
    }

    @Override
    public int getItemCount() {
        int discoverCount = ListUtils.getSize(mDiscoverList);
        return getDiscoverOffset() + discoverCount + (discoverCount == CompositeFragment.MAX_ITEM_COUNT_OF_ONE_TYPE ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        int offset = 0;
        int nextOffset = getUserOffset();
        int size = ListUtils.getSize(mActivityList);
        if (ListUtils.isIndexBetween(mActivityList, position - offset)) {
            return TYPE_ACTIVITY;
        }
        if (offset + size < nextOffset && offset + size == position) {
            return TYPE_ACTIVITY_MORE_TIP;
        }

        offset = nextOffset;
        nextOffset = getTopicOffset();
        if (offset < nextOffset && offset == position) {
            return TYPE_USER;
        }

        offset = nextOffset;
        nextOffset = getDiscoverOffset();
        size = ListUtils.getSize(mTopicList);
        if (ListUtils.isIndexBetween(mTopicList, position - offset)) {
            return TYPE_TOPIC;
        }
        if (offset + size < nextOffset && offset + size == position) {
            return TYPE_TOPIC_MORE_TIP;
        }

        offset = nextOffset;
        nextOffset = getItemCount();
        size = ListUtils.getSize(mDiscoverList);
        if (ListUtils.isIndexBetween(mDiscoverList, position - offset)) {
            return TYPE_DISCOVER;
        }
        if (offset + size < nextOffset && offset + size == position) {
            return TYPE_DISCOVER_MORE_TIP;
        }
        return TYPE_NONE;
    }

    private TipViewHolder createTipViewHolder(ViewGroup parent, int type) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tips, parent, false);
        TipViewHolder holder = new TipViewHolder(view);
        holder.itemView.setTag(type);
        return holder;
    }

    private int getUserOffset() {
        int activityCount = ListUtils.getSize(mActivityList);
        return activityCount + (activityCount == CompositeFragment.MAX_ITEM_COUNT_OF_ONE_TYPE ? 1 : 0);
    }

    private int getTopicOffset() {
        return getUserOffset() + (ListUtils.isEmpty(mUserList) ? 0 : 1);
    }

    private int getDiscoverOffset() {
        int topicCount = ListUtils.getSize(mTopicList);
        return getTopicOffset() + topicCount +  (topicCount == CompositeFragment.MAX_ITEM_COUNT_OF_ONE_TYPE ? 1 : 0);
    }

    @Override
    public void onClick(View v) {
        // 查看更多点击回调
        switch ((int) v.getTag()) {
            case TYPE_USER_MORE_TIP:
                if (mMoreClickListener != null) {
                    mMoreClickListener.onUserMoreClick();
                }
                break;
            case TYPE_ACTIVITY_MORE_TIP:
                if (mMoreClickListener != null) {
                    mMoreClickListener.onActivityMoreClick();
                }
                break;
            case TYPE_TOPIC_MORE_TIP:
                if (mMoreClickListener != null) {
                    mMoreClickListener.onTopicMoreClick();
                }
                break;
            case TYPE_DISCOVER_MORE_TIP:
                if (mMoreClickListener != null) {
                    mMoreClickListener.onDiscoverMoreClick();
                }
                break;
            default:
                break;
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        private RecyclerView mUserListRv;
        private TextView mMoreTv;
        private UserListAdapter mAdapter;

        public UserViewHolder(View itemView) {
            super(itemView);
            mUserListRv = itemView.findViewById(R.id.rv_recycler_view);
            mMoreTv = itemView.findViewById(R.id.tv_more);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            mUserListRv.setLayoutManager(layoutManager);
            mUserListRv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    if (parent.getChildAdapterPosition(view) != 0) {
                        outRect.left = DisplayUtils.dp2px(10);
                    }
                }
            });
        }

        public void bindData(List<User> users) {
            if (mAdapter == null) {
                mAdapter = new UserListAdapter(users);
                mUserListRv.setAdapter(mAdapter);
            }
            if (ListUtils.getSize(mUserList) == CompositeFragment.MAX_ITEM_COUNT_OF_ONE_TYPE) {
                mMoreTv.setVisibility(View.VISIBLE);
            }
        }

        public void setOnUserViewClickListener (OnUserViewClickListener listener) {
            mAdapter.setOnUserViewClickListener(listener);
        }
    }

    public interface OnMoreViewClickListener {
        void onUserMoreClick();
        void onActivityMoreClick();
        void onTopicMoreClick();
        void onDiscoverMoreClick();
    }

    public interface OnUserViewClickListener {
        void onUserItemClick(User user);
    }

    public interface OnActivityViewClickListener extends View.OnClickListener, ImageViewLayout.OnImageClickListener {
    }

    public interface OnTopicViewClickListener extends View.OnClickListener, TagInnerItemContainer.OnItemClickListener {
    }

    public interface OnDiscoverViewClickListener extends View.OnClickListener, ImageViewLayout.OnImageClickListener {
    }
}
