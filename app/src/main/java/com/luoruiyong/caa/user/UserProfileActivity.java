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
import com.luoruiyong.caa.home.activity.SwipeActivityFragment;
import com.luoruiyong.caa.home.discover.DiscoverFragment;
import com.luoruiyong.caa.home.tag.TagFragment;
import com.luoruiyong.caa.settings.SettingsActivity;

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
    private TextView mAgeTv;
    private TextView mGenderTv;
    private TextView mCollegeTv;

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
        mAgeTv = findViewById(R.id.tv_age);
        mGenderTv = findViewById(R.id.tv_gender);
        mCollegeTv = findViewById(R.id.tv_college);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mTitleTv.setText(R.string.title_user_profile);
        mSettingsIv.setVisibility(View.VISIBLE);

        mBackIv.setOnClickListener(this);
        mSettingsIv.setOnClickListener(this);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(SwipeActivityFragment.newInstance());
        mFragmentList.add(new TagFragment());
        mFragmentList.add(new DiscoverFragment());
        mFragmentList.add(SwipeActivityFragment.newInstance());

        mTabTitleList = new ArrayList<>();
        mTabTitleList.add("Notes");
        mTabTitleList.add("Tags");
        mTabTitleList.add("Discovers");
        mTabTitleList.add("Collects");

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
            default:
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mList;
        private List<String> mTitleList;

        public ViewPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titleList) {
            super(fm);
            mList = list;
            mTitleList = titleList;
        }

        @Override
        public Fragment getItem(int position) {
            return mList.get(position);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }
    }
}
