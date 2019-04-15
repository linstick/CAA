package com.luoruiyong.caa.base;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.model.bean.GlobalSource;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.TopSmoothScroller;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public abstract class BaseSwipeFragment<Item> extends BaseFragment {

    private static final String TAG = "BaseSwipeFragment";
    public static final String KEY_PAGE_ID = "key_page_id";
    public static final String KEY_OTHER_UID = "key_other_uid";
    public static final String KEY_KEYWORD = "key_keyword";
    public static final String KEY_TARGET_ID = "key_target_id";
    public static final String KEY_TARGET_UID = "key_uid";
    public static final String KEY_POSITION = "key_position";

    private final int DEFAULT_LOAD_MORE_THRESHOLD = 10;
    protected final int DEFAULT_ITEM_MARGIN_PX = DisplayUtils.dp2px(10);

    protected View mRootView;
    protected TextView mTopTipTv;
    protected SwipeRefreshLayout mRefreshLayout;
    protected RecyclerView mRecyclerView;

    protected List<Item> mList;
    protected RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    /**
     * 配置项
     */
    // 首次进入是否自动刷新
    private boolean mAutoRefreshForEmpty = true;
    // 页面不可见时是否隐藏错误提示框
    private boolean mAutoHideError = true;
    // 是否支持下拉更新
    private boolean mSupportRefresh = true;
    // 是否支持自动加载更多
    protected boolean mAutoLoadMore = true;
    // 拉取数据成功之后是否更新到全局数据列表中
    protected boolean mNeedUpdateGlobal = false;
    // 自动预加载更多的阈值
    private int mLoadMoreThreshold = DEFAULT_LOAD_MORE_THRESHOLD;

    /**
     * 状态项
     */
    private boolean mNeedSetCanRefresh = false;
    // 是否正在加载更多
    protected boolean mIsLoadingMore = false;
    // 是否已经加载更多失败
    protected boolean mCanLoadMore = true;
    protected boolean mHasVisible;
    protected boolean mHasResume;

    private List<String> mItemMoreStringArray;
    // 当前活动的类型，具体定义在其子类
    protected int mPageId;
    protected int mOtherUid;
    protected String mKeyword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = LayoutInflater.from(container.getContext()).inflate(R.layout.base_fragment_swipe_activity, container, false);

        initView(mRootView);

        return mRootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mHasResume = true;
        checkRefreshIfNeed();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHasResume = false;
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initView(View rootView) {
        mTopTipTv = rootView.findViewById(R.id.tv_top_tip);
        mRefreshLayout = rootView.findViewById(R.id.refresh_layout);
        mRecyclerView = rootView.findViewById(R.id.rv_recycler_view);

        setupRecyclerViewDivider();
        setUpErrorView(rootView.findViewById(R.id.tv_error_view));
        setRefreshNeedHide(true);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        if (mNeedSetCanRefresh) {
            mRefreshLayout.setEnabled(mSupportRefresh);
        }
        mRecyclerView.setAdapter(mAdapter);

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
                if (mAutoLoadMore && mCanLoadMore && !mIsLoadingMore) {
                    int position = mLayoutManager.findLastVisibleItemPosition();
                    if (position + mLoadMoreThreshold > mList.size()) {
                        mIsLoadingMore = true;
                        if (mAdapter instanceof LoadMoreSupportAdapter) {
                            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_loading_more));
                        }
                        doLoadMore();
                    }
                }
            }
        });
    }

    protected boolean isVisibleToUser() {
        return mHasVisible && mHasResume;
    }

    private void checkRefreshIfNeed() {
        if (ListUtils.isEmpty(mList)) {
            if (mAdapter != null) {
                // 全局数据已清空，需要清理Adapter
                mRecyclerView.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
                mAdapter = null;
            }
            if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                // 获取全局数据
                mList = GlobalSource.getData(mPageId);
            }
            initAdapterIfNeed();
            if (mAutoRefreshForEmpty && mList == null && !mRefreshLayout.isRefreshing()) {
                doRefresh();
            }
        }
    }

    public void setAutoRefreshForEmpty(boolean autoRefresh) {
        mAutoRefreshForEmpty = autoRefresh;
    }

    public void setAutoHideError(boolean autoHideError) {
        mAutoHideError = autoHideError;
    }

    /**
     * 设置是否能够下拉刷新
     * @param canPullRefresh
     */
    public void setCanPullRefresh(boolean canPullRefresh) {
        this.mSupportRefresh = canPullRefresh;
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(mSupportRefresh);
        } else {
            mNeedSetCanRefresh = true;
        }
    }

    /**
     * 加载更多失败时需要将值设置为false，默认为true
     * @param autoLoadMore
     */
    public void setCanLoadMore(boolean autoLoadMore) {
        this.mCanLoadMore = autoLoadMore;

    }

    /**
     * 是否自动加载更多，默认为true
     * @param autoLoadMore
     */
    public void setAutoLoadMore(boolean autoLoadMore) {
        this.mAutoLoadMore = autoLoadMore;
    }

    /**
     * 设置上拉预加载更多的阈值
     * @param threshold
     */
    public void setLoadMoreThreshold(int threshold) {
        this.mLoadMoreThreshold = threshold;
    }

    /**
     * 刷新成功提示，部分页面不需要展示
     * @param count
     */
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

    /**
     * 展示更多对话框
     * @param itemPosition
     * @param uid
     */
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

    /**
     * Item中的更多按钮点击事件统一处理
     * @param itemPosition
     * @param moreItemPosition
     */
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

    /**
     * 设置Item的间距，默认是非第一个Item顶部留出10dp的间隙
     */
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

    /**
     * 滚动到指定Item
     * @param position
     */
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mHasVisible = isVisibleToUser;
        if (!isVisibleToUser()) {
            if (mRefreshLayout != null && mRefreshLayout.isRefreshing()) {
                mRefreshLayout.setRefreshing(false);
            }
            mIsLoadingMore = false;
            if (mAutoHideError) {
                hideTipView();
            }
        }
    }

    protected void initAdapterIfNeed() {
        if (!ListUtils.isEmpty(mList) && mAdapter == null) {
            mAdapter = getListAdapter(mList);
            mRecyclerView.setAdapter(mAdapter);
            if (mAdapter instanceof LoadMoreSupportAdapter) {
                ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTipClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLoadMoreTipClick();
                    }
                });
            }
        } else if (mRecyclerView.getAdapter() != mAdapter) {
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onRefreshClick() {
        doRefresh();
    }

    /**
     * EventBus数据拉取完成事件回调
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPullFinishEvent(PullFinishEvent event) {
        if (event.getTargetPage() != mPageId) {
            // 不是当前页的数据请求结果，不处理
            return;
        }
        switch (event.getPullType()) {
            case Config.PULL_TYPE_REFRESH:
                if (event.getCode() == Config.CODE_OK) {
                    onRefreshSuccess((List<Item>) event.getData());
                } else {
                    onRefreshFail(event.getStatus());
                }
                break;
            case Config.PULL_TYPE_LOAD_MORE:
                if (event.getCode() == Config.CODE_OK) {
                    onLoadMoreSuccess((List<Item>) event.getData());
                } else {
                    onLoadMoreFail(event.getStatus());
                }
                break;
            default:
                break;
        }
    }


    /**
     * 获取用户自定义的适配器，必须实现并且返回的值非空
     * @param list
     * @return
     */
    protected abstract @NonNull RecyclerView.Adapter getListAdapter(List<Item> list);

    /**
     * 刷新回调
     * 1. 下拉刷新
     * 2. 首次进入无数据时自动刷新
     */
    protected void doRefresh() {

    }

    /**
     * 上拉加载更多回调
     * 默认达到一定的阈值会自动加载更多
     */
    protected void doLoadMore() {

    }


    /**
     * 点击加载更多提示时回调
     */
    protected void onLoadMoreTipClick() {
        if (!mIsLoadingMore) {
            if (mAdapter instanceof LoadMoreSupportAdapter) {
                ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_loading_more));
            }
            mIsLoadingMore = true;
            doLoadMore();
        }
        return;
    }


    /**
     * 刷新失败回调
     * @param error
     */
    protected void onRefreshFail(String error) {
        mRefreshLayout.setRefreshing(false);
        if (TextUtils.equals(error, ResourcesUtils.getString(R.string.common_tip_no_network))) {
            if (ListUtils.isEmpty(mList)) {
                showErrorView(R.drawable.bg_no_network, error);
            } else {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (ListUtils.isEmpty(mList)) {
                showErrorView(R.drawable.bg_load_fail, error);
            } else {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        }
        LogUtils.d(TAG, "onRefreshFail: " + error);
    }

    /**
     * 刷新成功回调
     * @param result
     */
    protected void onRefreshSuccess(List<Item> result) {
        mRefreshLayout.setRefreshing(false);
        // 某些数据全局缓存

        if (mList == null) {
            // 首次拉取数据成功，设置到列表中
            mList = result;
            initAdapterIfNeed();
            if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                // 添加并关联全局数据
                GlobalSource.appendData(mPageId, result);
            }
        } else {
            // 非首次根据条件是否展示更新成功提示
            if (mSupportRefresh && isVisibleToUser()) {
                showTopTip(ListUtils.getSize(result));
            }
            if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                GlobalSource.appendData(mPageId, result);
            } else {
                // 非全局数据，自己添加
                mList.addAll(0, result);
            }
        }

        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "onRefreshSuccess: " + result);
    }

    /**
     * 加载更多失败回调
     * @param error
     */
    protected void onLoadMoreFail(String error) {
        mIsLoadingMore = false;
        mCanLoadMore = false;
        if (mAdapter instanceof LoadMoreSupportAdapter) {
            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_load_fail));
        }
        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "load more fail: " + error);
    }

    /**
     * 加载更多成功回调
     * @param result
     */
    protected void onLoadMoreSuccess(List<Item> result) {
        mIsLoadingMore = false;
        mCanLoadMore = true;
        if (mList == null) {
            mList = result;
            initAdapterIfNeed();
            if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                GlobalSource.appendData(mPageId, result);
            }
        } else {
            mList.addAll(result);
        }
        if (mAdapter instanceof LoadMoreSupportAdapter) {
            ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_load_finish));
        }
        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "load more success: " + result);
    }



}
