package com.luoruiyong.caa.topic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.eventbus.DetailFinishEvent;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.luoruiyong.caa.utils.PageUtils.DETAIL_TYPE_TOPIC_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_DATA;
import static com.luoruiyong.caa.utils.PageUtils.KEY_DETAIL_PAGE_TYPE;
import static com.luoruiyong.caa.utils.PageUtils.KEY_TOPIC_PAGE_ID;
import static com.luoruiyong.caa.utils.PageUtils.KEY_TOPIC_PAGE_POSITION;

/**
 * Author: luoruiyong
 * Date: 2019/3/18/018
 **/
public class TopicActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "TopicActivity";

    private ImageView mBackIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private View mTabLayoutContainer;
    private View mDividerView;

    private SimpleDraweeView mUserAvatarIv;
    private TextView mNicknameTv;
    private TextView mTagNameTv;
    private TextView mJoinedCountTv;
    private TextView mVisitedCountTv;
    private TextView mIntroductionTv;
    private SimpleDraweeView mTagCoverIv;
    private View mCenterDivider;
    private View mIntroductionLayout;
    AppBarLayout mAppBarLayout;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private ViewPagerAdapter mAdapter;

    private State mCurState = State.IDLE;
    private int mPosition;
    private int mTopicId;
    private TopicData mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initView();

        handleIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mNicknameTv = findViewById(R.id.tv_nickname);
        mTagNameTv = findViewById(R.id.tv_tag_name);
        mJoinedCountTv = findViewById(R.id.tv_join_count);
        mVisitedCountTv = findViewById(R.id.tv_visit_count);
        mIntroductionTv = findViewById(R.id.tv_introduction);
        mTagCoverIv = findViewById(R.id.iv_tag_cover);
        mCenterDivider = findViewById(R.id.view_center_divider);
        mIntroductionLayout = findViewById(R.id.ll_introduce_layout);

        mBackIv.setOnClickListener(this);
        findViewById(R.id.iv_header_back).setOnClickListener(this);
        mUserAvatarIv.setOnClickListener(this);
        mNicknameTv.setOnClickListener(this);

        mAppBarLayout = findViewById(R.id.app_bar_layout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (mCurState != State.EXPANDED) {
                        onStateChanged(appBarLayout, State.EXPANDED);
                    }
                    mCurState = State.EXPANDED;
                } else if (Math.abs(-verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    if (mCurState != State.COLLAPSED) {
                        onStateChanged(appBarLayout, State.COLLAPSED);
                    }
                    mCurState = State.COLLAPSED;
                } else {
                    if (mCurState != State.IDLE) {
                        onStateChanged(appBarLayout, State.IDLE);
                    }
                    mCurState = State.IDLE;
                }
            }
        });

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
        mTabLayoutContainer = findViewById(R.id.ll_tab_layout_container);
        mDividerView = findViewById(R.id.view_divider);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        int type;
        if (intent == null || (type = intent.getIntExtra(KEY_DETAIL_PAGE_TYPE, -1)) == -1) {
            finish();
            return;
        }
        mPosition = intent.getIntExtra(KEY_TOPIC_PAGE_POSITION, 0);
        if (type == DETAIL_TYPE_TOPIC_ID) {
            if ((mTopicId = intent.getIntExtra(KEY_TOPIC_PAGE_ID, -1)) == -1) {
                finish();
                return;
            }
            CommonFetcher.doFetchTopicDetail(mTopicId);
        } else {
            if ((mData = (TopicData) intent.getSerializableExtra(KEY_DETAIL_PAGE_DATA)) == null) {
                finish();
                return;
            }
            bindBasicData();
            initFragment();
        }
    }

    private void bindBasicData() {
        if (mData == null) {
            return;
        }
        mUserAvatarIv.setImageURI(mData.getAvatarUrl());
        mTagCoverIv.setImageURI(mData.getCoverUrl());
        mNicknameTv.setText(mData.getNickname());
        mTagNameTv.setText(String.format(getString(R.string.common_str_topic), mData.getName()));
        mVisitedCountTv.setText(String.format(getString(R.string.common_str_visit_count), mData.getVisitedCount()));
        mJoinedCountTv.setText(String.format(getString(R.string.common_str_join_count), mData.getJoinCount()));
        mCenterDivider.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mData.getIntroduction())) {
            mIntroductionLayout.setVisibility(View.VISIBLE);
            mIntroductionTv.setText(mData.getIntroduction());
        }
    }

    private void initFragment() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add(getString(R.string.topic_detail_str_discover));
        mFragmentList.add(SwipeDiscoverFragment.newInstance(Config.PAGE_ID_DISCOVER_TOPIC, mData.getId(), mPosition));

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
    }

    private void onStateChanged(AppBarLayout appBarLayout, State changedState) {
        Log.d(TAG, "onStateChanged: " + changedState);
        if (changedState == State.COLLAPSED) {
            mBackIv.setVisibility(View.VISIBLE);
            mTabLayoutContainer.setBackgroundColor(ResourcesUtils.getColor(R.color.colorPrimary));
            mTabLayout.setTabTextColors(ResourcesUtils.getColor(R.color.colorDDDDDD), ResourcesUtils.getColor(R.color.white));
            mTabLayout.setSelectedTabIndicatorColor(ResourcesUtils.getColor(R.color.white));
            mDividerView.setBackgroundColor(ResourcesUtils.getColor(R.color.colorPrimary));
        } else {
            mBackIv.setVisibility(View.GONE);
            mTabLayoutContainer.setBackgroundColor(ResourcesUtils.getColor(android.R.color.transparent));
            mTabLayout.setTabTextColors(ResourcesUtils.getColor(R.color.color888888), ResourcesUtils.getColor(R.color.colorPrimary));
            mTabLayout.setSelectedTabIndicatorColor(ResourcesUtils.getColor(R.color.colorPrimary));
            mDividerView.setBackgroundColor(ResourcesUtils.getColor(R.color.colorDDDDDD));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
            case R.id.iv_header_back:
                finish();
                break;
            case R.id.iv_user_avatar:
            case R.id.tv_nickname:
                PageUtils.gotoUserProfilePage(this, mData.getUid());
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDetailFinishEvent(DetailFinishEvent<TopicData> event) {
        hideTipView();
        if (event.getCode() == Config.CODE_OK) {
            mData = event.getData();
            bindBasicData();
            initFragment();
        } else if (event.getCode() == Config.CODE_NO_DATA) {
            showErrorView(R.drawable.bg_load_fail, getString(R.string.common_tip_no_data));
        } else if (event.getCode() == Config.CODE_NETWORK_ERROR) {
            showErrorView(R.drawable.bg_no_network, getString(R.string.common_tip_no_network));
        }
    }

    public void setAppBarExpanded(boolean expanded) {
        mAppBarLayout.setExpanded(expanded);
    }

    enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}

