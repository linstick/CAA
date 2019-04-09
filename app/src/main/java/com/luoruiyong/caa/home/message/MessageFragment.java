package com.luoruiyong.caa.home.message;

import android.content.Intent;
import android.graphics.Rect;
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
import com.luoruiyong.caa.bean.MessageData;

import com.luoruiyong.caa.detail.DetailActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class MessageFragment extends BaseSwipeFragment<MessageData> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // for test
        for (int i = 0; i < 20 ; i++) {
            mList.add(new MessageData(i));
        }
    }

    private void gotoSrcDetailPage(int messageType) {
        switch (messageType) {
            case 0:
            case 1:
            case 5:
                startActivity(new Intent(getContext(), DetailActivity.class));
                break;
            case 2:
            case 6:
                startActivity(new Intent(getContext(), TopicActivity.class));
                break;
            case 3:
            case 4:
                startActivity(new Intent(getContext(), DetailActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    protected void initListAdapter(List<MessageData> list) {
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

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener{

        private List<MessageData> mList;

        public ListAdapter(List<MessageData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

        @Override
        public int getItemCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            MessageData data = mList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                case R.id.ll_src_layout:
                    gotoSrcDetailPage(data.getType());
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    long uid = data.getUid();
                    startActivity(new Intent(getContext(), UserProfileActivity.class));
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
}
