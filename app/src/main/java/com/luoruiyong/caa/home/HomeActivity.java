package com.luoruiyong.caa.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.home.activity.ActivityFragment;
import com.luoruiyong.caa.home.discover.DiscoverFragment;
import com.luoruiyong.caa.home.message.MessageFragment;
import com.luoruiyong.caa.home.tag.TagFragment;
import com.luoruiyong.caa.login.LoginActivity;
import com.luoruiyong.caa.search.SearchActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB_INDEX = "tab_index";
    public final static int ACTIVITY_TAB_INDEX = 0;
    public final static int TAG_TAB_INDEX = 1;
    public final static int DISCOVER_TAB_INDEX = 2;
    public final static int MESSAGE_TAB_INDEX = 3;

    private TextView mTitleTv;
    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;

    private LinearLayout mActivityTagLayout;
    private LinearLayout mTagTabLayout;
    private LinearLayout mDiscoverTabLayout;
    private LinearLayout mMessageTabLayout;

    private ImageView mUserAvatarIv;
    private ImageView mSearchIv;
    private ImageView mAddIv;

    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;

    private static void startAction(Context context, int tabIndex) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(KEY_TAB_INDEX, tabIndex);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initView();

        initFragment();

        handleIntent();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mViewPager = findViewById(R.id.view_pager);

        mActivityTagLayout = findViewById(R.id.ll_activity_tab_layout);
        mTagTabLayout = findViewById(R.id.ll_tag_tab_layout);
        mDiscoverTabLayout = findViewById(R.id.ll_discover_layout);
        mMessageTabLayout = findViewById(R.id.ll_message_tab_layout);

        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mSearchIv = findViewById(R.id.iv_search);
        mAddIv = findViewById(R.id.iv_add);

        mActivityTagLayout.setOnClickListener(this);
        mTagTabLayout.setOnClickListener(this);
        mDiscoverTabLayout.setOnClickListener(this);
        mMessageTabLayout.setOnClickListener(this);

        mUserAvatarIv.setOnClickListener(this);
        mSearchIv.setOnClickListener(this);
        mAddIv.setOnClickListener(this);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ActivityFragment());
        mFragmentList.add(new TagFragment());
        mFragmentList.add(new DiscoverFragment());
        mFragmentList.add(new MessageFragment());

        mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                updateFragmentByIndex(position, false);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            int tabIndex = intent.getIntExtra(KEY_TAB_INDEX, 0);
            updateFragmentByIndex(tabIndex, true);
        } else {
            updateFragmentByIndex(ACTIVITY_TAB_INDEX, false);
        }
    }

    private void updateFragmentByIndex(int index, boolean needCheck) {
        if (needCheck && (index < 0 || index >= mFragmentList.size() || index == mViewPager.getCurrentItem())) {
            return;
        }
        mViewPager.setCurrentItem(index, false);
        String title;
        switch (index) {
            case ACTIVITY_TAB_INDEX:
                title = getString(R.string.title_activity);
                break;
            case TAG_TAB_INDEX:
                title = getString(R.string.title_tag);
                break;
            case DISCOVER_TAB_INDEX:
                title = getString(R.string.title_discover);
                break;
            case MESSAGE_TAB_INDEX:
                title = getString(R.string.title_message);
                break;
            default:
                title = "";
                break;
        }
        mTitleTv.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_activity_tab_layout:
                updateFragmentByIndex(ACTIVITY_TAB_INDEX, true);
                break;
            case R.id.ll_tag_tab_layout:
                updateFragmentByIndex(TAG_TAB_INDEX, true);
                break;
            case R.id.ll_discover_layout:
                updateFragmentByIndex(DISCOVER_TAB_INDEX, true);
                break;
            case R.id.ll_message_tab_layout:
                updateFragmentByIndex(MESSAGE_TAB_INDEX, true);
                break;
            case R.id.iv_user_avatar:
                boolean isLonin = false;
                if (isLonin) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                } else {
                    LoginActivity.startAction(this, LoginActivity.LOGIN_TAB);
                }
                break;
            case R.id.iv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.iv_add:

                break;
            default:
                break;
        }
    }
}
