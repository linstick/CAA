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
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.base.LoadMoreSupportAdapter;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.MessageData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.common.viewholder.TopicItemViewHolder;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.model.CommonTargetOperator;
import com.luoruiyong.caa.model.bean.GlobalSource;
import com.luoruiyong.caa.model.puller.TopicPuller;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeTopicFragment extends BaseSwipeFragment<TopicData> {

    private static final String TAG = "SwipeTopicFragment";

    public static SwipeTopicFragment newInstance() {
        return newInstance(Config.PAGE_ID_TOPIC_ALL);
    }

    public static SwipeTopicFragment newInstance(int type) {
        SwipeTopicFragment fm = new SwipeTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, type);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeTopicFragment newInstance(String keyword) {
        SwipeTopicFragment fm = new SwipeTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, Config.PAGE_ID_TOPIC_SEARCH);
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    public static SwipeTopicFragment newInstance(int type, int uid) {
        SwipeTopicFragment fm = new SwipeTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PAGE_ID, type);
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
    public void onResume() {
        super.onResume();
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            getActivity().finish();
            return;
        }
        mPageId = bundle.getInt(KEY_PAGE_ID, Config.PAGE_ID_TOPIC_ALL);
        if (mPageId == Config.PAGE_ID_TOPIC_SEARCH) {
            mKeyword = bundle.getString(KEY_KEYWORD);
        } else if (mPageId == Config.PAGE_ID_TOPIC_OTHER_USER) {
            mOtherUid = bundle.getInt(KEY_OTHER_UID, -1);
        }
        if (mPageId != Config.PAGE_ID_TOPIC_ALL) {
            setCanPullRefresh(false);
        }
    }

    @Override
    protected void onDeleteItem(int position) {
        CommonTargetOperator.doDeleteTopic(mList.get(position).getId());
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List<TopicData> list) {
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
            case Config.PAGE_ID_TOPIC_ALL:
                TopicPuller.refreshAll(getFirstItemTime());
                break;
            case Config.PAGE_ID_TOPIC_SELF:
                TopicPuller.refreshSelf(getFirstItemTime());
                break;
            case Config.PAGE_ID_TOPIC_OTHER_USER:
                TopicPuller.refreshOtherUser(mOtherUid, getFirstItemTime());
                break;
            case Config.PAGE_ID_TOPIC_SEARCH:
                TopicPuller.refreshSearch(mKeyword);
                break;
            default:
                LogUtils.d(TAG, "refresh unknow type");
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
            case Config.PAGE_ID_TOPIC_ALL:
                TopicPuller.loadMoreAll(getLastItemTime());
                break;
            case Config.PAGE_ID_TOPIC_SELF:
                TopicPuller.loadMoreSelf(getLastItemTime());
                break;
            case Config.PAGE_ID_TOPIC_OTHER_USER:
                TopicPuller.loadMoreOtherUser(mOtherUid, getLastItemTime());
                break;
            case Config.PAGE_ID_TOPIC_SEARCH:
                TopicPuller.loadMoreSearch(mKeyword, ListUtils.getSize(mList));
                break;
            default:
                LogUtils.d(TAG, "load more unknow type");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonOperateEvent(CommonOperateEvent<TopicData> event) {
        switch (event.getType()) {
            case DELETE_TOPIC:
                if (event.getCode() == Config.CODE_OK) {
                    if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                        GlobalSource.deleteTopicItemDataIfNeed(event.getData());
                    }  else {
                        ListUtils.deleteTopicItem(mList, event.getData());
                    }
                    Toast.makeText(getContext(), R.string.common_str_delete_success, Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
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

    private class ListAdapter extends LoadMoreSupportAdapter<TopicData> implements View.OnClickListener, TagInnerItemContainer.OnItemClickListener{


        public ListAdapter(List<TopicData> list) {
            super(list);
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
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position, List payloads) {
            super.onBindViewHolder(viewHolder, position);
            if (viewHolder instanceof TopicItemViewHolder) {
                TopicItemViewHolder holder = (TopicItemViewHolder) viewHolder;
                TopicData data = mList.get(position);
                holder.bindData(data);

                holder.itemView.setOnClickListener(this);
                holder.mMoreIv.setOnClickListener(this);
                holder.mInnerContainerLayout.setOnItemClickListener(position,this);
                holder.itemView.setTag(position);
                holder.mInnerContainerLayout.setTag(position);
                holder.mMoreIv.setTag(position);
            }
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TopicData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    PageUtils.gotoTopicPage(getContext(), data);
                    break;
                case R.id.iv_more:
                    if (Enviroment.isVisitor()) {
                        Toast.makeText(getContext(), R.string.fm_login_tip_login_before, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showMoreOperateDialog(position, data.getUid());
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClick(int parentPosition, int position) {
            // position 为点击的子列表中的位置
            TopicData data = mList.get(parentPosition);
            PageUtils.gotoTopicPage(getContext(), data, position);
        }
    }
}
