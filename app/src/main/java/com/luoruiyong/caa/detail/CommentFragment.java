package com.luoruiyong.caa.detail;

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
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class CommentFragment extends BaseSwipeFragment<CommentData> {

    private static final String KEY_TYPE = "key_type";
    public static final int TYPE_ACTIVITY_COMMENT = 0;
    public static final int TYPE_DISCOVER_COMMENT = 1;

    private int mType;

    public static CommentFragment newInstance(int type) {
        CommentFragment fm = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleArguments();
        // for test
        for (int i = 0; i < 30; i++) {
            mList.add(new CommentData(i));
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = DisplayUtils.dp2px(1);
                }
            }
        });
        return view;
    }

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mType = bundle.getInt(KEY_TYPE, TYPE_ACTIVITY_COMMENT);
    }

    @Override
    protected void initListAdapter(List<CommentData> list) {
        mAdapter = new ListAdapter(mList);
    }

    @Override
    protected void setupRecyclerViewDivider() {
    }

    @Override
    protected void doRefresh() {
        Toast.makeText(getContext(), "doRefresh: type = " + mType, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doLoadMore() {
        Toast.makeText(getContext(), "doLoadMore: type = " + mType, Toast.LENGTH_SHORT).show();
    }

    class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener {

        private List<CommentData> mList;

        public ListAdapter(List<CommentData> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_comment_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
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

            private ImageView mUserAvatarIv;
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
//                mUserAvatarIv.setImageUrl(data.getAvatarUrl());
                mNicknameTv.setText(data.getNickname());
//                mPublishTimeTv.setText(data.getPublishTime() + "");
                mContentTv.setText(data.getContent());
            }
        }
    }
}
