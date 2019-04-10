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

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.puller.DiscoverPuller;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.List;

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

    private ImageView mUserAvatarIv;
    private TextView mNicknameTv;
    private TextView mTagNameTv;
    private TextView mJoinedCountTv;
    private TextView mVisitedCountTv;
    private TextView mIntroductionTv;
    private ImageView mTagCoverIv;
    private View mCenterDivider;
    private View mIntroductionLayout;
    AppBarLayout mAppBarLayout;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private ViewPagerAdapter mAdapter;

    private State mCurState = State.IDLE;
    private int mTargetId;
    private int mPosition;
    private TopicData mData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initView();

        handleIntent();
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
        if (intent == null || (mTargetId = intent.getIntExtra(KEY_TOPIC_PAGE_ID, -1)) == -1) {
            finish();
            return;
        }
        mPosition = intent.getIntExtra(KEY_TOPIC_PAGE_POSITION, 0);

        // 联网拉取数据

        // 模拟
        mBackIv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideTipView();
                mData = new TopicData();
                bindBasicData();
                initFragment();
            }
        }, 1200);

    }

    private void bindBasicData() {
        if (mData == null) {
            return;
        }
//        mUserAvatarIv.setImageUrl(mData.getAvatarUrl());
        mNicknameTv.setText(mData.getNickname());
        mTagNameTv.setText(String.format(getString(R.string.common_str_topic), mData.getName()));
        mVisitedCountTv.setText(String.format(getString(R.string.common_str_visit_count), mData.getVisitedCount()));
        mJoinedCountTv.setText(String.format(getString(R.string.common_str_join_count), mData.getJoinedCount()));
        mCenterDivider.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(mData.getIntroduction())) {
            mIntroductionLayout.setVisibility(View.VISIBLE);
            mIntroductionTv.setText(mData.getIntroduction());
        }
        if (mPosition != 0) {
            // 折叠
            mAppBarLayout.setExpanded(false);
        }
    }

    private void initFragment() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add(getString(R.string.topic_detail_str_hot));
        mFragmentList.add(SwipeDiscoverFragment.newInstance(DiscoverPuller.TYPE_TOPIC_HOT, mData.getId(), mPosition));

        mTitleList.add(getString(R.string.topic_detail_str_lasted));
        mFragmentList.add(SwipeDiscoverFragment.newInstance(DiscoverPuller.TYPE_TOPIC_LASTED, mData.getId()));

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

    enum State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}

