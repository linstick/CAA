package com.luoruiyong.caa.topic;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description:
 **/
public class TopicSearchResultFragment extends BaseFragment {

    private TopicSearchActivity mActivity;

    private List<TopicData> mList;
    private ListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    private String mLastKeyword;   // 字符增加导致的加载
    private String mSearchKeyword; // 点击搜索按钮导致的加载,优先

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TopicSearchActivity) {
            mActivity = (TopicSearchActivity) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_search_topic, container, false);

        mRecyclerView = view.findViewById(R.id.rv_recycler_view);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        setUpErrorViewStub(view.findViewById(R.id.vs_error_view));

        mAdapter = new ListAdapter(mList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setEnabled(false);
        searchQuietly(null);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void searchQuietly(String text) {
        if ((!TextUtils.isEmpty(text) && TextUtils.equals(mLastKeyword, text)) || mRefreshLayout.isRefreshing()) {
            return;
        }
        hideTipView();
        mAdapter.setShowCreateTopic(false);
        mAdapter.notifyDataSetChanged();
        mSearchKeyword = null;
        if (TextUtils.isEmpty(text)) {
            mLastKeyword = null;
            CommonFetcher.doFetchHotSimpleTopicList(Config.DEFAULT_TOPIC_SEARCH_TIP_REQUEST_COUNT);
        } else {
            mLastKeyword = text;
            CommonFetcher.doFetchSimpleTopicList(text, Config.DEFAULT_TOPIC_SEARCH_TIP_REQUEST_COUNT);
        }
    }

    public void doSearch(String text) {
        if (mRefreshLayout.isRefreshing()) {
            return;
        }
        mRefreshLayout.setRefreshing(true);
        mList.clear();
        mAdapter.setShowCreateTopic(false);
        mAdapter.setShowNotRelatedTopic(false);
        mAdapter.notifyDataSetChanged();
        mSearchKeyword = text;
        CommonFetcher.doFetchSimpleTopicList(text, Config.DEFAULT_TOPIC_SEARCH_REQUEST_COUNT);
    }


    @Override
    protected void onRefreshClick() {
        if (mActivity != null) {
            mActivity.setCreateResultDataAndFinish(mSearchKeyword != null ? mSearchKeyword : mLastKeyword);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent event) {
        switch (event.getType()) {
            case FETCH_HOT_SIMPLE_TOPIC_LIST:
                if (TextUtils.isEmpty(mActivity.getInputText()) && TextUtils.isEmpty(mLastKeyword) && TextUtils.isEmpty(mSearchKeyword)) {
                    mList.clear();
                    if (event.getCode() == Config.CODE_OK) {
                        mList.addAll((Collection<? extends TopicData>) event.getData());
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case FETCH_SIMPLE_TOPIC_LIST:
                if (!TextUtils.isEmpty(mSearchKeyword)) {
                    // 点击搜索结果或搜索之后下滑回调
                    mRefreshLayout.setRefreshing(false);
                    if (Config.CODE_OK == event.getCode()) {
                        List<TopicData> list = (List<TopicData>)event.getData();
                        if (!TextUtils.equals(list.get(0).getName(), mSearchKeyword)) {
                            mAdapter.setShowCreateTopic(true);
                        }
                        mList.addAll((List<TopicData>) event.getData());
                        mAdapter.setShowNotRelatedTopic(true);
                        mAdapter.notifyDataSetChanged();
                    } else if (Config.CODE_OK_BUT_EMPTY == event.getCode()) {
                        if (ListUtils.isEmpty(mList)) {
                            showErrorView(R.drawable.bg_load_fail, event.getStatus(), String.format(getString(R.string.topic_relate_str_create_topic), mSearchKeyword));
                        }
                    }
                } else {
                    // 输入删除结果回调
                    if (TextUtils.equals(mLastKeyword, mActivity.getInputText())) {
                        mList.clear();
                        if (Config.CODE_OK == event.getCode()) {
                            List<TopicData> list = (List<TopicData>)event.getData();
                            if (!TextUtils.equals(list.get(0).getName(), mLastKeyword)) {
                                mAdapter.setShowCreateTopic(true);
                            }
                            mList.addAll((List<TopicData>) event.getData());
                        } else if (Config.CODE_OK_BUT_EMPTY == event.getCode()) {
                            mAdapter.setShowCreateTopic(true);
                        }
                        mAdapter.setShowNotRelatedTopic(true);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

        private final static int TYPE_NO_TOPIC = 0;
        private final static int TYPE_SEARCH_TOPIC = 1;
        private final static int TYPE_CREATE_TOPIC = 2;

        private List<TopicData> mList;
        private boolean mShowCreateTopic = false;
        private boolean mShowNotRelatedTopic = true;

        public ListAdapter(List<TopicData> list) {
            this.mList = list;
        }

        public void setShowCreateTopic(boolean show) {
            mShowCreateTopic = show;
        }

        public void setShowNotRelatedTopic(boolean show) {
            mShowNotRelatedTopic = show;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            RecyclerView.ViewHolder holder = null;
            switch (viewType) {
                case TYPE_NO_TOPIC:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_not_relate_topic, parent, false);
                    holder = new NoTopicViewHolder(view);
                    break;
                case TYPE_SEARCH_TOPIC:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_topic, parent, false);
                    holder = new TopicViewHolder(view);
                    break;
                case TYPE_CREATE_TOPIC:
                    view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_create_topic, parent, false);
                    holder = new CreateViewHolder(view);
                    break;
                default:
                    break;
            }
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof NoTopicViewHolder) {
                ((NoTopicViewHolder) holder).itemView.setOnClickListener(this);
            } else if (holder instanceof TopicViewHolder) {
                int realPos = position - 1;
                TopicData data = mList.get(realPos);
                TopicViewHolder viewHolder = (TopicViewHolder) holder;
                viewHolder.bindData(data);
                viewHolder.itemView.setOnClickListener(this);
                viewHolder.mSelectTv.setOnClickListener(this);
                viewHolder.itemView.setTag(realPos);
                viewHolder.mSelectTv.setTag(realPos);
            } else if (holder instanceof CreateViewHolder) {
                CreateViewHolder viewHolder = (CreateViewHolder) holder;
                viewHolder.mCreateTv.setText(String.format(getString(R.string.topic_relate_str_create_topic), mActivity.getInputText()));
                viewHolder.itemView.setOnClickListener(this);
            }
        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList) + (mShowNotRelatedTopic ? 1 : 0) + (mShowCreateTopic ? 1 : 0);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == 0) {
                return TYPE_NO_TOPIC;
            }

            if (ListUtils.isIndexBetween(mList, position - 1)) {
                return TYPE_SEARCH_TOPIC;
            }

            return TYPE_CREATE_TOPIC;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ll_search_topic_item_layout:
                    PageUtils.gotoTopicPage(getContext(), mList.get((Integer) v.getTag()).getId());
                    break;
                case R.id.ll_create_topic_item_layout:
                    if (mActivity != null) {
                        mActivity.setCreateResultDataAndFinish(mSearchKeyword != null ? mSearchKeyword : mLastKeyword);
                    }
                    break;
                case R.id.ll_not_relate_topic_item_layout:
                    if (mActivity != null) {
                        mActivity.setNotRelateResultDataAndFinish();
                    }
                    break;
                case R.id.tv_choose:
                    if (mActivity != null) {
                        TopicData data = mList.get((int) v.getTag());
                        mActivity.setSelectResultDataAndFinish(data);
                    }
                    break;
                default:
                    break;
            }
        }

        class TopicViewHolder extends RecyclerView.ViewHolder {

            private TextView mTopicTv;
            private TextView mJoinTv;
            private TextView mVisitTv;
            private TextView mSelectTv;

            public TopicViewHolder(View itemView) {
                super(itemView);
                mTopicTv = itemView.findViewById(R.id.tv_topic);
                mJoinTv = itemView.findViewById(R.id.tv_join);
                mVisitTv = itemView.findViewById(R.id.tv_visit);
                mSelectTv = itemView.findViewById(R.id.tv_choose);
            }

            public void bindData(TopicData data) {
                mTopicTv.setText(String.format(getString(R.string.common_str_topic), data.getName()));
                mJoinTv.setText(String.format(getString(R.string.common_str_join_count), data.getJoinCount()));
                mVisitTv.setText(String.format(getString(R.string.common_str_visit_count), data.getVisitedCount()));
            }
        }

        class CreateViewHolder extends RecyclerView.ViewHolder {

            private TextView mCreateTv;

            public CreateViewHolder(View itemView) {
                super(itemView);
                mCreateTv = itemView.findViewById(R.id.tv_create_topic);
            }
        }

        class NoTopicViewHolder extends RecyclerView.ViewHolder {

            public NoTopicViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
