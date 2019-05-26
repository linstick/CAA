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

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.common.fragment.SwipeTopicFragment;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.simple.SettingsActivity;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.utils.PageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class UserProfileActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mRightOperateTv;
    private SimpleDraweeView mUserAvatarIv;
    private TextView mUserIdTv;
    private TextView mNicknameTv;
    private ImageView mEditIv;
    private TextView mBaseInfoTv;
    private TextView mCollegeInfoTv;
    private TextView mDescriptionTv;
    private TextView mActivityCountTv;
    private TextView mTopicCountTv;
    private TextView mDiscoverCountTv;
    private TextView mCollectCountTv;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private int mUid;
    private User mUser;
    private boolean mIsSelf;
    private List<String> mTabTitleList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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

    @Override
    protected void onResume() {
        super.onResume();
        // 退出登录操作
        if (mIsSelf) {
            if (Enviroment.getCurUid() != mUid) {
                finish();
            } else if (mUser != null && mUser != Enviroment.getCurUser()) {
                // 用户修改过信息
                mUser = Enviroment.getCurUser();
                bindUserData();
            }
        }
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mRightOperateTv = findViewById(R.id.iv_right_operate);
        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mUserIdTv = findViewById(R.id.tv_id);
        mNicknameTv = findViewById(R.id.tv_nickname);
        mEditIv = findViewById(R.id.iv_edit_profile);
        mBaseInfoTv = findViewById(R.id.tv_basic_info);
        mCollegeInfoTv = findViewById(R.id.tv_college_info);
        mDescriptionTv = findViewById(R.id.tv_description);
        mActivityCountTv = findViewById(R.id.tv_activity_count);
        mTopicCountTv = findViewById(R.id.tv_topic_count);
        mDiscoverCountTv = findViewById(R.id.tv_discover_count);
        mCollectCountTv = findViewById(R.id.tv_collect_count);
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        mTitleTv.setText(R.string.title_user_profile);

        mBackIv.setOnClickListener(this);
        mRightOperateTv.setOnClickListener(this);
        mEditIv.setOnClickListener(this);

        setUpErrorViewStub(findViewById(R.id.vs_error_view));
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent == null || (mUid = intent.getIntExtra(PageUtils.KEY_USER_PROFILE_PAGE_UID, -1)) == -1) {
            finish();
            return;
        }
        if (Enviroment.isSelf(mUid)) {
            mIsSelf = true;
            mUser = Enviroment.getCurUser();
            bindUserData();
            initFragment();
        } else {
            // 网络获取用户数据
            showLoadingView();
            getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {
                    CommonFetcher.doFetchOtherUserDetail(mUid);
                }
            });
        }
    }

    private void bindUserData() {
        if (mUser == null) {
            return;
        }
        mUserAvatarIv.setImageURI(mUser.getAvatar());
        mUserIdTv.setText(String.format(getString(R.string.profile_str_account), mUser.getId()));
        mNicknameTv.setText(String.format(getString(R.string.profile_str_nickname), mUser.getNickname()));
        mActivityCountTv.setText(String.format(getString(R.string.profile_str_activity_count), String.valueOf(mUser.getActivityCount())));
        mTopicCountTv.setText(String.format(getString(R.string.profile_str_topic_count), String.valueOf(mUser.getTopicCount())));
        mDiscoverCountTv.setText(String.format(getString(R.string.profile_str_discover_count),String.valueOf(mUser.getDiscoverCount())));

        StringBuilder builder = new StringBuilder();
        builder.append(TextUtils.isEmpty(mUser.getGender()) ? "" : mUser.getGender());
        builder.append(mUser.getAge() == 0 ? "" : "\n" + mUser.getAge());
        builder.append(TextUtils.isEmpty(mUser.getEmail()) ? "" :  "\n" +mUser.getEmail());
        mBaseInfoTv.setText(builder.toString());

        User.CollegeInfo collegeInfo = mUser.getCollegeInfo();
        if (collegeInfo != null) {
            builder = new StringBuilder();
            builder.append(TextUtils.isEmpty(collegeInfo.getName()) ? "" : collegeInfo.getName());
            builder.append(TextUtils.isEmpty(collegeInfo.getDepartment()) ? "" :  "\n" +collegeInfo.getDepartment());
            builder.append(TextUtils.isEmpty(collegeInfo.getMajor()) ? "" :  "\n" +collegeInfo.getMajor());
            builder.append(TextUtils.isEmpty(collegeInfo.getKlass()) ? "" :  "\n" +collegeInfo.getKlass());
            mCollegeInfoTv.setText(builder.toString());
        }

        if (!TextUtils.isEmpty(mUser.getDescription())) {
            mDescriptionTv.setText(mUser.getDescription());
        }

        if (mIsSelf) {
            mEditIv.setVisibility(View.VISIBLE);
            mRightOperateTv.setVisibility(View.VISIBLE);
            mRightOperateTv.setImageResource(R.drawable.ic_settings_white);
            mCollectCountTv.setVisibility(View.VISIBLE);
            mCollectCountTv.setText(String.format(getString(R.string.profile_str_collect_count),String.valueOf(mUser.getCollectCount())));
        } else {
            mRightOperateTv.setVisibility(View.VISIBLE);
            mRightOperateTv.setImageResource(R.drawable.ic_more_white);
        }
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        if (Enviroment.isSelf(mUid)) {
            mFragmentList.add(SwipeActivityFragment.newInstance(Config.PAGE_ID_ACTIVITY_SELF));
            mFragmentList.add(SwipeTopicFragment.newInstance(Config.PAGE_ID_TOPIC_SELF));
            mFragmentList.add(SwipeDiscoverFragment.newInstance(Config.PAGE_ID_DISCOVER_SELF));
            mFragmentList.add(SwipeActivityFragment.newInstance(Config.PAGE_ID_ACTIVITY_SELF_COLLECT));
        } else {
            mFragmentList.add(SwipeActivityFragment.newInstance(Config.PAGE_ID_ACTIVITY_OTHER_USER, mUid));
            mFragmentList.add(SwipeTopicFragment.newInstance(Config.PAGE_ID_TOPIC_OTHER_USER, mUid));
            mFragmentList.add(SwipeDiscoverFragment.newInstance(Config.PAGE_ID_DISCOVER_OTHER_USER, mUid));
        }

        mTabTitleList = new ArrayList<>();
        mTabTitleList.add(getString(R.string.title_activity));
        mTabTitleList.add(getString(R.string.title_topic));
        mTabTitleList.add(getString(R.string.title_discover));
        if (Enviroment.isSelf(mUid)) {
            mTabTitleList.add(getString(R.string.title_collect));
        }

        mTabLayout.setupWithViewPager(mViewPager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList, mTabTitleList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);
    }

    @Override
    protected void onRefreshClick() {
        showLoadingView();
        CommonFetcher.doFetchOtherUserDetail(mUid);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_right_operate:
                if (mIsSelf) {
                    startActivity(new Intent(this, SettingsActivity.class));
                } else {
                    DialogHelper.showListDialog(this, getString(R.string.common_str_impeach), new CommonDialog.Builder.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            PageUtils.gotoFeedbackPage(UserProfileActivity.this, mUser);
                        }
                    });
                }
                break;
            case R.id.iv_edit_profile:
                startActivity(new Intent(this, EditBasicInfoActivity.class));
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent<User> event) {
        switch (event.getType()) {
            case FETCH_USER_DETAIL:
                hideTipView();
                if (event.getCode() == Config.CODE_OK) {
                    mUser = event.getData();
                    bindUserData();
                    initFragment();
                } else if (event.getCode() == Config.CODE_REQUEST_ERROR){
                    showErrorView(R.drawable.bg_no_network, event.getStatus());
                } else {
                    showErrorView(R.drawable.bg_load_fail, event.getStatus());
                }
                break;
            default:
                break;
        }
    }
}
