package com.luoruiyong.caa.topic;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.user.UserProfileActivity;
import com.luoruiyong.caa.utils.DisplayUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/18/018
 **/
public class TopicActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = "TopicActivity";

    private TextView mTitleTv;
    private LinearLayout mActionBarLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private LinearLayout mTabLayoutContainer;
    private View mDividerView;

    private ImageView mUserAvatarIv;
    private TextView mNicknameTv;
    private TextView mTagNameTv;
    private TextView mJoinedCountTv;
    private TextView mVisitedCountTv;
    private TextView mIntroductionTv;
    private ImageView mTagCoverIv;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private ViewPagerAdapter mAdapter;


    private State mCurState = State.IDLE;
    private boolean mHasSetActionBarMarginTop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);

        initView();

        initFragment();

    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText(R.string.title_topic_detail);

        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mNicknameTv = findViewById(R.id.tv_nickname);
        mTagNameTv = findViewById(R.id.tv_tag_name);
        mJoinedCountTv = findViewById(R.id.tv_join_count);
        mVisitedCountTv = findViewById(R.id.tv_visit_count);
        mIntroductionTv = findViewById(R.id.tv_introduction);
        mTagCoverIv = findViewById(R.id.iv_tag_cover);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_header_back).setOnClickListener(this);
        mUserAvatarIv.setOnClickListener(this);
        mNicknameTv.setOnClickListener(this);

        mActionBarLayout = findViewById(R.id.action_bar);
        ((AppBarLayout)findViewById(R.id.app_bar_layout)).addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
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

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new SwipeDiscoverFragment());
        mFragmentList.add(new SwipeDiscoverFragment());

        mTitleList = new ArrayList<>();
        mTitleList.add("Hot");
        mTitleList.add("Lasted");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
    }

    private void onStateChanged(AppBarLayout appBarLayout, State changedState) {
        Log.d(TAG, "onStateChanged: " + changedState);
        if (changedState == State.COLLAPSED) {
            if (!mHasSetActionBarMarginTop) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mActionBarLayout.getLayoutParams();
                params.topMargin = DisplayUtils.getStatusBarHeight(this);
                mActionBarLayout.setLayoutParams(params);
                mHasSetActionBarMarginTop = true;
            }
            mActionBarLayout.setVisibility(View.VISIBLE);
            mTabLayoutContainer.setBackgroundColor(ResourcesUtils.getColor(R.color.colorPrimary));
            mTabLayout.setTabTextColors(ResourcesUtils.getColor(R.color.colorDDDDDD), ResourcesUtils.getColor(R.color.white));
            mTabLayout.setSelectedTabIndicatorColor(ResourcesUtils.getColor(R.color.white));
            mDividerView.setBackgroundColor(ResourcesUtils.getColor(R.color.colorPrimary));
        } else {
            mActionBarLayout.setVisibility(View.GONE);
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
                startActivity(new Intent(this, UserProfileActivity.class));
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

