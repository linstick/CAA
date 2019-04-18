package com.luoruiyong.caa.search.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.model.ImageLoader;
import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserItemViewHolder> implements View.OnClickListener{

    private List<User> mList;
    private CompositeListAdapter.OnUserViewClickListener mListener;

    public UserListAdapter(List<User> list) {
        this.mList = list;
    }

    public void setOnUserViewClickListener(CompositeListAdapter.OnUserViewClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public UserItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false);
        return new UserItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemViewHolder holder, int position) {
        User data = mList.get(position);
        holder.bindData(data);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(data);
    }

    @Override
    public int getItemCount() {
        return ListUtils.getSize(mList);
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onUserItemClick((User) v.getTag());
        }
    }

    class UserItemViewHolder extends RecyclerView.ViewHolder {

        private SimpleDraweeView mBackgroundIv;
        private SimpleDraweeView mUserAvatarIv;
        private TextView mNicknameTv;
        private TextView mIntroduceTv;

        public UserItemViewHolder(View itemView) {
            super(itemView);
            mBackgroundIv = itemView.findViewById(R.id.iv_background);
            mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
            mNicknameTv = itemView.findViewById(R.id.tv_nickname);
            mIntroduceTv = itemView.findViewById(R.id.tv_introduction);
        }

        public void bindData(User user) {
            ImageLoader.showUrlBlur(mBackgroundIv, user.getAvatar(), 5, 10);
            mUserAvatarIv.setImageURI(user.getAvatar());
            mNicknameTv.setText(user.getNickname());
            mIntroduceTv.setText(user.getDescription());
        }
    }
}
