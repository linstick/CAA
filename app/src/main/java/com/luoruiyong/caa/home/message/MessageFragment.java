package com.luoruiyong.caa.home.message;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.MessageData;

import com.luoruiyong.caa.common.fragment.SwipeTopicFragment;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.puller.MessagePuller;
import com.luoruiyong.caa.puller.PullerHelper;
import com.luoruiyong.caa.puller.TopicPuller;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TopSmoothScroller;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_LOAD_MORE_SUCCESS;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_FAIL;
import static com.luoruiyong.caa.eventbus.PullFinishEvent.TYPE_REFRESH_SUCCESS;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_ID;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_ID;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class MessageFragment extends BaseSwipeFragment<MessageData> {

    private static final String TAG = "MessageFragment";

    private MessagePuller mPuller;

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
        mPuller.refresh(getFirstItemTime());
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doLoadMore: " + mType);
        ((ListAdapter)mAdapter).showLoadMoreTip(getString(R.string.common_str_loading_more));
        mPuller.loadMore(getLastItemTime());
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
                    mList = mPuller.getData();
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

    private void gotoSrcDetailPage(int messageType, long id) {
        switch (messageType) {
            case 0:
            case 1:
            case 5:
                PageUtils.gotoActivityDetailPage(getContext(), DETAIL_TYPE_ACTIVITY_ID, id);
                break;
            case 2:
            case 6:
                PageUtils.gotoTopicPage(getContext(), (int) id);
                break;
            case 3:
            case 4:
                PageUtils.gotoActivityDetailPage(getContext(), DETAIL_TYPE_DISCOVER_ID, id);
                break;
            default:
                break;
        }
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<MessageData> list) {
        return new ListAdapter(list);
    }

    private class ListAdapter extends LoadMoreSupportAdapter implements View.OnClickListener{

        private List<MessageData> mList;

        public ListAdapter(List<MessageData> list) {
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                MessageData data = mList.get(position);
                holder.bindData(data);

                holder.itemView.setOnClickListener(this);
                holder.mSrcLayout.setOnClickListener(this);
                holder.mUserAvatarIv.setOnClickListener(this);
                holder.mNicknameTv.setOnClickListener(this);
                holder.itemView.setTag(position);
                holder.mSrcLayout.setTag(position);
                holder.mUserAvatarIv.setTag(position);
                holder.mNicknameTv.setTag(position);
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
            MessageData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                case R.id.ll_src_layout:
                    gotoSrcDetailPage(data.getType(), data.getSrcId());
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    int uid = data.getUid();
                    PageUtils.gotoUserProfilePage(getContext(), uid);
                    break;
                default:
                    break;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mPublishTimeTv;
            private TextView mMessageTypeTv;
            private TextView mContentTv;

            private View mSrcLayout;
            private ImageView mSrcCoverIv;
            private TextView mSrcTitleTv;
            private TextView mSrcContentTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
                mNicknameTv = itemView.findViewById(R.id.tv_nickname);
                mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
                mMessageTypeTv = itemView.findViewById(R.id.tv_message_type);
                mContentTv = itemView.findViewById(R.id.tv_content);

                mSrcLayout = itemView.findViewById(R.id.ll_src_layout);
                mSrcCoverIv = itemView.findViewById(R.id.iv_src_cover);
                mSrcTitleTv = itemView.findViewById(R.id.tv_src_title);
                mSrcContentTv = itemView.findViewById(R.id.tv_src_content);
            }

            public void bindData(MessageData data) {
//                mUserAvatarIv.setImageUrl(data.getAvatarUrl());
                mNicknameTv.setText(data.getNickname());
                mMessageTypeTv.setText(Enviroment.getMessageTypeNameById(data.getType()));
                if (!TextUtils.isEmpty(data.getContent())) {
                    mContentTv.setVisibility(View.VISIBLE);
                    mContentTv.setText(data.getContent());
                }

                if (!TextUtils.isEmpty(data.getSrcCoverUrl())) {
                    mSrcCoverIv.setVisibility(View.VISIBLE);
//                    mSrcCoverIv.setImageUrl(data.getSrcCoverUrl());
                }

                if (!TextUtils.isEmpty(data.getSrcTitle())) {
                    mSrcTitleTv.setVisibility(View.VISIBLE);
                    mSrcTitleTv.setText(data.getSrcTitle());
                }

                if (!TextUtils.isEmpty(data.getSrcContent())) {
                    mSrcContentTv.setVisibility(View.VISIBLE);
                    mSrcContentTv.setText(data.getSrcContent());
                }
            }
        }
    }

    @Override
    protected void onVisibleMaybeChange() {
        super.onVisibleMaybeChange();
        if (isVisibleToUser()) {
            if (mPuller == null) {
                mPuller = (MessagePuller) PullerHelper.get(PullerHelper.TYPE_MESSAGE);
            }
            mList = mPuller.getData();
            if (mList != null) {
                mAdapter = getListAdapter(mList);
                mRecyclerView.setAdapter(mAdapter);
            } else if (!mRefreshLayout.isRefreshing()) {
                doRefresh();
            }
        }
    }
}
