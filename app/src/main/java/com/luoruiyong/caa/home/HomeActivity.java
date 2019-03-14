package com.luoruiyong.caa.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.home.activity.ActivityFragment;
import com.luoruiyong.caa.home.discover.DiscoverFragment;
import com.luoruiyong.caa.home.message.MessageFragment;
import com.luoruiyong.caa.home.tag.TagFragment;
import com.luoruiyong.caa.login.LoginActivity;
import com.luoruiyong.caa.search.SearchActivity;
import com.luoruiyong.caa.user.UserProfileActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB_INDEX = "tab_index";
    public final static int ACTIVITY_TAB_INDEX = 0;
    public final static int TAG_TAB_INDEX = 1;
    public final static int DISCOVER_TAB_INDEX = 2;
    public final static int MESSAGE_TAB_INDEX = 3;

    private TextView mTitleTv;
    private ViewPager mViewPager;

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
        mViewPager = findViewById(R.id.view_pager);

        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mUserAvatarIv.setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);

        findViewById(R.id.ll_activity_tab_layout).setOnClickListener(this);
        findViewById(R.id.ll_tag_tab_layout).setOnClickListener(this);
        findViewById(R.id.ll_discover_layout).setOnClickListener(this);
        findViewById(R.id.ll_message_tab_layout).setOnClickListener(this);
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
                if (Enviroment.isVistor()) {
                    LoginActivity.startAction(this, LoginActivity.LOGIN_TAB);
                } else {
                    startActivity(new Intent(this, UserProfileActivity.class));
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
}
