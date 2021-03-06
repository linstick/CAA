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

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.common.adapter.LoadMoreSupportAdapter;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.login.LoginActivity;
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
    private boolean mAutoHideError = false;
    // 是否支持下拉更新
    private boolean mSupportRefresh = true;
    // 是否支持自动加载更多
    protected boolean mAutoLoadMore = true;
    // 自动预加载更多的阈值
    private int mLoadMoreThreshold = DEFAULT_LOAD_MORE_THRESHOLD;
    // Item是否支持举报
    protected boolean mSupportImpeach = true;

    /**
     * 状态项
     */
    private boolean mNeedSetCanRefresh = false;
    // 是否正在加载更多
    protected boolean mIsLoadingMore = false;
    // 是否可以有更多的数据可加载
    // 允许条件(OR)：
    // 1. 首次刷新完成并且刷新的列表数量等于请求数量
    // 2. 上拉自动加载更多完成时拉取到的列表数量等于请求数量
    private boolean mHasMore = false;
    // 是否可以加载更多，
    // 当数据加载更多失败是会置为false，网络原因或者服务器异常
    // 需要用户手动点击才会重新获取数据
    private boolean mCanLoadMore = true;
    // 列表所需要的数据是否为空，当加载数据请求成功但数据为空时置为ture
    protected boolean mIsNoData = false;

    // 页面是否对用户可见
    protected boolean mHasVisible;
    protected boolean mHasResume;

    // 当前活动的类型，具体定义在其子类
    protected int mPageId = Config.PAGE_ID_NONE;
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
        tryUpdateUI();
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
                if (mHasMore && mCanLoadMore && mAutoLoadMore && !mIsLoadingMore) {
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
        //恢复提示控件
        if (mHasTipViewShow) {
            showErrorView();
        }
    }

    protected boolean isVisibleToUser() {
        return mHasVisible && mHasResume;
    }

    protected void checkRefreshIfNeed() {
        if (ListUtils.isEmpty(mList)) {
            if (mAdapter != null) {
                // 全局数据已清空，需要清理Adapter
                mRecyclerView.scrollToPosition(0);
                mAdapter.notifyDataSetChanged();
                mAdapter = null;
            }
            if (mPageId > Config.PAGE_ID_NONE && mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                // 获取全局数据
                mList = GlobalSource.getData(mPageId);
            }
            initAdapterIfNeed();
            if (!mIsNoData && mAutoRefreshForEmpty && mList == null && !mRefreshLayout.isRefreshing()) {
                hideTipView();
                doRefresh();
            }
        }
    }

    private void tryUpdateUI() {
        if (mLayoutManager == null || mAdapter == null) {
            return;
        }
        int first = mLayoutManager.findFirstVisibleItemPosition();
        int last = mLayoutManager.findLastVisibleItemPosition();
        mAdapter.notifyItemRangeChanged(first, last);
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

    public void setSupportImpeach(boolean supportImpeach) {
        this.mSupportImpeach = supportImpeach;
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
    protected void showMoreOperateDialog(final int itemPosition, int uid) {
        List<String> items = new ArrayList<>();
        if (mSupportImpeach) {
            items.add(getString(R.string.common_str_impeach));
        }
        if (Enviroment.isSelf(uid)) {
            items.add(ResourcesUtils.getString(R.string.common_str_delete));
        }
        if (ListUtils.isEmpty(items)) {
            Toast.makeText(getContext(), R.string.common_tip_no_operation, Toast.LENGTH_SHORT).show();
            return;
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
    private void onMoreItemClick(int itemPosition, int moreItemPosition) {
        if (mSupportImpeach) {
            switch (moreItemPosition) {
                case 0:
                    // 举报
                    PageUtils.gotoFeedbackPage(getContext(), (Serializable) mList.get(itemPosition));
                    break;
                case 1:
                    onDeleteItem(itemPosition);
                    break;
                default:
                    break;
            }
        } else {
            switch (moreItemPosition) {
                case 0:
                    onDeleteItem(itemPosition);
                    break;
                default:
                    break;
            }
        }
    }

    protected void onDeleteItem(int position) {

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

    protected boolean isItemVisible(int position) {
        return mLayoutManager.findFirstVisibleItemPosition() <= position && mLayoutManager.findLastVisibleItemPosition() >= position;
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
        hideTipView();
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
                    onRefreshFail(event);
                }
                break;
            case Config.PULL_TYPE_LOAD_MORE:
                if (event.getCode() == Config.CODE_OK) {
                    onLoadMoreSuccess((List<Item>) event.getData());
                } else {
                    onLoadMoreFail(event);
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
        if (!mIsLoadingMore && mHasMore) {
            mCanLoadMore = true;
            mIsLoadingMore = true;
            doLoadMore();
        }
        return;
    }

    /**
     * 刷新失败回调
     * @param event
     */
    protected void onRefreshFail(PullFinishEvent event) {
        mRefreshLayout.setRefreshing(false);
        int code = event.getCode();
        String error = event.getStatus();
        switch (code) {
            case Config.CODE_OK_BUT_EMPTY:
                if (ListUtils.isEmpty(mList)) {
                    // 第一次刷新时请求成功，但无数据
                    mIsNoData = true;
                    showErrorView(Enviroment.getNoDataTipByPageId(mPageId), Enviroment.getNoDataOperateTipByPageId(mPageId));
                } else {
                    // 非第一次刷新请求成功，但无数据
                    Toast.makeText(getContext(), getString(R.string.common_tip_no_more_new_data), Toast.LENGTH_SHORT).show();
                }
                break;
            case Config.CODE_REQUEST_ERROR:
                if (ListUtils.isEmpty(mList)) {
                    showErrorView(R.drawable.bg_no_network, error);
                } else {
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;
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
            if (mList.size() == Config.DEFAULT_REQUEST_COUNT) {
                // 第一次刷新完成时的更新数量等于请求数量，说明还有可能有数据可以加载
                mHasMore = true;
            } else {
                // 没有更多的数据了
                if (mAdapter instanceof  LoadMoreSupportAdapter) {
                    ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_no_more));
                }
            }
        } else {
            // 非首次根据条件是否展示更新成功提示
            if (mSupportRefresh && isVisibleToUser()) {
                showTopTip(ListUtils.getSize(result));
            }
            if (mPageId <= Config.MAX_GLOBAL_CACHE_ID) {
                GlobalSource.insertData(mPageId, result);
            } else {
                mList.addAll(0, result);
            }
        }

        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "onRefreshSuccess: " + result);
    }

    /**
     * 加载更多失败回调
     * @param event
     */
    protected void onLoadMoreFail(PullFinishEvent event) {
        mIsLoadingMore = false;
        if (event.getCode() == Config.CODE_OK_BUT_EMPTY) {
            mHasMore = false;
            if (mAdapter instanceof LoadMoreSupportAdapter) {
                ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_no_more));
            }
        } else {
            mCanLoadMore = false;
            if (mAdapter instanceof LoadMoreSupportAdapter) {
                ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_load_fail));
            }
        }
        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "load more fail: " + event);
    }

    /**
     * 加载更多成功回调
     * @param result
     */
    protected void onLoadMoreSuccess(List<Item> result) {
        mIsLoadingMore = false;
        mCanLoadMore = true;
        mList.addAll(result);
        if (result.size() < Config.DEFAULT_REQUEST_COUNT) {
            mHasMore = false;
            if (mAdapter instanceof LoadMoreSupportAdapter) {
                ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_no_more));
            }
        } else {
            if (mAdapter instanceof LoadMoreSupportAdapter) {
                ((LoadMoreSupportAdapter) mAdapter).setLoadMoreTip(getString(R.string.common_str_load_finish));
            }
        }
        mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "load more success: " + result);
    }

    protected boolean checkLoginIfNeed() {
        if (Enviroment.isVisitor()) {
            Toast.makeText(MyApplication.getAppContext(), R.string.fm_login_tip_login_before, Toast.LENGTH_SHORT).show();
            LoginActivity.startAction(getContext(), LoginActivity.LOGIN_TAB);
            return true;
        }
        return false;
    }
}
