package com.luoruiyong.caa.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.feedback.FeedbackActivity;
import com.luoruiyong.caa.home.activity.ActivityFragment;
import com.luoruiyong.caa.home.discover.DiscoverFragment;
import com.luoruiyong.caa.home.message.MessageFragment;
import com.luoruiyong.caa.home.tag.TagFragment;
import com.luoruiyong.caa.login.LoginActivity;
import com.luoruiyong.caa.mine.MineActivity;
import com.luoruiyong.caa.search.SearchActivity;
import com.luoruiyong.caa.settings.SettingsActivity;
import com.luoruiyong.caa.user.EditBasicInfoActivity;
import com.luoruiyong.caa.user.ModifyPasswordActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    private final static String KEY_TAB_INDEX = "tab_index";
    public final static int ACTIVITY_TAB_INDEX = 0;
    public final static int TAG_TAB_INDEX = 1;
    public final static int DISCOVER_TAB_INDEX = 2;
    public final static int MESSAGE_TAB_INDEX = 3;

    private TextView mTitleTv;
    private DrawerLayout mDrawerLayout;
    private NavigationView mSideBarView;
    private ViewPager mViewPager;

    private ImageView mSideBarUserAvatarIv;
    private TextView mSideBarNicknameTv;

    private ImageView mUserAvatarIv;

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

        mSideBarView = findViewById(R.id.view_side_bar);
        FrameLayout headerLayout = (FrameLayout) mSideBarView.getHeaderView(0);
        mSideBarUserAvatarIv = headerLayout.findViewById(R.id.iv_user_avatar);
        mSideBarNicknameTv = headerLayout.findViewById(R.id.tv_nickname);

        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mUserAvatarIv.setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);

        findViewById(R.id.ll_activity_tab_layout).setOnClickListener(this);
        findViewById(R.id.ll_tag_tab_layout).setOnClickListener(this);
        findViewById(R.id.ll_discover_layout).setOnClickListener(this);
        findViewById(R.id.ll_message_tab_layout).setOnClickListener(this);

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        });

        mSideBarView.setNavigationItemSelectedListener(this);
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

    private void doLogout() {
        Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
    }

    private void showEditTypeChooseDialog() {
        Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
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
                boolean isLogin = true;
                if (isLogin) {
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
                showEditTypeChooseDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_my_notes:
                MineActivity.startAction(this, MineActivity.TAB_NOTES);
                break;
            case R.id.menu_str_my_tags:
                MineActivity.startAction(this, MineActivity.TAB_TAGS);
                break;
            case R.id.menu_str_my_collections:
                MineActivity.startAction(this, MineActivity.TAB_COLLECTIONS);
                break;
            case R.id.menu_edit_info:
                startActivity(new Intent(this, EditBasicInfoActivity.class));
                break;

            case R.id.menu_modify_password:
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;

            case R.id.menu_str_setting:
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            case R.id.menu_logout:
                doLogout();
                break;
        }
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
