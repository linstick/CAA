package com.luoruiyong.caa.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.model.CommonTargetOperator;
import com.luoruiyong.caa.simple.PictureBrowseActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.KeyboardUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;
import com.luoruiyong.caa.widget.imageviewlayout.ImageViewLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_DATA;
import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_ACTIVITY_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_TYPE;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_BROWSE_COMMENT;

/**
 * Author: luoruiyong
 * Date: 2019/4/4/004
 **/
public class ActivityDetailFragment extends BaseFragment implements
        View.OnClickListener,
        ImageViewLayout.OnImageClickListener{

    private static final int INPUT_TYPE_COMMENT = 1;
    private static final int INPUT_TYPE_ADDITION = 2;

    private View mExtrasRootView;
    private LinearLayout mExtrasContainer;
    private ImageView mAddOperateIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private View mCommentBarLayout;
    private EditText mCommentInputEt;
    private ImageView mSendIv;
    private AppBarLayout mAppBarLayout;

    private ActivityItemViewHolder mViewHolder;

    private int mActivityId;
    private ActivityData mData;
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    private int mInputType = INPUT_TYPE_COMMENT;

    public static ActivityDetailFragment newInstance(int id) {
        ActivityDetailFragment fm = new ActivityDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_DETAIL_PAGE_ID, id);
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_ACTIVITY_ID);
        fm.setArguments(bundle);
        return fm;
    }

    public static ActivityDetailFragment newInstance(ActivityData data, boolean isComment) {
        ActivityDetailFragment fm = new ActivityDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_DETAIL_PAGE_DATA, data);
        bundle.putInt(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_ACTIVITY_DATA);
        bundle.putBoolean(KEY_DETAIL_PAGE_BROWSE_COMMENT, isComment);
        fm.setArguments(bundle);
        return fm;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_detail, container, false);
        initView(view);

        handleArguments();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View rootView) {
        mViewHolder = new ActivityItemViewHolder(rootView);
        mExtrasRootView = mViewHolder.mExtrasVs.inflate();
        mExtrasContainer = mExtrasRootView.findViewById(R.id.ll_extras_container);
        mAddOperateIv = rootView.findViewById(R.id.iv_add_operate);
        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager = rootView.findViewById(R.id.view_pager);
        mCommentBarLayout = rootView.findViewById(R.id.ll_comment_bar_layout);
        mCommentInputEt = rootView.findViewById(R.id.et_input);
        mSendIv = rootView.findViewById(R.id.iv_send);
        mAppBarLayout = rootView.findViewById(R.id.app_bar_layout);

        mAddOperateIv.setOnClickListener(this);
        mSendIv.setOnClickListener(this);
        mViewHolder.mUserAvatarIv.setOnClickListener(this);
        mViewHolder.mNicknameTv.setOnClickListener(this);
        mViewHolder.mTopicTv.setOnClickListener(this);
        mViewHolder.mCollectTv.setOnClickListener(this);
        mViewHolder.mMoreIv.setOnClickListener(this);
        mViewHolder.mImageViewLayout.setOnImageClickListener(this);

        mViewHolder.mCommentTv.setVisibility(View.GONE);
        mViewHolder.mImageViewLayout.setMaxChildViewCount(9);

        setUpErrorViewStub(rootView.findViewById(R.id.vs_error_view));
    }

    private void handleArguments() {
        showLoadingView();
        Bundle bundle = getArguments();
        int type;
        if (bundle == null || (type = bundle.getInt(KEY_DETAIL_PAGE_TYPE, -1)) == -1) {
            getActivity().finish();
            return;
        }
        if (type == DETAIL_TYPE_ACTIVITY_ID) {
            if ((mActivityId = bundle.getInt(KEY_DETAIL_PAGE_ID, -1)) == -1) {
                getActivity().finish();
                return;
            }
            // 联网获取活动详情数据
            CommonFetcher.doFetchActivityDetail(mActivityId);
        } else {
            if ((mData = (ActivityData) bundle.getSerializable(KEY_DETAIL_PAGE_DATA)) == null) {
                getActivity().finish();
                return;
            }
            hideTipView();
            mActivityId = mData.getId();
            mViewHolder.bindData(mData);
            bindExtrasInfo();
            initFragment();
            if (bundle.getBoolean(KEY_DETAIL_PAGE_BROWSE_COMMENT, false)) {
                toggleCommentBar();
            }
        }
    }

    private void initFragment() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
        mFragmentList.add(CommentFragment.newInstance(Config.PAGE_ID_ACTIVITY_COMMENT, mData.getId()));
        mTitleList.add(String.format(getString(R.string.activity_detail_str_addition), mData.getAdditionCount()));
        mFragmentList.add(AdditionFragment.newInstance(mData.getUid(), mData.getId()));

        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    if (Enviroment.isSelf(mData.getUid())) {
                        mAddOperateIv.setImageResource(R.drawable.ic_edit_light_gray);
                        mCommentInputEt.setHint(R.string.activity_detail_str_addition_bar_hint);
                    } else {
                        mAddOperateIv.setVisibility(View.GONE);
                    }
                    mInputType = INPUT_TYPE_ADDITION;
                } else {
                    mAddOperateIv.setVisibility(View.VISIBLE);
                    mAddOperateIv.setImageResource(R.drawable.ic_comment_light_gray);
                    mCommentInputEt.setHint(R.string.activity_detail_str_comment_bar_hint);
                    mInputType = INPUT_TYPE_COMMENT;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void bindExtrasInfo() {
        if (!TextUtils.isEmpty(mData.getHost())) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_extras_list, mExtrasContainer, false);
            ((TextView)view.findViewById(R.id.tv_label)).setText(getString(R.string.activity_detail_str_host));
            ((TextView)view.findViewById(R.id.tv_value)).setText(mData.getHost());
            mExtrasContainer.addView(view);
        }
        if (!TextUtils.isEmpty(mData.getTime())) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_extras_list, mExtrasContainer, false);
            ((TextView)view.findViewById(R.id.tv_label)).setText(getString(R.string.activity_detail_str_time));
            ((TextView)view.findViewById(R.id.tv_value)).setText(mData.getTime());
            mExtrasContainer.addView(view);
        }
        if (!TextUtils.isEmpty(mData.getAddress())) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_extras_list, mExtrasContainer, false);
            ((TextView)view.findViewById(R.id.tv_label)).setText(getString(R.string.activity_detail_str_address));
            ((TextView)view.findViewById(R.id.tv_value)).setText(mData.getAddress());
            mExtrasContainer.addView(view);
        }
        if (mExtrasContainer.getChildCount() > 0) {
            mExtrasRootView.setVisibility(View.VISIBLE);
        }
    }

    private void checkAndSendComment() {
        String text = mCommentInputEt.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(getContext(), R.string.activity_detail_tip_empty_comment_input, Toast.LENGTH_SHORT).show();
            return;
        }
        CommonTargetOperator.doAddActivityComment(mActivityId, text);
    }

    private void onCommentCountOrAdditionCountChanged() {
        mTitleList.clear();
        mTitleList.add(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
        mTitleList.add(String.format(getString(R.string.activity_detail_str_addition), mData.getAdditionCount()));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_avatar:
            case R.id.tv_nickname:
                PageUtils.gotoUserProfilePage(getContext(), mData.getUid());
                break;
            case R.id.tv_topic:
                PageUtils.gotoTopicPage(getContext(), mData.getTopicId());
                break;
            case R.id.tv_collect:
                // 先请求，请求成功后修改数据，和列表中的活动收藏的逻辑不一样
                CommonTargetOperator.doCollectActivity(mData.getId(), !mData.isHasCollect());
                break;
            case R.id.tv_more:
                List<String>  mItemMoreStringArray = Enviroment.getItemMoreNewStringArray();
                List<String> items = new ArrayList<>();
                if (Enviroment.isSelf(mData.getUid())) {
                    items.addAll(mItemMoreStringArray);
                    items.add(ResourcesUtils.getString(R.string.common_str_delete));
                } else {
                    items = mItemMoreStringArray;
                }
                DialogHelper.showListDialog(getContext(), items, new CommonDialog.Builder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Toast.makeText(ActivityDetailFragment.this.getContext(), "click position = " + position, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.iv_add_operate:
                toggleCommentBar();
                break;
            case R.id.iv_send:
                checkAndSendComment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onImageClick(View parent, int position) {
        PictureBrowseActivity.startAction(getActivity(), mData.getPictureList(), position);
    }

    private void toggleCommentBar() {
        if (mCommentBarLayout.getVisibility() == View.VISIBLE) {
            mCommentBarLayout.setVisibility(View.GONE);
            mCommentInputEt.clearFocus();
        } else {
            mCommentBarLayout.setVisibility(View.VISIBLE);
            mCommentInputEt.requestFocus();
            KeyboardUtils.showKeyboard(mCommentInputEt);
        }
    }

    @Override
    protected void doRefresh() {
        showLoadingView();
        CommonFetcher.doFetchActivityDetail(mActivityId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onComonEvent(CommonEvent event) {
        hideTipView();
        switch (event.getType()) {
            case FETCH_ACTIVITY_DETAIL:
                if (event.getCode() == Config.CODE_OK) {
                    mData = (ActivityData) event.getData();
                    mViewHolder.bindData(mData);
                    bindExtrasInfo();
                    initFragment();
                } else if (event.getCode() == Config.CODE_NO_DATA) {
                    showErrorView(R.drawable.bg_load_fail, getString(R.string.common_tip_no_data));
                } else if (event.getCode() == Config.CODE_NETWORK_ERROR) {
                    showErrorView(R.drawable.bg_no_network, getString(R.string.common_tip_no_network));
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonOperateEvent(CommonOperateEvent event) {
        if (event.getTargetId() != mActivityId) {
            return;
        }
        switch (event.getType()) {
            case COLLECT_ACTIVITY:
                if (event.getCode() == Config.CODE_OK) {
                    boolean isCollect = !mData.isHasCollect();
                    int collectCount = mData.getCollectCount() + (isCollect ? 1 : 0);
                    mData.setHasCollect(isCollect);
                    mData.setCollectCount(collectCount);
                    mViewHolder.mCollectTv.setSelected(isCollect);
                    mViewHolder.mCollectTv.setText(collectCount <= 0 ? getString(R.string.common_str_collect) : String.valueOf(collectCount));
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ADD_ACTIVITY_COMMENT:
                if (event.getCode() == Config.CODE_OK) {
                    mData.setCommentCount(mData.getCommentCount() + 1);
                    onCommentCountOrAdditionCountChanged();
                    Toast.makeText(getContext(), R.string.common_tip_comment_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETE_ACTIVITY_COMMENT:
                if (event.getCode() == Config.CODE_OK) {
                    mData.setCommentCount(mData.getCommentCount() - 1);
                    onCommentCountOrAdditionCountChanged();
                    Toast.makeText(getContext(), R.string.common_str_delete_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case ADD_ACTIVITY_ADDITION:
                if (event.getCode() == Config.CODE_OK) {
                    mData.setAdditionCount(mData.getAdditionCount() + 1);
                    onCommentCountOrAdditionCountChanged();
                    Toast.makeText(getContext(), R.string.common_tip_addition_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case DELETE_ACTIVITY_ADDITION:
                if (event.getCode() == Config.CODE_OK) {
                    mData.setAdditionCount(mData.getAdditionCount() - 1);
                    onCommentCountOrAdditionCountChanged();
                    Toast.makeText(getContext(), R.string.common_str_delete_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
