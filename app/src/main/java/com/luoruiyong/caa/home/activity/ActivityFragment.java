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
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;

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

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_ALL));
        mTitleTabList.add("All");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_PREACH));
        mTitleTabList.add("Preach");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_VOLUNTEER));
        mTitleTabList.add("Volunteer");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_CLUB));
        mTitleTabList.add("Club");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_LECTURE));
        mTitleTabList.add("Lecture");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_SPORT));
        mTitleTabList.add("Sport");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_CAMPUS));
        mTitleTabList.add("Campus");

        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_OTHER));
        mTitleTabList.add("Other");

        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleTabList);
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mViewPager.setOffscreenPageLimit(1);
    }
}
