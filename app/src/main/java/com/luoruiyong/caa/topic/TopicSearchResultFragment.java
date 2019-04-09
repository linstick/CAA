package com.luoruiyong.caa.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.TagSimpleData;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description:
 **/
public class TopicSearchResultFragment extends BaseSwipeFragment<TagSimpleData> {

    private TopicSearchActivity mActivity;

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

        // for test
        for (int i = 0; i < 30; i++) {
            mList.add(new TagSimpleData(i));
        }
    }

    public void searchQuietly(String text) {
        Toast.makeText(getContext(), "do search quietly", Toast.LENGTH_SHORT).show();
    }

    public void doSearch(String text) {
        mRefreshLayout.setRefreshing(true);
        ((ListAdapter) mAdapter).mShowCreateTopic = false;
        mList.clear();
        mAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "do search", Toast.LENGTH_SHORT).show();

        // for test
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                for (int i = 0; i < 30; i++) {
                    mList.add(new TagSimpleData(i));
                }
                ((ListAdapter) mAdapter).mShowCreateTopic = true;
                mAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    protected void initListAdapter(List list) {
        mAdapter = new ListAdapter(list);
    }

    @Override
    protected void setupRecyclerViewDivider() {
        // do nothing
    }

    @Override
    protected void doLoadMore() {

    }

    private class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener{

        private final static int TYPE_NO_TOPIC = 0;
        private final static int TYPE_SEARCH_TOPIC = 1;
        private final static int TYPE_CREATE_TOPIC = 2;

        private List<TagSimpleData> mList;
        private boolean mShowCreateTopic = true;

        public ListAdapter(List<TagSimpleData> list) {
            this.mList = list;
        }

        public void setShowCreateTopic(boolean show) {
            mShowCreateTopic = show;
            notifyDataSetChanged();
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
                TagSimpleData data = mList.get(realPos);
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
            return 1 + (mShowCreateTopic ? ListUtils.getSize(mList) + 1 : ListUtils.getSize(mList));
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
                    startActivity(new Intent(getContext(), TopicActivity.class));
                    break;
                case R.id.ll_create_topic_item_layout:
                    if (mActivity != null) {
                        mActivity.setCreateResultDataAndFinish();
                    }
                    break;
                case R.id.ll_not_relate_topic_item_layout:
                    if (mActivity != null) {
                        mActivity.setNotRelateResultDataAndFinish();
                    }
                    break;
                case R.id.tv_choose:
                    if (mActivity != null) {
                        TagSimpleData data = mList.get((int) v.getTag());
                        mActivity.setSelectResultDataAndFinish(data);
                    }
                    break;
                default:
                    break;
            }
        }

        class TopicViewHolder extends RecyclerView.ViewHolder {

            private TextView mTopicTv;
            private TextView mLabelTv;
            private TextView mSelectTv;

            public TopicViewHolder(View itemView) {
                super(itemView);
                mTopicTv = itemView.findViewById(R.id.tv_topic);
                mLabelTv = itemView.findViewById(R.id.tv_label);
                mSelectTv = itemView.findViewById(R.id.tv_choose);
            }

            public void bindData(TagSimpleData data) {
                mTopicTv.setText(String.format(getString(R.string.common_str_topic), data.getName()));
                mLabelTv.setText(String.format(getString(R.string.topic_relate_str_join_count), data.getJoinCount()));
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
