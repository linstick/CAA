package com.luoruiyong.caa.base;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public abstract class BaseSwipeFragment<Item> extends Fragment {

    private final int DEFAULT_LOAD_MORE_THRESHOLD = 5;
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
    private boolean mCanLoadMore = true;
    private boolean mIsLoadingMore = false;
    private boolean mHasMore = true;
    private int mLoadMoreThreshold = DEFAULT_LOAD_MORE_THRESHOLD;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = new ArrayList<>();
        initListAdapter(mList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(container.getContext()).inflate(R.layout.base_fragment_swipe_activity, container, false);

        initView(mRootView);

        return mRootView;
    }

    private void initView(View rootView) {
        mTopTipTv = rootView.findViewById(R.id.tv_top_tip);
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mRecyclerView = rootView.findViewById(R.id.rv_recycler_view);

        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
                mRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onRefreshResult();
                    }
                }, 2000);
            }
        });
        if (mNeedSetCanRefresh) {
            mRefreshLayout.setEnabled(mCanRefresh);
        }

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = mLayoutManager.findLastVisibleItemPosition();
                if (!mIsLoadingMore && mHasMore && position + mLoadMoreThreshold > mList.size()) {
                    mIsLoadingMore = true;
                    doLoadMore();
                }
            }
        });
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


    protected void onRefreshResult() {
        mRefreshLayout.setRefreshing(false);
        mTopTipTv.setVisibility(View.VISIBLE);
        mTopTipTv.setText("Update ten new activities");
        mTopTipTv.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTopTipTv.setVisibility(View.GONE);
            }
        }, 2000);
    }

    protected void onLoadMoreResult() {
        mIsLoadingMore = false;
    }

    protected abstract void initListAdapter(List<Item> list);

    protected abstract void doRefresh();

    protected abstract void doLoadMore();

}
