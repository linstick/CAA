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

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseSwipeFragment;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.model.puller.CommonPuller;
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


    public static SwipeUserFragment newInstance(String keyword) {
        SwipeUserFragment fm = new SwipeUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_KEYWORD, keyword);
        fm.setArguments(bundle);
        return fm;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageId = Config.PAGE_ID_USER_SEARCH;
        setCanPullRefresh(false);
        handleArguments();
    }

    public void handleArguments() {
        Bundle bundle = getArguments();
        mKeyword = bundle.getString(KEY_KEYWORD);
    }

    @Override
    protected RecyclerView.Adapter getListAdapter(List list) {
        return new ListAdapter(list);
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

    @Override
    protected void doRefresh() {
        mRefreshLayout.setRefreshing(true);
        CommonPuller.refreshUserSearch(mKeyword);
    }

    @Override
    protected void doLoadMore() {
        CommonPuller.loadMoreUserSearch(mKeyword, ListUtils.getSize(mList));
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

            private SimpleDraweeView mUserAvatarIv;
            private TextView mNicknameTv;
            private TextView mIntroduceTv;

            public ViewHolder(View itemView) {
                super(itemView);
                mUserAvatarIv = itemView.findViewById(R.id.iv_user_avatar);
                mNicknameTv = itemView.findViewById(R.id.tv_nickname);
                mIntroduceTv = itemView.findViewById(R.id.tv_introduction);
            }

            public void bindData(User user) {
                mUserAvatarIv.setImageURI(user.getAvatar());
                mNicknameTv.setText(user.getNickname());
                mIntroduceTv.setText(user.getDescription());
            }
        }
    }
}
