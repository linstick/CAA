package com.luoruiyong.caa.search;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.CompositeSearchData;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.common.fragment.SwipeTopicFragment;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.utils.KeyboardUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements
        View.OnClickListener,
        CompositeFragment.OnMoreViewClickListener{


    private final String TAB_COMPOSITE = "tab_composite";
    private final String TAB_USER = "tab_user";
    private final String TAB_ACTIVITY = "tab_activity";
    private final String TAB_TOPIC = "tab_topic";
    private final String TAB_DISCOVER = "tab_discover";

    private final int SEARCH_TYPE_ALL = 0;
    private final int SEARCH_TYPE_ACTIVITY = 1;
    private final int SEARCH_TYPE_USER = 2;
    private final int SEARCH_TYPE_TOPIC = 3;
    private final int SEARCH_TYPE_DISCOVER = 4;

    private String mCurTab;

    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mSearchTv;
    private EditText mInputEt;
    private TextView mSearchTypeTv;
    private ImageView mUpAndDownIv;
    private View mInputLayout;

    private CompositeFragment mCompositeFragment;
    private Fragment mActivityFragment;
    private FragmentManager mFragmentManager;

    private String[] mSearchTypeArray;
    private int mSearchType = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

        initFragment();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mInputEt = findViewById(R.id.et_search_input);
        mTitleTv = findViewById(R.id.tv_title);
        mSearchTv = findViewById(R.id.tv_search);
        mSearchTypeTv = findViewById(R.id.tv_search_type);
        mUpAndDownIv = findViewById(R.id.iv_up_and_down);
        mInputLayout = findViewById(R.id.ll_input_layout);

        mBackIv.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);
        mSearchTypeTv.setOnClickListener(this);
        mUpAndDownIv.setOnClickListener(this);
        mSearchTypeTv.setVisibility(View.VISIBLE);
        mUpAndDownIv.setVisibility(View.VISIBLE);

        mInputEt.requestFocus();
        KeyboardUtils.showKeyboard(mInputEt);
    }

    private void initFragment() {
        mCompositeFragment = new CompositeFragment();
        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.fl_container, mCompositeFragment, TAB_COMPOSITE).commit();
        mCurTab = TAB_COMPOSITE;
    }

    private void checkAndSearch(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, getString(R.string.search_tip_empty_search_input), Toast.LENGTH_SHORT).show();
            return;
        }
        // do search
        mInputEt.setText(keyword);
        mInputEt.clearFocus();
        switch (mSearchType) {
            case SEARCH_TYPE_ALL:
               gotoCompositeSearchPage(keyword);
                break;
            case SEARCH_TYPE_USER:
                gotoUserSearchPage(keyword, false);
                break;
            case SEARCH_TYPE_ACTIVITY:
                gotoActivitySearchPage(keyword, false);
                break;
            case SEARCH_TYPE_TOPIC:
                gotoTopicSearchPage(keyword, false);
                break;
            case SEARCH_TYPE_DISCOVER:
                gotoDiscoverSearchPage(keyword, false);
                break;
            default:
                break;
        }
    }

    public void showSearchTypeDialog() {
        if (mSearchTypeArray == null) {
            mSearchTypeArray = ResourcesUtils.getStringArray(R.array.str_array_search_type);
        }
        mUpAndDownIv.setSelected(true);
        new CommonDialog.Builder(this)
                .type(CommonDialog.TYPE_LIST)
                .items(mSearchTypeArray)
                .onItem(new CommonDialog.Builder.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        mUpAndDownIv.setSelected(false);
                        onSearchTypeSelected(position);
                    }
                })
                .onDismiss(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mUpAndDownIv.setSelected(false);
                    }
                })
                .build()
                .show();
    }

    private void onSearchTypeSelected(int type) {
        String searchFor = getString(R.string.search_str_search_for_hint);
        String target;
        switch (type) {
            case SEARCH_TYPE_ALL:
                target = getString(R.string.search_str_search_type_all);
                break;
            case SEARCH_TYPE_ACTIVITY:
                target = getString(R.string.search_str_search_type_activity);
                break;
            case SEARCH_TYPE_USER:
                target = getString(R.string.search_str_search_type_user);
                break;
            case SEARCH_TYPE_TOPIC:
                target = getString(R.string.search_str_search_type_topic);
                break;
            case SEARCH_TYPE_DISCOVER:
                target = getString(R.string.search_str_search_type_discover);
                break;
            default:
                target = "";
                break;
        }
        mSearchType = type;
        mInputEt.setHint(type == 0 ? getString(R.string.search_str_search_default_hint) : String.format(searchFor, target));
        mSearchTypeTv.setText(target);
    }

    private void updateActionBar(String title) {
        if (TextUtils.isEmpty(title)) {
            mInputLayout.setVisibility(View.VISIBLE);
            mTitleTv.setVisibility(View.GONE);
            mSearchTv.setVisibility(View.VISIBLE);
        } else {
            mTitleTv.setText(title);
            mInputLayout.setVisibility(View.GONE);
            mTitleTv.setVisibility(View.VISIBLE);
            mSearchTv.setVisibility(View.GONE);
        }
    }

    private void gotoCompositeSearchPage(String keyword) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (!TextUtils.equals(mCurTab, TAB_COMPOSITE)) {
            // 当前的fragment不是综合搜索的fragment
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStack();
            } else {
                mFragmentManager.beginTransaction()
                        .replace(R.id.fl_container, mCompositeFragment, TAB_COMPOSITE)
                        .commit();
            }
        }
        mCompositeFragment.doSearch(keyword);
        mCurTab = TAB_COMPOSITE;
    }

    private void gotoActivitySearchPage(String keyword, boolean isMoreClick) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (mActivityFragment != null) {
            mActivityFragment = SwipeActivityFragment.newInstance(keyword);
        }
        if (isMoreClick) {
            // 点击查看更多,
            mFragmentManager.beginTransaction()
                    .addToBackStack(TAB_COMPOSITE)
                    .add(R.id.fl_container, SwipeActivityFragment.newInstance(keyword), TAB_ACTIVITY)
                    .commit();
        } else {
            // 直接搜索
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, SwipeActivityFragment.newInstance(keyword), TAB_ACTIVITY)
                    .commit();
        }
        if (isMoreClick) {
            updateActionBar(String.format(getString(R.string.title_related_activity_for_search), keyword));
        }
        mCurTab = TAB_ACTIVITY;
    }

    private void gotoUserSearchPage(String keyword, boolean isMoreClick) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (isMoreClick) {
            mFragmentManager.beginTransaction()
                    .addToBackStack(TAB_COMPOSITE)
                    .add(R.id.fl_container, SwipeUserFragment.newInstance(keyword), TAB_USER)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, SwipeUserFragment.newInstance(keyword), TAB_USER)
                    .commit();
        }
        if (isMoreClick) {
            updateActionBar(String.format(getString(R.string.title_related_user_for_search), keyword));
        }
        mCurTab = TAB_USER;
    }

    private void gotoTopicSearchPage(String keyword, boolean isMoreClick) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (isMoreClick) {
            mFragmentManager.beginTransaction()
                    .addToBackStack(TAB_COMPOSITE)
                    .add(R.id.fl_container, SwipeTopicFragment.newInstance(keyword), TAB_TOPIC)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, SwipeTopicFragment.newInstance(keyword), TAB_TOPIC)
                    .commit();
        }
        if (isMoreClick) {
            updateActionBar(String.format(getString(R.string.title_related_topic_for_search), keyword));
        }
        mCurTab = TAB_TOPIC;
    }

    private void gotoDiscoverSearchPage(String keyword, boolean isMoreClick) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (isMoreClick) {
            mFragmentManager.beginTransaction()
                    .addToBackStack(TAB_COMPOSITE)
                    .add(R.id.fl_container, SwipeDiscoverFragment.newInstance(keyword), TAB_DISCOVER)
                    .commit();
        } else {
            mFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, SwipeDiscoverFragment.newInstance(keyword), TAB_DISCOVER)
                    .commit();
        }
        if (isMoreClick) {
            updateActionBar(String.format(getString(R.string.title_related_discover_for_search), keyword));
        }
        mCurTab = TAB_DISCOVER;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_search:
                String data = mInputEt.getText().toString().trim();
                checkAndSearch(data);
                break;
            case R.id.tv_search_type:
            case R.id.iv_up_and_down:
                showSearchTypeDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public void onUserMoreClick(String keyword) {
       gotoUserSearchPage(keyword, true);
    }

    @Override
    public void onActivityMoreClick(String keyword) {
        gotoActivitySearchPage(keyword, true);
    }

    @Override
    public void onTopicMoreClick(String keyword) {
       gotoTopicSearchPage(keyword, true);
    }

    @Override
    public void onDiscoverMoreClick(String keyword) {
        gotoDiscoverSearchPage(keyword, true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateActionBar(null);
    }
}
