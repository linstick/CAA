package com.luoruiyong.caa.base;

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
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TopSmoothScroller;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public abstract class BaseSwipeFragment<Item> extends BaseFragment {

    private static final String TAG = "BaseSwipeFragment";
    public static final String KEY_TYPE = "key_type";
    public static final String KEY_OTHER_UID = "key_other_uid";
    public static final String KEY_KEYWORD = "key_keyword";

    private final int DEFAULT_LOAD_MORE_THRESHOLD = 10;
    protected final int DEFAULT_ITEM_MARGIN_PX = DisplayUtils.dp2px(10);

    protected View mRootView;
    protected TextView mTopTipTv;
    protected SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    protected List<Item> mList;
    protected RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    private boolean mNeedSetCanRefresh = false;
    private boolean mCanRefresh = true;
    protected boolean mIsLoadingMore = false;
    protected boolean mCanLoadMore = true;
    private int mLoadMoreThreshold = DEFAULT_LOAD_MORE_THRESHOLD;

    private List<String> mItemMoreStringArray;
    // 当前活动的类型，具体定义在其子类
    protected int mType;
    protected int mOtherUid;
    protected String mKeyword;

    private boolean mHasResume;
    private boolean mHasVisible;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(container.getContext()).inflate(R.layout.base_fragment_swipe_activity, container, false);

        initView(mRootView);

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHasResume = true;

        onVisibleMaybeChange();
    }

    private void initView(View rootView) {
        mTopTipTv = rootView.findViewById(R.id.tv_top_tip);
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mRecyclerView = rootView.findViewById(R.id.rv_recycler_view);

        setupRecyclerViewDivider();
        setUpErrorViewStub(rootView.findViewById(R.id.vs_error_view));
        setRefreshNeedHide(true);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        if (mNeedSetCanRefresh) {
            mRefreshLayout.setEnabled(mCanRefresh);
        }

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = mLayoutManager.findLastVisibleItemPosition();
                if (mCanLoadMore && !mIsLoadingMore && position + mLoadMoreThreshold > mList.size()) {
                    mIsLoadingMore = true;
                    doLoadMore();
                }
            }
        });
    }

    protected void onVisibleMaybeChange() {
        if (isVisibleToUser()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        } else {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            if (mTopTipTv != null && mTopTipTv.getVisibility() == View.VISIBLE) {
                mTopTipTv.setVisibility(View.GONE);
            }
            mIsLoadingMore = false;
        }
    }

    public void setCanRefresh(boolean canRefresh) {
        this.mCanRefresh = canRefresh;
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(mCanRefresh);
        } else {
            mNeedSetCanRefresh = true;
        }
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.mCanLoadMore = canLoadMore;

    }

    public void setLoadMoreThreshold(int threshold) {
        this.mLoadMoreThreshold = threshold;
    }


    protected void showTopTip(int count) {
        mTopTipTv.setText(String.format(getString(R.string.common_str_refresh_success_tip), count));
        mTopTipTv.setVisibility(View.VISIBLE);
        mTopTipTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTopTipTv.getContext() != null) {
                    mTopTipTv.setVisibility(View.GONE);
                }
            }
        }, 2000);
    }

    protected void onLoadMoreResult() {
        mIsLoadingMore = false;
    }

    protected void showMoreOperateDialog(final int itemPosition, long uid) {
        if (mItemMoreStringArray == null) {
            mItemMoreStringArray = Enviroment.getItemMoreNewStringArray();
        }
        List<String> items = new ArrayList<>();
        if (Enviroment.isSelf(uid)) {
            items.addAll(mItemMoreStringArray);
            items.add(ResourcesUtils.getString(R.string.common_str_delete));
        } else {
            items = mItemMoreStringArray;
        }
        DialogHelper.showListDialog(getContext(), items, new CommonDialog.Builder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                onMoreItemClick(itemPosition, position);
            }
        });
    }

    protected void onMoreItemClick(int itemPosition, int moreItemPosition) {
        switch (moreItemPosition) {
            case 0:
                // 举报
                PageUtils.gotoFeedbackPage(getContext(), (Serializable) mList.get(itemPosition));
                break;
            case 1:
                if (!ListUtils.isIndexBetween(mList, itemPosition)) {
                    return;
                }
                mList.remove(itemPosition);
                mAdapter.notifyDataSetChanged();
                Toast.makeText(getContext(), R.string.common_str_delete_success, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    protected void setupRecyclerViewDivider() {
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) != 0) {
                    outRect.top = DEFAULT_ITEM_MARGIN_PX;
                }
            }
        });
    }

    protected void handleScrollPosition(int position) {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                TopSmoothScroller scroller = new TopSmoothScroller(getContext());
                scroller.setTargetPosition(position);
                mRecyclerView.getLayoutManager().startSmoothScroll(scroller);
            }
        }, 200);
    }

    protected boolean isVisibleToUser() {
        return mHasVisible && mHasResume;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mHasVisible = isVisibleToUser;
        onVisibleMaybeChange();
    }

    protected abstract RecyclerView.Adapter getListAdapter(List<Item> list);

    protected void doRefresh() {

    }

    protected void doLoadMore() {

    }
}
