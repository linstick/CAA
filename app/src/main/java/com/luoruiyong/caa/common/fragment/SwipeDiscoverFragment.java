package com.luoruiyong.caa.common.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.model.puller.DiscoverPuller;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeDiscoverFragment extends BaseSwipeFragment<DiscoverData> {

    private static final String TAG = "SwipeDiscoverFragment";

    private static final String KEY_TYPE = "key_type";
    private static final String KEY_TOPIC_ID = "key_topic_id";
    private static final String KEY_ITEM_POSITION = "key_item_position";

    private int mTopicId = -1;
    private int mPosition = 0;

    public static SwipeDiscoverFragment newInstance() {
        return newInstance(Config.PAGE_ID_DISCOVER_ALL);
    }

    public static SwipeDiscoverFragment newInstance(int type) {
        SwipeDiscoverFragment fm = new SwipeDiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeDiscoverFragment newInstance(String keyword) {
        SwipeDiscoverFragment fm = new SwipeDiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, Config.PAGE_ID_DISCOVER_SEARCH);
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeDiscoverFragment newInstance(int type, int data) {
        SwipeDiscoverFragment fm = new SwipeDiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        if (type == Config.PAGE_ID_DISCOVER_OTHER_USER) {
            bundle.putInt(KEY_OTHER_UID, data);
        } else if (type == Config.PAGE_ID_ACTIVITY_TOPIC
                || type == Config.PAGE_ID_DISCOVER_TOPIC) {
            bundle.putInt(KEY_TOPIC_ID, data);
        }
        fm.setArguments(bundle);
        return fm;
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPageId = bundle.getInt(KEY_TYPE, Config.PAGE_ID_DISCOVER_ALL);
            if (mPageId == Config.PAGE_ID_ACTIVITY_TOPIC
                    || mPageId == Config.PAGE_ID_DISCOVER_TOPIC) {
                mTopicId = bundle.getInt(KEY_TOPIC_ID);
                mPosition = bundle.getInt(KEY_ITEM_POSITION);
            } else if (mPageId == Config.PAGE_ID_DISCOVER_SEARCH) {
                mKeyword = bundle.getString(KEY_KEYWORD);
            } else if (mPageId == Config.PAGE_ID_DISCOVER_OTHER_USER) {
                mOtherUid = bundle.getInt(KEY_OTHER_UID, -1);
            }
            if (mPageId != Config.PAGE_ID_DISCOVER_ALL && mPageId != Config.PAGE_ID_DISCOVER_SELF) {
                setCanPullRefresh(false);
            }
        }
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<DiscoverData> list) {
        return new ListAdapter(list);
    }

    private String getFirstItemTime() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(0).getPublishTime();
        }
        return Config.DEFAULT_TIME_STAMP;
    }

    private String getLastItemTime() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(mList.size() - 1).getPublishTime();
        }
        return Config.DEFAULT_TIME_STAMP;
    }

    @Override
    protected void doRefresh() {
        LogUtils.d(TAG, "doRefreshClick: " + mPageId);
        mRefreshLayout.setRefreshing(true);
        switch (mPageId) {
            case Config.PAGE_ID_DISCOVER_ALL:
                DiscoverPuller.refreshAll(getFirstItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_SELF:
                DiscoverPuller.refreshSelf(getFirstItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_OTHER_USER:
                DiscoverPuller.refreshOtherUser(mOtherUid, getFirstItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_SEARCH:
                DiscoverPuller.refreshSearch(mKeyword);
                break;
            case Config.PAGE_ID_ACTIVITY_TOPIC:
                DiscoverPuller.refreshTopicHot(mTopicId, getFirstItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_TOPIC:
                DiscoverPuller.refreshTopicLasted(mTopicId,getFirstItemTime());
                break;
            default:
                LogUtils.d(TAG, "load more unknow type");
                break;
        }
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doLoadMore: " + mPageId);
        if (mAdapter instanceof LoadMoreSupportAdapter) {
            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_loading_more));
        }
        switch (mPageId) {
            case Config.PAGE_ID_DISCOVER_ALL:
                DiscoverPuller.loadMoreAll(getLastItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_SELF:
                DiscoverPuller.loadMoreSelf(getLastItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_OTHER_USER:
                DiscoverPuller.loadMoreOtherUser(mOtherUid, getLastItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_SEARCH:
                DiscoverPuller.loadMoreSearch(mKeyword,getLastItemTime());
                break;
            case Config.PAGE_ID_ACTIVITY_TOPIC:
                DiscoverPuller.loadMoreTopicHot(mTopicId, getLastItemTime());
                break;
            case Config.PAGE_ID_DISCOVER_TOPIC:
                DiscoverPuller.loadMoreTopicLasted(mTopicId,getLastItemTime());
                break;
            default:
                LogUtils.d(TAG, "load more unknow type");
                break;
        }
    }

    private class ListAdapter extends LoadMoreSupportAdapter<DiscoverData> implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        public ListAdapter(List<DiscoverData> list) {
            super(list);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_LOAD_MORE_TIP) {
                return super.onCreateViewHolder(parent, viewType);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discover_list, parent, false);
            return new DiscoverItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof DiscoverItemViewHolder) {
                DiscoverItemViewHolder holder = (DiscoverItemViewHolder) viewHolder;
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
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tv_load_more_tip) {
                if (!mIsLoadingMore) {
                    setLoadMoreTip(getString(R.string.common_str_loading_more));
                    mIsLoadingMore = true;
                    doLoadMore();
                }
                return;
            }
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
                    PageUtils.gotoActivityDetailPage(getContext(), data, true);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onImageClick(View parent, int position) {
            DiscoverData data = (DiscoverData) parent.getTag();
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPullFinishEvent(PullFinishEvent event) {
        super.onPullFinishEvent(event);
        if (mPosition < 1
                || event.getTargetPage() != Config.PAGE_ID_DISCOVER_TOPIC
                || event.getPullType() != Config.PULL_TYPE_REFRESH
                || event.getCode() != Config.CODE_OK) {
            return;
        }
        // 第一次拉去数据成功，话题详情页需要滚动到对应的位置
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Activity activity = getActivity();
                if (activity != null && !activity.isFinishing())
                handleScrollPosition(mPosition);
                if (activity instanceof TopicActivity) {
                    ((TopicActivity) activity).setAppBarExpanded(false);
                }
            }
        }, 500);
    }
}
