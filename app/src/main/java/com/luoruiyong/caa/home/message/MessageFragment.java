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

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.MessageData;

import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.login.LoginActivity;
import com.luoruiyong.caa.model.CommonTargetOperator;
import com.luoruiyong.caa.model.bean.GlobalSource;
import com.luoruiyong.caa.model.puller.CommonPuller;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.utils.TimeUtils;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_ID;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_DISCOVER_ID;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class MessageFragment extends BaseSwipeFragment<MessageData> {

    private static final String TAG = "MessageFragment";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageId = Config.PAGE_ID_MESSAGE;
    }

    @Override
    public void onResume() {
        // 顺序不能变，未登录的用户切换到消息页不主动刷新
        checkUserState();
        super.onResume();
    }

    @Override
    protected void onDeleteItem(int position) {
        CommonTargetOperator.doDeleteMessage(mList.get(position).getId());
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
        hideTipView();
        mRefreshLayout.setRefreshing(true);
        CommonPuller.refreshMessage(getFirstItemTime());
    }

    @Override
    protected void onRefreshClick() {
        if (Enviroment.isVisitor()) {
            LoginActivity.startAction(getContext(), LoginActivity.LOGIN_TAB);
        } else {
            doRefresh();
        }
    }

    @Override
    protected void doLoadMore() {
        LogUtils.d(TAG, "doLoadMore: " + mPageId);
        if (mAdapter instanceof LoadMoreSupportAdapter) {
            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_loading_more));
        }
        CommonPuller.loadMoreMessage(getLastItemTime());
    }

    private void gotoSrcDetailPage(int messageType, int id) {
        switch (messageType) {
            case 0:
            case 1:
            case 5:
                PageUtils.gotoActivityDetailPage(getContext(), DETAIL_TYPE_ACTIVITY_ID, id);
                break;
            case 2:
            case 6:
                PageUtils.gotoTopicPage(getContext(), id);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonOperateEvent(CommonOperateEvent<MessageData> event) {
        switch (event.getType()) {
            case DELETE_MESSAGE:
                if (event.getCode() == Config.CODE_OK) {
                    if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                        GlobalSource.deleteMessageItemDataIfNeed(event.getData());
                    } else {
                        ListUtils.deleteMessageItem(mList, event.getData());
                    }
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), R.string.common_str_delete_success, Toast.LENGTH_SHORT).show();
                    if (ListUtils.isEmpty(mList)) {
                        showErrorView(R.drawable.bg_load_fail, getString(R.string.common_tip_no_related_content));
                    }
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private class ListAdapter extends LoadMoreSupportAdapter<MessageData> implements View.OnClickListener{

        public ListAdapter(List<MessageData> list) {
            super(list);
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, List payloads) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof ViewHolder) {
                ViewHolder holder = (ViewHolder) viewHolder;
                MessageData data = mList.get(position);
                holder.bindData(data);
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        DialogHelper.showListDialog(getContext(), getString(R.string.common_str_delete), new CommonDialog.Builder.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                CommonTargetOperator.doDeleteMessage(mList.get(position).getId());
                            }
                        });
                        return true;
                    }
                });
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
        public void onClick(View v) {
            int position = (int) v.getTag();
            MessageData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                case R.id.ll_src_layout:
                    gotoSrcDetailPage(data.getType(), data.getTargetId());
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

            private SimpleDraweeView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mPublishTimeTv;
            private TextView mMessageTypeTv;
            private TextView mContentTv;

            private View mSrcLayout;
            private SimpleDraweeView mSrcCoverIv;
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
                mUserAvatarIv.setImageURI(data.getAvatarUrl());
                mNicknameTv.setText(data.getNickname());
                mMessageTypeTv.setText(Enviroment.getMessageTypeNameById(data.getType()));
                mPublishTimeTv.setText(TimeUtils.format(data.getPublishTime()));
                if (!TextUtils.isEmpty(data.getContent())) {
                    mContentTv.setVisibility(View.VISIBLE);
                    mContentTv.setText(data.getContent());
                }

                if (!TextUtils.isEmpty(data.getTargetCoverUrl())) {
                    mSrcCoverIv.setVisibility(View.VISIBLE);
                    mSrcCoverIv.setImageURI(data.getTargetCoverUrl());
                }

                if (!TextUtils.isEmpty(data.getTargetTitle())) {
                    mSrcTitleTv.setVisibility(View.VISIBLE);
                    mSrcTitleTv.setText(data.getTargetTitle());
                }

                if (!TextUtils.isEmpty(data.getTargetContent())) {
                    mSrcContentTv.setVisibility(View.VISIBLE);
                    mSrcContentTv.setText(data.getTargetContent());
                }
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (Enviroment.isVisitor()) {
            setAutoHideError(false);
        } else {
            setAutoHideError(true);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    public void checkUserState() {
        if (Enviroment.isVisitor()) {
            setAutoRefreshForEmpty(false);
            showErrorView(R.drawable.ic_user_default_avatar_light_gray_60, ResourcesUtils.getString(R.string.fm_login_str_not_login), ResourcesUtils.getString(R.string.fm_login_str_goto_login));
        } else {
            setAutoRefreshForEmpty(true);
        }
    }
}
