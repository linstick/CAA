package com.luoruiyong.caa.common.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.viewholder.DiscoverItemViewHolder;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.puller.ActivityPuller;
import com.luoruiyong.caa.puller.DiscoverPuller;
import com.luoruiyong.caa.puller.PullerHelper;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_SUCCESS;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_SUCCESS;
import static com.luoruiyong.caa.puller.ActivityPuller.TYPE_SEARCH;
import static com.luoruiyong.caa.puller.DiscoverPuller.TYPE_TOPIC_HOT;

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

    private DiscoverPuller mPuller;

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
        bundle.putInt(KEY_TYPE, TYPE_SEARCH);
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeDiscoverFragment newInstance(int type, int data) {
        SwipeDiscoverFragment fm = new SwipeDiscoverFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        if (type == DiscoverPuller.TYPE_OTHER_USER) {
            bundle.putInt(KEY_OTHER_UID, data);
        } else if (type == TYPE_TOPIC_HOT
                || type == DiscoverPuller.TYPE_TOPIC_LASTED) {
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

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(KEY_TYPE, DiscoverPuller.TYPE_ALL);
            if (mType == DiscoverPuller.TYPE_TOPIC_HOT
                    || mType == DiscoverPuller.TYPE_TOPIC_LASTED) {
                mTopicId = bundle.getInt(KEY_TOPIC_ID);
                mPosition = bundle.getInt(KEY_ITEM_POSITION);
            } else if (mType == DiscoverPuller.TYPE_SEARCH) {
                mKeyword = bundle.getString(KEY_KEYWORD);
            } else if (mType == DiscoverPuller.TYPE_OTHER_USER) {
                mOtherUid = bundle.getInt(KEY_OTHER_UID, -1);
            }
            if (mType != DiscoverPuller.TYPE_ALL) {
                setCanRefresh(false);
            }
        }
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<DiscoverData> list) {
        return new ListAdapter(list);
    }

    private long getFirstItemTime() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(0).getPublishTime();
        }
        return 0;
    }

    private long getLastItemTime() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(mList.size() - 1).getPublishTime();
        }
        return 0;
    }

    @Override
    protected void doRefresh() {
        LogUtils.d(TAG, "doRefresh: " + mType);
        mRefreshLayout.setRefreshing(true);
        switch (mType) {
            case DiscoverPuller.TYPE_ALL:
                mPuller.refreshAll(getFirstItemTime());
                break;
            case DiscoverPuller.TYPE_SELF:
                mPuller.refreshSelf(getFirstItemTime());
                break;
            case DiscoverPuller.TYPE_OTHER_USER:
                mPuller.refreshOtherUser(mOtherUid, getFirstItemTime());
                break;
            case DiscoverPuller.TYPE_SEARCH:
                mPuller.refreshSearch(mKeyword);
                break;
            case DiscoverPuller.TYPE_TOPIC_HOT:
                mPuller.refreshTopicHot(mTopicId, getFirstItemTime());
                break;
            case DiscoverPuller.TYPE_TOPIC_LASTED:
                mPuller.refreshTopicLasted(mTopicId,getFirstItemTime());
                break;
            default:
                LogUtils.d(TAG, "load more unknow type");
                break;
        }
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doRefresh: " + mType);
        ((ListAdapter)mAdapter).showLoadMoreTip(getString(R.string.common_str_loading_more));
        switch (mType) {
            case DiscoverPuller.TYPE_ALL:
                mPuller.loadMoreAll(getLastItemTime());
                break;
            case DiscoverPuller.TYPE_SELF:
                mPuller.loadMoreSelf(getLastItemTime());
                break;
            case DiscoverPuller.TYPE_OTHER_USER:
                mPuller.loadMoreOtherUser(mOtherUid, getLastItemTime());
                break;
            case DiscoverPuller.TYPE_SEARCH:
                mPuller.loadMoreSearch(mKeyword,getLastItemTime());
                break;
            case DiscoverPuller.TYPE_TOPIC_HOT:
                mPuller.loadMoreTopicHot(mTopicId, getLastItemTime());
                break;
            case DiscoverPuller.TYPE_TOPIC_LASTED:
                mPuller.loadMoreTopicLasted(mTopicId,getLastItemTime());
                break;
            default:
                LogUtils.d(TAG, "load more unknow type");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPullFinishEvent(PullFinishEvent event) {
        switch (event.getType()) {
            case TYPE_REFRESH_FAIL:
                mRefreshLayout.setRefreshing(false);
                String error = (String) event.getData();
                if (TextUtils.equals(error, ResourcesUtils.getString(R.string.common_tip_no_network))) {
                    if (ListUtils.isEmpty(mList)) {
                        showErrorView(R.drawable.bg_no_network, error);
                    } else {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (ListUtils.isEmpty(mList)) {
                        showErrorView(R.drawable.bg_load_fail, error);
                    } else {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
                LogUtils.d(TAG, "refresh fail: " + event.getData());
                break;
            case TYPE_REFRESH_SUCCESS:
                mRefreshLayout.setRefreshing(false);
                if (mList == null) {
                    // 首次拉取数据成功，设置到列表中
                    mList = mPuller.getData(mType);
                    mAdapter = getListAdapter(mList);
                    mRecyclerView.setAdapter(mAdapter);
                } else if (mType >= 0 && mType < ActivityPuller.TYPE_SELF) {
                    // 非首次根据条件是否展示更新成功提示
                    showTopTip((Integer) event.getData());
                }
                mAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "refresh success: " + event.getData());
                break;
            case TYPE_LOAD_MORE_FAIL:
                mIsLoadingMore = false;
                mCanLoadMore = false;
                ((ListAdapter) mAdapter).showLoadMoreTip(getString(R.string.common_str_load_fail));
                mAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "load more fail: " + event.getData());
                break;
            case TYPE_LOAD_MORE_SUCCESS:
                mIsLoadingMore = false;
                mCanLoadMore = true;
                ((ListAdapter) mAdapter).showLoadMoreTip(getString(R.string.common_str_load_finish));
                mAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "load more success: " + event.getData());
                break;
            default:
                break;
        }
    }

    private class ListAdapter extends LoadMoreSupportAdapter implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        private List<DiscoverData> mList;
        public ListAdapter(List<DiscoverData> list) {
            this.mList = list;
        }

        public void showLoadMoreTip(String text) {
            setLoadMoreTip(text);
            setOnLoadMoreClickListener(this);
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
        public int getItemCount() {
            int count = ListUtils.getSize(mList);
            return count == 0 ? 0 : count + 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (ListUtils.getSize(mList) == position) {
                return ITEM_TYPE_LOAD_MORE_TIP;
            }
            return ITEM_TYPE_NORMAL;
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
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, true);
        }
    }

    @Override
    protected void onVisibleMaybeChange() {
        super.onVisibleMaybeChange();
        if (isVisibleToUser()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
            if (mPuller == null) {
                mPuller = (DiscoverPuller) PullerHelper.get(PullerHelper.TYPE_DISCOVER);
            }
            mList = mPuller.getData(mType);
            if (mList != null) {
                mAdapter = getListAdapter(mList);
                mRecyclerView.setAdapter(mAdapter);
            } else if (!mRefreshLayout.isRefreshing()) {
                doRefresh();
            }
        } else {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
        }
    }
}
