package com.luoruiyong.caa.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.model.puller.CommonPuller;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.utils.TimeUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class CommentFragment extends BaseSwipeFragment<CommentData> {
    private static final String TAG = "CommentFragment";

    private int mTargetId;

    public static CommentFragment newInstance(int type, int id) {
        CommentFragment fm = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, type);
        bundle.putInt(KEY_TARGET_ID, id);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCanPullRefresh(false);
        handleArguments();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.setBackgroundColor(ResourcesUtils.getColor(R.color.white));
        return view;
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mPageId = bundle.getInt(KEY_PAGE_ID, Config.PAGE_ID_ACTIVITY_COMMENT);
        mTargetId = bundle.getInt(KEY_TARGET_ID, -1);
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<CommentData> list) {
        return new ListAdapter(mList);
    }

    @Override
    protected void setupRecyclerViewDivider() {
    }

    private String getLastItemTime() {
        if (!ListUtils.isEmpty(mList)) {
            return mList.get(mList.size() - 1).getPublishTime();
        }
        return Config.DEFAULT_TIME_STAMP;
    }

    @Override
    protected void doRefresh() {
        mRefreshLayout.setRefreshing(true);
        if (mPageId == Config.PAGE_ID_ACTIVITY_COMMENT) {
            CommonPuller.refreshActivityComment(mTargetId);
        } else if (mPageId == Config.PAGE_ID_DISCOVER_COMMENT){
            CommonPuller.refreshDiscoverComment(mTargetId);
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
        if (mPageId == Config.PAGE_ID_ACTIVITY_COMMENT) {
            CommonPuller.loadMoreActivityComment(mTargetId, getLastItemTime());
        } else if (mPageId == Config.PAGE_ID_DISCOVER_COMMENT){
            CommonPuller.loadMoreDiscoverComment(mTargetId, getLastItemTime());
        } else {
            LogUtils.d(TAG, "Unknow type");
        }
    }

    class ListAdapter extends LoadMoreSupportAdapter<CommentData> implements View.OnClickListener {


        public ListAdapter(List<CommentData> list) {
            super(list);
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == ITEM_TYPE_LOAD_MORE_TIP) {
                return super.onCreateViewHolder(parent, viewType);
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                CommentData data = mList.get(position);
                holder.bindData(data);

                holder.mUserAvatarIv.setOnClickListener(this);
                holder.mNicknameTv.setOnClickListener(this);
                holder.mUserAvatarIv.setTag(position);
                holder.mNicknameTv.setTag(position);
                holder.itemView.setTag(position);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int itemPosition = (int) v.getTag();
                        CommentData comment = mList.get(itemPosition);
                        showMoreOperateDialog(itemPosition, comment.getUid());
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            CommentData data = mList.get(position);
            switch (v.getId()) {
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    PageUtils.gotoUserProfilePage(getContext(), data.getUid());
                    break;
                default:
                    break;
            }
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private SimpleDraweeView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mPublishTimeTv;
            private TextView mContentTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
                mNicknameTv = itemView.findViewById(R.id.tv_nickname);
                mPublishTimeTv = itemView.findViewById(R.id.tv_publish_time);
                mContentTv = itemView.findViewById(R.id.tv_content);
            }

            public void bindData(CommentData data) {
                mUserAvatarIv.setImageURI(data.getAvatarUrl());
                mNicknameTv.setText(data.getNickname());
                mPublishTimeTv.setText(TimeUtils.format(data.getPublishTime()));
                mContentTv.setText(data.getContent());
            }
        }
    }
}
