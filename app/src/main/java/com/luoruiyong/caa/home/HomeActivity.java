package com.luoruiyong.caa.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.adapter.ViewPagerAdapter;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.common.fragment.SwipeTopicFragment;
import com.luoruiyong.caa.eventbus.LoginStateChangedEvent;
import com.luoruiyong.caa.login.LoginActivity;
import com.luoruiyong.caa.model.bean.GlobalSource;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.edit.EditorActivity;
import com.luoruiyong.caa.search.SearchActivity;
import com.luoruiyong.caa.utils.PageUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB_INDEX = "tab_index";
    public final static int ACTIVITY_TAB_INDEX = 0;
    public final static int TAG_TAB_INDEX = 1;
    public final static int DISCOVER_TAB_INDEX = 2;
    public final static int MESSAGE_TAB_INDEX = 3;

    private static long sLastBackPressTime = 0L;

    private TextView mTitleTv;
    private ViewPager mViewPager;

    private SimpleDraweeView mUserAvatarIv;
    private TextView mActivityTabTv;
    private TextView mTagTabTv;
    private TextView mDiscoverTabTv;
    private TextView mMessageTabTv;

    private List<TextView> mBottomTabList;
    private List<Fragment> mFragmentList;
    private ViewPagerAdapter mAdapter;
    private List<String> mEditTypeList;

    private int mCurIndex = ACTIVITY_TAB_INDEX;

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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Enviroment.getCurUser() != null) {
            mUserAvatarIv.setImageURI(Enviroment.getCurUser().getAvatar());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mViewPager = findViewById(R.id.view_pager);
        mUserAvatarIv = findViewById(R.id.iv_user_avatar);
        mActivityTabTv = findViewById(R.id.tv_activity_tab);
        mTagTabTv = findViewById(R.id.tv_tag_tab);
        mDiscoverTabTv = findViewById(R.id.tv_discover_tab);
        mMessageTabTv = findViewById(R.id.tv_message_tab);

        mUserAvatarIv.setOnClickListener(this);
        mActivityTabTv.setOnClickListener(this);
        mTagTabTv.setOnClickListener(this);
        mDiscoverTabTv.setOnClickListener(this);
        mMessageTabTv.setOnClickListener(this);
        findViewById(R.id.iv_search).setOnClickListener(this);
        findViewById(R.id.iv_add).setOnClickListener(this);
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new ActivityFragment());
        mFragmentList.add(SwipeTopicFragment.newInstance());
        mFragmentList.add(SwipeDiscoverFragment.newInstance());
        mFragmentList.add(new MessageFragment());

        mBottomTabList = new ArrayList<>();
        mBottomTabList.add(mActivityTabTv);
        mBottomTabList.add(mTagTabTv);
        mBottomTabList.add(mDiscoverTabTv);
        mBottomTabList.add(mMessageTabTv);

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
        if (mCurIndex != ACTIVITY_TAB_INDEX) {
            mViewPager.setCurrentItem(mCurIndex);
        }
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            int tabIndex = intent.getIntExtra(KEY_TAB_INDEX, ACTIVITY_TAB_INDEX);
            updateFragmentByIndex(tabIndex, false);
        } else {
            updateFragmentByIndex(ACTIVITY_TAB_INDEX, false);
        }
    }

    private void updateFragmentByIndex(int index, boolean needCheck) {
        if (needCheck && (index < 0 || index >= mFragmentList.size() || index == mViewPager.getCurrentItem())) {
            return;
        }
        mCurIndex = index;
        mViewPager.setCurrentItem(index, false);
        String title;
        switch (index) {
            case ACTIVITY_TAB_INDEX:
                title = getString(R.string.title_activity);
                break;
            case TAG_TAB_INDEX:
                title = getString(R.string.title_topic);
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
        for (int i = 0; i < mBottomTabList.size(); i++) {
            if (i == index) {
                mBottomTabList.get(i).setSelected(true);
            } else {
                mBottomTabList.get(i).setSelected(false);
            }
        }
    }

    private void showEditTypeChooseDialog() {
        if (mEditTypeList == null) {
            mEditTypeList = Enviroment.getCreateNewStringArray();
        }
        DialogHelper.showListDialog(this, mEditTypeList, new CommonDialog.Builder.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        EditorActivity.startAction(HomeActivity.this, EditorActivity.TAB_CREATE_ACTIVITY);
                        break;
                    case 1:
                        EditorActivity.startAction(HomeActivity.this, EditorActivity.TAB_CREATE_TOPIC);
                        break;
                    case 2:
                        EditorActivity.startAction(HomeActivity.this, EditorActivity.TAB_CREATE_DISCOVER);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_activity_tab:
                updateFragmentByIndex(ACTIVITY_TAB_INDEX, true);
                break;
            case R.id.tv_tag_tab:
                updateFragmentByIndex(TAG_TAB_INDEX, true);
                break;
            case R.id.tv_discover_tab:
                updateFragmentByIndex(DISCOVER_TAB_INDEX, true);
                break;
            case R.id.tv_message_tab:
                updateFragmentByIndex(MESSAGE_TAB_INDEX, true);
                break;
            case R.id.iv_user_avatar:
                if (Enviroment.isVisitor()) {
                    LoginActivity.startAction(this, LoginActivity.LOGIN_TAB);
                } else {
                    PageUtils.gotoUserProfilePage(this, Enviroment.getCurUid());
                }
                break;
            case R.id.iv_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.iv_add:
                if (Enviroment.isVisitor()) {
                    toast(R.string.fm_login_tip_login_before);
                    LoginActivity.startAction(this, LoginActivity.LOGIN_TAB);
                } else {
                    showEditTypeChooseDialog();
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onLoginStateChangedEvent(LoginStateChangedEvent event) {
        EventBus.getDefault().removeStickyEvent(event);
        GlobalSource.clearAll();
        switch (event) {
            case LOGIN_SUCCESS:
                User curUser = Enviroment.getCurUser();
                if (curUser != null) {
                    mUserAvatarIv.setImageURI(curUser.getAvatar());
                }
                break;
            case LOGOUT_SUCCESS:
                mUserAvatarIv.setImageURI(Uri.EMPTY);
                if (mCurIndex == MESSAGE_TAB_INDEX) {
                    // 回到活动页
                    updateFragmentByIndex(ACTIVITY_TAB_INDEX, false);
                    ((ActivityFragment)mFragmentList.get(ACTIVITY_TAB_INDEX)).forceToAllTab();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - sLastBackPressTime > 1500) {
            sLastBackPressTime = curTime;
            toast(R.string.common_tip_press_again_for_exit);
        } else {
            super.onBackPressed();
        }
    }
}
