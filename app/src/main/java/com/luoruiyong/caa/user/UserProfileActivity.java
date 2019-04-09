package com.luoruiyong.caa.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.common.fragment.SwipeTagFragment;
import com.luoruiyong.caa.simple.SettingsActivity;
import com.luoruiyong.caa.utils.PageUtils;

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
    private TextView mDescriptionTv;

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

        handleIntent();

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
        mDescriptionTv = findViewById(R.id.tv_description);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mTitleTv.setText(R.string.title_user_profile);

        mBackIv.setOnClickListener(this);
        mSettingsIv.setOnClickListener(this);
        mEditIv.setOnClickListener(this);

        setUpErrorViewStub(findViewById(R.id.vs_error_view));
    }

    private void handleIntent() {
        Intent intent = getIntent();
        int uid;
        if (intent == null || (uid = intent.getIntExtra(PageUtils.KEY_USER_PROFILE_UID, -1)) == -1) {
            finish();
            return;
        }
        if (Enviroment.isSelf(uid)) {
            mUser = Enviroment.getCurUser();
            bindData();
        } else {
            // 网络获取用户数据
            showLoadingView();

            // 模拟
            mUserAvatarIv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideTipView();
                    mUser = new User();
                    bindData();

                }
            }, 2000);
        }
    }

    private void bindData() {
        if (mUser == null) {
            return;
        }
//        mUserAvatarIv.setImageUrl(mUser.getAvatar());
        mUserIdTv.setText(String.format(getString(R.string.profile_str_id), mUser.getId()));
        mNicknameTv.setText(String.format(getString(R.string.profile_str_nickname), mUser.getNickName()));

        StringBuilder builder = new StringBuilder();
        builder.append(TextUtils.isEmpty(mUser.getGender()) ? "" : mUser.getGender() + "\n");
        builder.append(mUser.getAge() == 0 ? "" : mUser.getAge() + "\n");
        builder.append(TextUtils.isEmpty(mUser.getEmail()) ? "" : mUser.getEmail());
        mBaseInfoTv.setText(builder.toString());

        User.CollegeInfo collegeInfo = mUser.getCollegeInfo();
        if (collegeInfo != null) {
            builder = new StringBuilder();
            builder.append(TextUtils.isEmpty(collegeInfo.getName()) ? "" : collegeInfo.getName() + "\n");
            builder.append(TextUtils.isEmpty(collegeInfo.getDepartment()) ? "" : collegeInfo.getDepartment() + "\n");
            builder.append(TextUtils.isEmpty(collegeInfo.getMajor()) ? "" : collegeInfo.getMajor() + "\n");
            builder.append(TextUtils.isEmpty(collegeInfo.getKlass()) ? "" : collegeInfo.getKlass());
            mCollegeInfoTv.setText(builder.toString());
        }

        if (!TextUtils.isEmpty(mUser.getDescription())) {
            mDescriptionTv.setText(mUser.getDescription());
        }

        if (Enviroment.isSelf(mUser.getUid())) {
            mEditIv.setVisibility(View.VISIBLE);
            mSettingsIv.setVisibility(View.VISIBLE);
        }
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_SELF));
        mFragmentList.add(new SwipeTagFragment());
        mFragmentList.add(SwipeDiscoverFragment.newInstance(SwipeDiscoverFragment.TYPE_SELF));
        mFragmentList.add(SwipeActivityFragment.newInstance(SwipeActivityFragment.TYPE_COLLECT));

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
