package com.luoruiyong.caa.common.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.common.adapter.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.edit.EditorActivity;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.model.CommonTargetOperator;
import com.luoruiyong.caa.model.bean.GlobalSource;
import com.luoruiyong.caa.model.puller.ActivityPuller;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeActivityFragment extends BaseSwipeFragment<ActivityData> {

    private static final String TAG = "SwipeActivityFragment";
    private static final int PAYLOAD_COLLECT = 0;

    public static SwipeActivityFragment newInstance(int activityType) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, activityType);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeActivityFragment newInstance(String keyword) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, Config.PAGE_ID_ACTIVITY_SEARCH);
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeActivityFragment newInstance(int type, int uid) {
        SwipeActivityFragment fm = new SwipeActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, type);
        bundle.putInt(KEY_OTHER_UID, uid);
        fm.setArguments(bundle);
        return fm;
    }

    public void setKeyword(String keyword) {
        mKeyword = keyword;
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
        if (bundle == null) {
            getActivity().finish();
            return;
        }
        mPageId = bundle.getInt(KEY_PAGE_ID, Config.PAGE_ID_ACTIVITY_ALL);
        if (mPageId == Config.PAGE_ID_ACTIVITY_SEARCH) {
            mKeyword = bundle.getString(KEY_KEYWORD);
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_OTHER_USER) {
            mOtherUid = bundle.getInt(KEY_OTHER_UID, -1);
        }
        if (mPageId > Config.PAGE_ID_ACTIVITY_ONE_KIND) {
            setCanPullRefresh(false);
        }
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<ActivityData> list) {
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

    private int getFirstId() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(0).getId();
        }
        return Config.DEFAULT_FIRST_OR_LAST_ID;
    }

    private int getLastId() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(mList.size() - 1).getId();
        }
        return Config.DEFAULT_FIRST_OR_LAST_ID;
    }

    @Override
    protected void onDeleteItem(int position) {
        CommonTargetOperator.doDeleteActivity(mList.get(position).getId());
    }

    private void doCollect(ActivityData data, int position) {
        // 收藏时先修改本地的数量，等请求结果回来时，如果收藏失败(几率比较小)，再把数据修改回来
        boolean isCollect = !data.isHasCollect();
        data.setHasCollect(isCollect);
        data.setCollectCount(data.getCollectCount() + (isCollect ? 1 : -1));
        mAdapter.notifyItemChanged(position, PAYLOAD_COLLECT);
        CommonTargetOperator.doCollectActivity(data.getId(), isCollect);
    }

    private void rollbackCollectData(int targetId) {
        int i = 0;
        for (ActivityData data : mList) {
            if (data.getId() == targetId) {
                boolean isCollect = !data.isHasCollect();
                data.setHasCollect(isCollect);
                data.setCollectCount(data.getCollectCount() + (isCollect ? 1 : -1));
                if (isItemVisible(i)) {
                    mAdapter.notifyItemChanged(i, PAYLOAD_COLLECT);
                }
                break;
            }
            i++;
        }
    }

    @Override
    protected void onRefreshClick() {
        if (mIsNoData && mPageId == Config.PAGE_ID_ACTIVITY_SELF) {
            EditorActivity.startAction(getContext(), EditorActivity.TAB_CREATE_ACTIVITY);
        } else {
            super.onRefreshClick();
        }
    }

    @Override
    protected void doRefresh() {
        LogUtils.d(TAG, "onRefreshClick: " + mPageId);
        mRefreshLayout.setRefreshing(true);
        if (mPageId == Config.PAGE_ID_ACTIVITY_ALL) {
            ActivityPuller.refreshAll(getFirstId());
        } else if (mPageId > 0 && mPageId < Config.PAGE_ID_ACTIVITY_ONE_KIND) {
            ActivityPuller.refreshOneKind(mPageId, getFirstId());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF) {
            ActivityPuller.refreshSelf(getFirstId());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_OTHER_USER) {
            ActivityPuller.refreshOtherUser(mOtherUid, getFirstId());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF_COLLECT) {
            ActivityPuller.refreshSelfCollect(getFirstItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SEARCH) {
            ActivityPuller.refreshSearch(mKeyword);
        } else {
            mRefreshLayout.setRefreshing(false);
            LogUtils.d(TAG, "Unknow type");
        }
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doLoadMore: " + mPageId);
        if (mAdapter instanceof LoadMoreSupportAdapter) {
            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_loading_more));
        }
        if (mPageId == Config.PAGE_ID_ACTIVITY_ALL) {
            ActivityPuller.loadMoreAll(getLastId());
        } else if (mPageId > 0 && mPageId < Config.PAGE_ID_ACTIVITY_ONE_KIND) {
            ActivityPuller.loadMoreOneKind(mPageId, getLastId());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF) {
            ActivityPuller.loadMoreSelf(getLastId());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_OTHER_USER) {
            ActivityPuller.loadMoreOtherUser(mOtherUid, getLastId());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF_COLLECT) {
            ActivityPuller.loadMoreSelfCollect(getLastItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SEARCH) {
            ActivityPuller.loadMoreSearch(mKeyword, ListUtils.getSize(mList));
        } else {
            mRefreshLayout.setRefreshing(false);
            LogUtils.d(TAG, "Unknow type");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonOperateEvent event) {
        switch (event.getType()) {
            case COLLECT_ACTIVITY:
                if (event.getCode() == Config.CODE_OK) {
                    // 收藏成功
                    // do nothing
                } else {
                    // 收藏失败，需要回滚收藏数据
                    int activity_id = event.getTargetId();
                    rollbackCollectData(activity_id);
                    toast(event.getStatus());
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonOperateEvent(CommonOperateEvent<ActivityData> event) {
        switch (event.getType()) {
            case DELETE_ACTIVITY:
                if (event.getCode() == Config.CODE_OK) {
                    if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                        GlobalSource.deleteActivityItemIfNeed(event.getData());
                    } else {
                        ListUtils.deleteActivityItem(mList, event.getData());
                    }
                    mAdapter.notifyDataSetChanged();
                    toast(R.string.common_str_delete_success);
                    if (ListUtils.isEmpty(mList)) {
                        mIsNoData = true;
                        showErrorView(Enviroment.getNoDataTipByPageId(mPageId), Enviroment.getNoDataOperateTipByPageId(mPageId));
                    }
                } else {
                    toast(event.getStatus());
                }
                break;
            default:
                break;
        }
    }

    private class ListAdapter extends LoadMoreSupportAdapter<ActivityData> implements View.OnClickListener, ImageViewLayout.OnImageClickListener{

        public ListAdapter(List<ActivityData> list) {
            super(list);
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, @NonNull List payloads) {
            if (ListUtils.isEmpty(payloads)) {
                super.onBindViewHolder(viewHolder, position);
                if (viewHolder instanceof ActivityItemViewHolder) {
                    ActivityItemViewHolder holder = (ActivityItemViewHolder) viewHolder;
                    ActivityData data = mList.get(position);
                    holder.bindData(data, mPageId);
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
            } else {
                int type = (int) payloads.get(0);
                switch (type) {
                    case PAYLOAD_COLLECT:
                        // 只刷新收藏控件
                        if (viewHolder instanceof ActivityItemViewHolder) {
                            ActivityData data = mList.get(position);
                            ActivityItemViewHolder holder = (ActivityItemViewHolder) viewHolder;
                            holder.mCollectTv.setText(data.getCollectCount() <= 0 ? ResourcesUtils.getString(R.string.common_str_collect) : String.valueOf(data.getCollectCount()));
                            holder.mCollectTv.setSelected(data.isHasCollect());
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            ActivityData data = mList.get(position);
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
                    if (!checkLoginIfNeed()) {
                        doCollect(data, position);
                    }
                    break;
                case R.id.tv_comment:
                    if (!checkLoginIfNeed()) {
                        PageUtils.gotoActivityDetailPage(getContext(), data, true);
                    }
                    break;
                case R.id.tv_more:
                    if (!checkLoginIfNeed()) {
                        showMoreOperateDialog(position, data.getUid());
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onImageClick(View parent, int position) {
            ActivityData data = (ActivityData) parent.getTag();
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, false);
        }
    }
}
