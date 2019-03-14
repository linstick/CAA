package com.luoruiyong.caa.home.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.home.tag.TagFragment;

import java.util.ArrayList;
import java.util.List;

public class ActivityFragment extends Fragment{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private List<Fragment> mFragmentList;
    private List<String> mTitleTabList;
    private ViewPagerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        initView(view);

        initFragment();

        return view;
    }

    private void initView(View rootView) {
        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager = rootView.findViewById(R.id.view_pager);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mTitleTabList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            mFragmentList.add(new TagFragment());
            mTitleTabList.add("Tab " + (i + 1));
        }
        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleTabList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setOffscreenPageLimit(1);
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
