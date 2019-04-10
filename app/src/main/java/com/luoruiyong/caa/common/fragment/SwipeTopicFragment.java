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
import com.luoruiyong.caa.bean.TopicSimpleData;
import com.luoruiyong.caa.common.viewholder.TopicItemViewHolder;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.puller.ActivityPuller;
import com.luoruiyong.caa.puller.PullerHelper;
import com.luoruiyong.caa.puller.TopicPuller;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_SUCCESS;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_SUCCESS;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeTopicFragment extends BaseSwipeFragment<TopicSimpleData> {

    private static final String TAG = "SwipeTopicFragment";

    private TopicPuller mPuller;

    public static SwipeTopicFragment newInstance(int type) {
        SwipeTopicFragment fm = new SwipeTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeTopicFragment newInstance(String keyword) {
        SwipeTopicFragment fm = new SwipeTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, TopicPuller.TYPE_SEARCH);
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeTopicFragment newInstance(int type, int uid) {
        SwipeTopicFragment fm = new SwipeTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putInt(KEY_OTHER_UID, uid);
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
        if (bundle == null) {
            getActivity().finish();
            return;
        }
        mType = bundle.getInt(KEY_TYPE, TopicPuller.TYPE_ALL);
        if (mType == TopicPuller.TYPE_SEARCH) {
            mKeyword = bundle.getString(KEY_KEYWORD);
        } else if (mType == TopicPuller.TYPE_OTHER_USER) {
            mOtherUid = bundle.getInt(KEY_OTHER_UID, -1);
        }
        if (mType != TopicPuller.TYPE_ALL) {
            setCanRefresh(false);
        }
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<TopicSimpleData> list) {
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
            case TopicPuller.TYPE_ALL:
                mPuller.refreshAll(getFirstItemTime());
                break;
            case TopicPuller.TYPE_SELF:
                mPuller.refreshSelf(getFirstItemTime());
                break;
            case TopicPuller.TYPE_OTHER_USER:
                mPuller.refreshOtherUser(mOtherUid, getFirstItemTime());
                break;
            case TopicPuller.TYPE_SEARCH:
                mPuller.refreshSearch(mKeyword);
                break;
            default:
                LogUtils.d(TAG, "refresh unknow type");
                break;
        }
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doLoadMore: " + mType);
        ((ListAdapter)mAdapter).showLoadMoreTip(getString(R.string.common_str_loading_more));
        switch (mType) {
            case TopicPuller.TYPE_ALL:
                mPuller.loadMoreAll(getLastItemTime());
                break;
            case TopicPuller.TYPE_SELF:
                mPuller.loadMoreSelf(getLastItemTime());
                break;
            case TopicPuller.TYPE_OTHER_USER:
                mPuller.loadMoreOtherUser(mOtherUid, getLastItemTime());
                break;
            case TopicPuller.TYPE_SEARCH:
                mPuller.loadMoreSearch(mKeyword,getLastItemTime());
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
                } else if (mType == TopicPuller.TYPE_ALL) {
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

    private class ListAdapter extends LoadMoreSupportAdapter implements View.OnClickListener, TagInnerItemContainer.OnItemClickListener{

        private List<TopicSimpleData> mList;

        public void showLoadMoreTip(String text) {
            setLoadMoreTip(text);
            setOnLoadMoreClickListener(this);
        }

        public ListAdapter(List<TopicSimpleData> list) {
            this.mList = list;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_LOAD_MORE_TIP) {
                return super.onCreateViewHolder(parent, viewType);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_list, parent, false);
            return new TopicItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof TopicItemViewHolder) {
                TopicItemViewHolder holder = (TopicItemViewHolder) viewHolder;
                TopicSimpleData data = mList.get(position);
                holder.bindData(data);

                holder.itemView.setOnClickListener(this);
                holder.mMoreIv.setOnClickListener(this);
                holder.mInnerContainerLayout.setOnItemClickListener(this);
                holder.itemView.setTag(position);
                holder.mInnerContainerLayout.setTag(position);
                holder.mMoreIv.setTag(position);
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
            TopicSimpleData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    PageUtils.gotoTopicPage(getContext(), data.getId());
                    break;
                case R.id.iv_more:
                    showMoreOperateDialog(position, data.getUid());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            TopicSimpleData data = mList.get(position);
            PageUtils.gotoTopicPage(getContext(), data.getId(), position);
        }
    }

    @Override
    protected void onVisibleMaybeChange() {
        super.onVisibleMaybeChange();
        if (isVisibleToUser()) {
            if (mPuller == null) {
                mPuller = (TopicPuller) PullerHelper.get(PullerHelper.TYPE_TOPIC);
            }
            mList = mPuller.getData(mType);
            if (mList != null) {
                mAdapter = getListAdapter(mList);
                mRecyclerView.setAdapter(mAdapter);
            } else if (!mRefreshLayout.isRefreshing()) {
                doRefresh();
            }
        }
    }
}
