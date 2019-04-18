package com.luoruiyong.caa.common.fragment;

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
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.model.puller.ActivityPuller;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;


import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeActivityFragment extends BaseSwipeFragment<ActivityData> {

    private static final String TAG = "SwipeActivityFragment";

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

    @Override
    protected void doRefresh() {
        LogUtils.d(TAG, "doRefreshClick: " + mPageId);
        mRefreshLayout.setRefreshing(true);
        if (mPageId == Config.PAGE_ID_ACTIVITY_ALL) {
            ActivityPuller.refreshAll(getFirstItemTime());
        } else if (mPageId > 0 && mPageId < Config.PAGE_ID_ACTIVITY_ONE_KIND) {
            ActivityPuller.refreshOneKind(mPageId, getFirstItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF) {
            ActivityPuller.refreshSelf(getFirstItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_OTHER_USER) {
            ActivityPuller.refreshOtherUser(mOtherUid, getFirstItemTime());
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
            ActivityPuller.loadMoreAll(getLastItemTime());
        } else if (mPageId > 0 && mPageId < Config.PAGE_ID_ACTIVITY_ONE_KIND) {
            ActivityPuller.loadMoreOneKind(mPageId, getLastItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF) {
            ActivityPuller.loadMoreSelf(getLastItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_OTHER_USER) {
            ActivityPuller.loadMoreOtherUser(mOtherUid, getLastItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SELF_COLLECT) {
            ActivityPuller.loadMoreSelfCollect(getLastItemTime());
        } else if (mPageId == Config.PAGE_ID_ACTIVITY_SEARCH) {
            ActivityPuller.loadMoreSearch(mKeyword, ListUtils.getSize(mList));
        } else {
            mRefreshLayout.setRefreshing(false);
            LogUtils.d(TAG, "Unknow type");
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
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
                    Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_comment:
                    PageUtils.gotoActivityDetailPage(getContext(), data, true);
                    break;
                case R.id.tv_more:
                    showMoreOperateDialog(position, data.getUid());
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
