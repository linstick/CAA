package com.luoruiyong.caa.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.viewholder.ActivityItemViewHolder;
import com.luoruiyong.caa.widget.ImageViewLayoutV2;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/4/004
 **/
public class DiscoverDetailFragment extends Fragment implements View.OnClickListener, ImageViewLayoutV2.OnImageClickListener{

    private static final String KEY_ACTIVITY_DATA = "key_activity_data";

    private View mExtrasRootView;
    private LinearLayout mExtrasContainer;
    private ImageView mAddCommentIv;
    private ImageView mAddAdditionIv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ActivityItemViewHolder mViewHolder;
    private DiscoverData mData;
    private List<String> mTitleList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    public static DiscoverDetailFragment newInstance(DiscoverData data) {
        DiscoverDetailFragment fm = new DiscoverDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ACTIVITY_DATA, data);
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

    private void handleArguments() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }
        mData = (DiscoverData) bundle.getSerializable(KEY_ACTIVITY_DATA);
//        mViewHolder.bindData(mData, -1);
    }

    private void initView(View rootView) {
        mViewHolder = new ActivityItemViewHolder(rootView);
        mExtrasRootView = mViewHolder.mExtrasVs.inflate();
        mExtrasContainer = mExtrasRootView.findViewById(R.id.ll_extras_container);
        mAddCommentIv = rootView.findViewById(R.id.iv_add_operate);
        mTabLayout = rootView.findViewById(R.id.tab_layout);
        mViewPager = rootView.findViewById(R.id.view_pager);

        mAddCommentIv.setOnClickListener(this);
        mViewHolder.mUserAvatarIv.setOnClickListener(this);
        mViewHolder.mNicknameTv.setOnClickListener(this);
        mViewHolder.mTopicTv.setOnClickListener(this);
        mViewHolder.mCollectTv.setOnClickListener(this);
        mViewHolder.mMoreIv.setOnClickListener(this);
        mViewHolder.mImageViewLayout.setOnImageClickListener(this);

        mViewHolder.mCommentTv.setVisibility(View.GONE);
        mViewHolder.mImageViewLayout.setMaxChildCount(9);
    }

    private void initFragment() {
        mTitleList = new ArrayList<>();
        mFragmentList = new ArrayList<>();

        mTitleList.add(String.format(getString(R.string.activity_detail_str_comment), mData.getCommentCount()));
        mFragmentList.add(CommentFragment.newInstance(CommentFragment.TYPE_ACTIVITY_COMMENT));

        mAdapter = new ViewPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onImageClick(View parent, int position) {

    }
}
