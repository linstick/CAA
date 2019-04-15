package com.luoruiyong.caa.search;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.search.adapter.CompositeListAdapter;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/9/009
 * Description:
 **/
public class CompositeFragment extends BaseFragment {

    private final int DEFAULT_ITEM_MARGIN_PX = DisplayUtils.dp2px(10);
    public final static int MAX_ITEM_COUNT_OF_ONE_TYPE = 10;

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private CompositeListAdapter mAdapter;

    private String mKeyword;
    private List<User> mUserList;
    private List<ActivityData> mActivityList;
    private List<TopicData> mTopicList;
    private List<DiscoverData> mDiscoverList;
    private List<String> mItemMoreStringArray;

    private OnMoreViewClickListener mMoreClickListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMoreViewClickListener) {
            mMoreClickListener = (OnMoreViewClickListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_composite, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.rv_recycler_view);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setEnabled(false);
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = DEFAULT_ITEM_MARGIN_PX;
                }
                if (parent.getChildAdapterPosition(view) == mAdapter.getItemCount() - 1) {
                    outRect.bottom = DEFAULT_ITEM_MARGIN_PX;
                }
            }
        });

        mUserList = new ArrayList<>();
        mActivityList = new ArrayList<>();
        mTopicList = new ArrayList<>();
        mDiscoverList = new ArrayList<>();

        mAdapter = new CompositeListAdapter(mUserList, mActivityList, mTopicList, mDiscoverList);
        mAdapter.setOnUserViewListener(new OnUserViewClickListenerImpl());
        mAdapter.setOnActivityViewClickListener(new OnActivityViewClickListenerImpl());
        mAdapter.setOnTopicViewClickListener(new OnTopicViewClickListenerImpl());
        mAdapter.setOnDiscoverViewClickListener(new OnDiscoverViewClickListener());
        mAdapter.setOnMoreViewClickListener(new OnMoreViewClickListenerImpl());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        setUpErrorViewStub(view.findViewById(R.id.vs_error_view));
        setRefreshNeedHide(true);
    }

    public void doSearch(String keyword) {
        mKeyword = keyword;
        ListUtils.clear(mUserList, mActivityList, mDiscoverList, mTopicList);
        mRefreshLayout.setRefreshing(true);
        mAdapter.notifyDataSetChanged();

        // for test
        mRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < MAX_ITEM_COUNT_OF_ONE_TYPE; i++) {
                    mUserList.add(new User());
                    mActivityList.add(new ActivityData(i, (int) (Math.random() * 6) + 1));
                    mTopicList.add(new TopicData(i));
                    mDiscoverList.add(new DiscoverData(i));
                }
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }

    protected void showMoreOperateDialog(final Serializable data) {
        if (mItemMoreStringArray == null) {
            mItemMoreStringArray = Enviroment.getItemMoreNewStringArray();
        }
        DialogHelper.showListDialog(getContext(), mItemMoreStringArray, new CommonDialog.Builder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onMoreItemClick(data, position);
            }
        });
    }

    protected void onMoreItemClick(Serializable data, int position) {
        switch (position) {
            case 0:
                // 举报
                PageUtils.gotoFeedbackPage(getContext(), data);
                break;
            default:
                break;
        }
    }

    public class OnUserViewClickListenerImpl implements CompositeListAdapter.OnUserViewClickListener {
        @Override
        public void onUserItemClick(User user) {
            PageUtils.gotoUserProfilePage(getContext(), user.getUid());
        }
    }

    public class OnActivityViewClickListenerImpl implements CompositeListAdapter.OnActivityViewClickListener {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            ActivityData data = mActivityList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    PageUtils.gotoActivityDetailPage(getContext(), data);
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    PageUtils.gotoUserProfilePage(getContext(), data.getUid());
                    break;
                case R.id.tv_topic:
                    PageUtils.gotoTopicPage(getContext(), data.getTopicId());
                    break;
                case R.id.tv_collect:
                    Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_comment:
                    PageUtils.gotoActivityDetailPage(getContext(), data, true);
                    break;
                case R.id.tv_more:
                    showMoreOperateDialog(mActivityList.get(position));
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onImageClick(View parent, int position) {
            ActivityData data = (ActivityData) parent.getTag();
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, false);
        }
    }

    public class OnTopicViewClickListenerImpl implements CompositeListAdapter.OnTopicViewClickListener {

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            TopicData data = mTopicList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    PageUtils.gotoTopicPage(getContext(), data.getId());
                    break;
                case R.id.iv_more:
                    showMoreOperateDialog(data);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onItemClick(int parentPosition, int position) {
            TopicData data = mTopicList.get(parentPosition);
            PageUtils.gotoTopicPage(getContext(), data, position);
        }
    }

    public class OnDiscoverViewClickListener implements CompositeListAdapter.OnDiscoverViewClickListener{

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            DiscoverData data = mDiscoverList.get(position);
            switch (v.getId()) {
                case R.id.ll_item_layout:
                    PageUtils.gotoActivityDetailPage(getContext(), data);
                    break;
                case R.id.iv_user_avatar:
                case R.id.tv_nickname:
                    PageUtils.gotoUserProfilePage(getContext(), data.getUid());
                    break;
                case R.id.iv_more:
                    showMoreOperateDialog(data);
                    break;
                case R.id.tv_topic:
                    PageUtils.gotoTopicPage(getContext(), data.getTopicId());
                    break;
                case R.id.tv_like:
                    Toast.makeText(getContext(), "click collect", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_comment:
                    PageUtils.gotoActivityDetailPage(getContext(), data, true);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onImageClick(View parent, int position) {
            DiscoverData data = (DiscoverData) parent.getTag();
            PictureBrowseActivity.startAction(getActivity(), data.getPictureList(), position, false);
        }
    }

    class OnMoreViewClickListenerImpl implements CompositeListAdapter.OnMoreViewClickListener {
        @Override
        public void onUserMoreClick() {
            if (mMoreClickListener != null) {
                mMoreClickListener.onUserMoreClick(mKeyword);
            }
        }

        @Override
        public void onActivityMoreClick() {
            if (mMoreClickListener != null) {
                mMoreClickListener.onActivityMoreClick(mKeyword);
            }
        }

        @Override
        public void onTopicMoreClick() {
            if (mMoreClickListener != null) {
                mMoreClickListener.onTopicMoreClick(mKeyword);
            }
        }

        @Override
        public void onDiscoverMoreClick() {
            if (mMoreClickListener != null) {
                mMoreClickListener.onDiscoverMoreClick(mKeyword);
            }
        }
    }

    public interface OnMoreViewClickListener {
        void onUserMoreClick(String keyword);
        void onActivityMoreClick(String keyword);
        void onTopicMoreClick(String keyword);
        void onDiscoverMoreClick(String keyword);
    }
}
