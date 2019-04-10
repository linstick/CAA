package com.luoruiyong.caa.common.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.puller.ActivityPuller;
import com.luoruiyong.caa.puller.PullerHelper;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_SUCCESS;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_SUCCESS;
import static com.luoruiyong.caa.puller.ActivityPuller.TYPE_ALL;
import static com.luoruiyong.caa.puller.ActivityPuller.TYPE_OTHER_USER;
import static com.luoruiyong.caa.puller.ActivityPuller.TYPE_SEARCH;
import static com.luoruiyong.caa.puller.ActivityPuller.TYPE_SELF;
import static com.luoruiyong.caa.puller.ActivityPuller.TYPE_SELF_COLLECT;
import static com.luoruiyong.caa.puller.IPuller.REFRESH_ACTIVITY_URL;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeActivityFragment extends BaseSwipeFragment<ActivitySimpleData> {

    private static final String TAG = "SwipeActivityFragment";

    private static final String KEY_TYPE = "key_type";
    private static final String KEY_OTHER_UID = "key_other_uid";
    private static final String KEY_KEYWORD = "key_keyword";

    private int mOtherUid;
    private String mKeyword;
    private ActivityPuller mPuller;

    public static SwipeActivityFragment newInstance() {
        return newInstance(TYPE_ALL);
    }

    public static SwipeActivityFragment newInstance(int activityType) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, activityType);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeActivityFragment newInstance(String keyword) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, TYPE_SEARCH);
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeActivityFragment newInstance(int type, int uid) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            getActivity().finish();
            return;
        }
        mType = bundle.getInt(KEY_TYPE, TYPE_ALL);
        if (mType == TYPE_SEARCH) {
            mKeyword = bundle.getString(KEY_KEYWORD);
        } else if (mType == TYPE_OTHER_USER) {
            mOtherUid = bundle.getInt(KEY_OTHER_UID, -1);
        }
        if (mType >= TYPE_SELF) {
            setCanRefresh(false);
        }
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<ActivitySimpleData> list) {
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
        Log.d(TAG, "doRefresh: " + mType);
        mRefreshLayout.setRefreshing(true);
        if (mType == TYPE_ALL) {
            mPuller.refreshAll(getFirstItemTime());
        } else if (mType > 0 && mType < TYPE_SELF) {
            mPuller.refreshOneKind(mType, getFirstItemTime());
        } else if (mType == TYPE_SELF) {
            mPuller.refreshSelf(getFirstItemTime());
        } else if (mType == TYPE_OTHER_USER) {
            mPuller.refreshOtherUser(mOtherUid, getFirstItemTime());
        } else if (mType == TYPE_SELF_COLLECT) {
            mPuller.refreshSelfCollect(getFirstItemTime());
        } else if (mType == TYPE_SEARCH) {
            mPuller.refreshSearch(mKeyword);
        } else {
            mRefreshLayout.setRefreshing(false);
            LogUtils.d(TAG, "Unknow type");
        }
    }

    @Override
    protected void doLoadMore() {
        Log.d(TAG, "doLoadMore: " + mType);
        ((ListAdapter)mAdapter).showLoadMoreTip(getString(R.string.common_str_loading_more));
        if (mType == TYPE_ALL) {
            mPuller.loadMoreAll(getLastItemTime());
        } else if (mType > 0 && mType < TYPE_SELF) {
            mPuller.loadMoreOneKind(mType, getLastItemTime());
        } else if (mType == TYPE_SELF) {
            mPuller.loadMoreSelf(getLastItemTime());
        } else if (mType == TYPE_OTHER_USER) {
            mPuller.loadMoreOtherUser(mOtherUid, getLastItemTime());
        } else if (mType == TYPE_SELF_COLLECT) {
            mPuller.loadMoreSelfCollect(getLastItemTime());
        } else if (mType == TYPE_SEARCH) {
            mPuller.loadMoreSearch(mKeyword,getLastItemTime());
        } else {
            LogUtils.d(TAG, "Unknow type");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPullFinishEvent(PullFinishEvent event) {
        switch (event.getType()) {
            case TYPE_REFRESH_FAIL:
                mRefreshLayout.setRefreshing(false);
                if (ListUtils.isEmpty(mList)) {
                    showErrorView((String) event.getData());
                }
                break;
            case TYPE_REFRESH_SUCCESS:
                mRefreshLayout.setRefreshing(false);
                if (mList == null) {
                    // 首次拉取数据成功，设置到列表中
                    mList = mPuller.getData(mType);
                    mAdapter = getListAdapter(mList);
                    mRecyclerView.setAdapter(mAdapter);
                } else if (mType >= 0 && mType < TYPE_SELF) {
                    // 非首次根据条件是否展示更新成功提示
                    showTopTip((Integer) event.getData());
                }
                mAdapter.notifyDataSetChanged();
                break;
            case TYPE_LOAD_MORE_FAIL:
                mIsLoadingMore = false;
                mCanLoadMore = false;
                ((ListAdapter) mAdapter).showLoadMoreTip(getString(R.string.common_str_load_fail));
                mAdapter.notifyDataSetChanged();
                break;
            case TYPE_LOAD_MORE_SUCCESS:
                mIsLoadingMore = false;
                mCanLoadMore = true;
                ((ListAdapter) mAdapter).showLoadMoreTip(getString(R.string.common_str_load_finish));
                mAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private class ListAdapter extends LoadMoreSupportAdapter implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        private List<ActivitySimpleData> mList;

        public ListAdapter(List<ActivitySimpleData> list) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity_list, parent, false);
            return new ActivityItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof ActivityItemViewHolder) {
                ActivityItemViewHolder holder = (ActivityItemViewHolder) viewHolder;
                ActivitySimpleData data = mList.get(position);
                holder.bindData(data, mType);
                holder.itemView.setOnClickListener(this);
                holder.mUserAvatarIv.setOnClickListener(this);
                holder.mNicknameTv.setOnClickListener(this);
                holder.mTopicTv.setOnClickListener(this);
                holder.mCollectTv.setOnClickListener(this);
                holder.mCommentTv.setOnClickListener(this);
                holder.mMoreIv.setOnClickListener(this);
                holder.mImageViewLayout.setOnImageClickListener(this);
                holder.itemView.setTag(position);
                holder.mUserAvatarIv.setTag(position);
                holder.mNicknameTv.setTag(position);
                holder.mTopicTv.setTag(position);
                holder.mCollectTv.setTag(position);
                holder.mCommentTv.setTag(position);
                holder.mMoreIv.setTag(position);
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
            int position = (int) v.getTag();
            ActivitySimpleData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    PageUtils.gotoActivityDetailPage(getContext(), data);
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    PageUtils.gotoUserProfilePage(getContext(), data.getUid());
                    break;
                case R.id.tv_topic:
                   PageUtils.gotoTopicPage(getContext(), data.getTopicId());
                    break;
                case R.id.tv_collect:
                    Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_comment:
                    PageUtils.gotoActivityDetailPage(getContext(), data, true);
                    break;
                case R.id.tv_more:
                    showMoreOperateDialog(position, data.getUid());
                    break;
                case R.id.tv_load_more_tip:
                    if (!mIsLoadingMore) {
                        setLoadMoreTip(getString(R.string.common_str_loading_more));
                        mIsLoadingMore = true;
                        doLoadMore();
                    }
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

    @Override
    protected void onVisibleMaybeChange() {
        super.onVisibleMaybeChange();
        if (isVisibleToUser()) {
            if (mPuller == null) {
                mPuller = (ActivityPuller) PullerHelper.get(PullerHelper.TYPE_ACTIVITY);
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
