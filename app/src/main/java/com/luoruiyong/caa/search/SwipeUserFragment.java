package com.luoruiyong.caa.search;

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

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class SwipeUserFragment extends BaseSwipeFragment<User> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        for (int i = 0; i < 30; i++) {
            mList.add(new User());
        }
    }

    @Override
    protected void initListAdapter(List list) {
        mAdapter = new ListAdapter(list);
    }

    @Override
    protected void setupRecyclerViewDivider() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = DisplayUtils.dp2px(1);
                }
            }
        });
    }

    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> implements View.OnClickListener{

        private List<User> mList;

        public ListAdapter(List<User> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_user_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            User data = mList.get(position);
            holder.bindData(data);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setTag(data.getUid());

        }

        @Override
        public int getItemCount() {
            return ListUtils.getSize(mList);
        }

        @Override
        public void onClick(View v) {
            PageUtils.gotoUserProfilePage(getContext(), (Integer) v.getTag());
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private ImageView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mIntroduceTv;

            public ViewHolder(View itemView) {
                super(itemView);

                mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
                mNicknameTv = itemView.findViewById(R.id.tv_nickname);
                mIntroduceTv = itemView.findViewById(R.id.tv_introduction);
            }

            public void bindData(User user) {
//            mUserAvatarIv.setImageUrl(user.getAvatar());
                mNicknameTv.setText(user.getNickName());
                mIntroduceTv.setText(user.getDescription());
            }
        }
    }
}
