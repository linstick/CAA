package com.luoruiyong.caa.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.common.fragment.SwipeTagFragment;
import com.luoruiyong.caa.simple.SettingsActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class UserProfileActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mSettingsIv;
    private ImageView mUserAvatarIv;
    private TextView mUserIdTv;
    private TextView mNicknameTv;
    private ImageView mEditIv;
    private TextView mBaseInfoTv;
    private TextView mCollegeInfoTv;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private User mUser;
    private List<String> mTabTitleList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initView();

        initFragment();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mSettingsIv = findViewById(R.id.iv_right_operate);
        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mUserIdTv = findViewById(R.id.tv_id);
        mNicknameTv = findViewById(R.id.tv_nickname);
        mEditIv = findViewById(R.id.iv_edit_profile);
        mBaseInfoTv = findViewById(R.id.tv_basic_info);
        mCollegeInfoTv = findViewById(R.id.tv_college_info);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mTitleTv.setText(R.string.title_user_profile);
        mSettingsIv.setVisibility(View.VISIBLE);

        mBackIv.setOnClickListener(this);
        mSettingsIv.setOnClickListener(this);
        mEditIv.setOnClickListener(this);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(SwipeActivityFragment.newInstance());
        mFragmentList.add(new SwipeTagFragment());
        mFragmentList.add(new SwipeDiscoverFragment());
        mFragmentList.add(SwipeActivityFragment.newInstance());

        mTabTitleList = new ArrayList<>();
        mTabTitleList.add(getString(R.string.title_activity));
        mTabTitleList.add(getString(R.string.title_topic));
        mTabTitleList.add(getString(R.string.title_discover));
        mTabTitleList.add(getString(R.string.title_collect));

        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTabTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right_operate:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.iv_edit_profile:
                startActivity(new Intent(this, EditBasicInfoActivity.class));
                break;
            default:
                break;
        }
    }
}
