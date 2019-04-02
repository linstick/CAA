package com.luoruiyong.caa.common.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.TagSimpleData;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TagInnerItemContainer;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class SwipeTagFragment extends BaseSwipeFragment<TagSimpleData> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for test
        for (int i = 0; i < 20 ; i++) {
            mList.add(new TagSimpleData(i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.top = DEFAULT_ITEM_MARGIN_PX;
                if (parent.getChildAdapterPosition(view) == ListUtils.getSize(mList) - 1) {
                    outRect.bottom = DEFAULT_ITEM_MARGIN_PX;
                }
            }
        });
        return view;
    }

    @Override
    protected void initListAdapter(List<TagSimpleData> list) {
        mAdapter = new ListAdapter(list);
    }

    @Override
    protected void doRefresh() {
        Toast.makeText(getContext(), "doRefresh", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doLoadMore() {
        Toast.makeText(getContext(), "doLoadMore", Toast.LENGTH_SHORT).show();
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener, TagInnerItemContainer.OnItemClickListener{

        private List<TagSimpleData> mList;

        public ListAdapter(List<TagSimpleData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            TagSimpleData data = mList.get(position);
            holder.bindData(data);

            holder.itemView.setOnClickListener(this);
            holder.mMoreIv.setOnClickListener(this);
            holder.mInnerContainerLayout.setOnItemClickListener(this);
            holder.itemView.setTag(position);
            holder.mInnerContainerLayout.setTag(position);
            holder.mMoreIv.setTag(position);
        }

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TagSimpleData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
//                    Toast.makeText(getContext(), "item layout click", Toast.LENGTH_SHORT).show();
                    getContext().startActivity(new Intent(getContext(), TopicActivity.class));
                    break;
                case R.id.iv_more:
                    showMoreOperateDialog(position, data.getUid());
//                    Toast.makeText(getContext(), "more click", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClick(View view, int position) {
            TagSimpleData data = (TagSimpleData) view.getTag();
//            Toast.makeText(getContext(), "inner item click " + position, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getContext(), TopicActivity.class));
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mTagCoverIv;
            private TextView mTagNameTv;
            private TextView mVisitedCountTv;
            private TextView mJoinedCountTv;
            private ImageView mMoreIv;
            private View mHeaderDividerView;
            private TagInnerItemContainer mInnerContainerLayout;

            public ViewHolder(View itemView) {
                super(itemView);
                mTagCoverIv = itemView.findViewById(R.id.iv_tag_cover);
                mTagNameTv = itemView.findViewById(R.id.tv_tag_name);
                mVisitedCountTv = itemView.findViewById(R.id.tv_visit_count);
                mJoinedCountTv = itemView.findViewById(R.id.tv_join_count);
                mMoreIv = itemView.findViewById(R.id.iv_more);
                mHeaderDividerView = itemView.findViewById(R.id.view_header_divider);
                mInnerContainerLayout = itemView.findViewById(R.id.inner_container_layout);
            }

            public void bindData(TagSimpleData data) {
//                mTagCoverIv.setImageUrl(data.getCoverUrl());
                mTagNameTv.setText(data.getName());
                mVisitedCountTv.setText(data.getVisitedCount() + "");
                mJoinedCountTv.setText(data.getJoinCount() + "");
                mHeaderDividerView.setVisibility(ListUtils.isEmpty(data.getDiscoverList()) ? View.GONE : View.VISIBLE);
                mInnerContainerLayout.setItems(data.getDiscoverList());
            }
        }
    }
}
