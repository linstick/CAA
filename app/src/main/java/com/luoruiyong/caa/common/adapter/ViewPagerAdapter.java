package com.luoruiyong.caa.common.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.luoruiyong.caa.utils.ListUtils;

import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mList;
    private List<String> mTitleList;
    private FragmentManager mFm;

    public ViewPagerAdapter(FragmentManager fm) {
        this(fm, null, null);
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list) {
        this(fm, list, null);
    }

    public ViewPagerAdapter(FragmentManager fm, List<Fragment> list, List<String> titleList) {
        super(fm);
        this.mFm = fm;
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
        if (mTitleList != null) {
            return mTitleList.get(position);
        }
        return super.getPageTitle(position);
    }
}
