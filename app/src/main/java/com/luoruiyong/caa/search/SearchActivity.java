package com.luoruiyong.caa.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.common.fragment.SwipeActivityFragment;
import com.luoruiyong.caa.common.fragment.SwipeDiscoverFragment;
import com.luoruiyong.caa.common.fragment.SwipeTopicFragment;
import com.luoruiyong.caa.search.adapter.SearchTipsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements
        View.OnClickListener,
        SearchTipsAdapter.OnItemClickListener,
        CompositeFragment.OnMoreViewClickListener{


    private final String TAB_COMPOSITE = "tab_composite";
    private final String TAB_USER = "tab_user";
    private final String TAB_ACTIVITY = "tab_activity";
    private final String TAB_TOPIC = "tab_topic";
    private final String TAB_DISCOVER = "tab_discover";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mSearchTv;
    private EditText mInputEt;
    private RecyclerView mSearchTipsRv;

    private List<String> mTipsList;
    private SearchTipsAdapter mTipsAdapter;
    private CompositeFragment mCompositeFragment;
    private SwipeUserFragment mUserFragment;
    private SwipeActivityFragment mActivityFragment;
    private SwipeDiscoverFragment mDiscoverFragment;
    private SwipeTopicFragment mTopicFragment;
    private FragmentManager mFragmentManager;

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
        mSearchTipsRv = findViewById(R.id.rv_search_tips);

        mBackIv.setOnClickListener(this);
        mSearchTv.setOnClickListener(this);

        mInputEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (!TextUtils.isEmpty(text)) {
                    doSearchQuietly(text);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mInputEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    fetchSearchTips();
                }
            }
        });

        mTipsList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            mTipsList.add("春风十里不如你" + (i + 1));
        }
        mTipsAdapter = new SearchTipsAdapter(mTipsList);
        mTipsAdapter.setOnItemClickListener(this);
        mSearchTipsRv.setAdapter(mTipsAdapter);
        mSearchTipsRv.setLayoutManager(new LinearLayoutManager(this));

        mInputEt.requestFocus();
    }

    private void initFragment() {
        mCompositeFragment = new CompositeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, mCompositeFragment, TAB_COMPOSITE).commit();
    }

    private void doSearchQuietly(String text) {
        Toast.makeText(this, "do search quietly", Toast.LENGTH_SHORT).show();
    }

    private void checkAndSearch(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            Toast.makeText(this, getString(R.string.search_tip_empty_search_input), Toast.LENGTH_SHORT).show();
            return;
        }
        // do search
        mInputEt.setText(keyword);
        mSearchTipsRv.setVisibility(View.GONE);
        mInputEt.clearFocus();
        if (mCompositeFragment != null) {
            mCompositeFragment.doSearch(keyword);
        }
    }

    private void fetchSearchTips() {

    }

    private void updateActionBarUI(String title) {
        if (TextUtils.isEmpty(title)) {
            // 搜索页
            mInputEt.setVisibility(View.VISIBLE);
            mSearchTv.setVisibility(View.VISIBLE);
            mTitleTv.setVisibility(View.GONE);
        } else {
            mInputEt.setVisibility(View.GONE);
            mSearchTv.setVisibility(View.GONE);
            mTitleTv.setVisibility(View.VISIBLE);
            mTitleTv.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.et_search_input:
                mSearchTipsRv.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_search:
                String data = mInputEt.getText().toString().trim();
                checkAndSearch(data);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(String data) {
        checkAndSearch(data);
    }

    @Override
    public void onUserMoreClick(String keyword) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (mUserFragment == null) {
            mUserFragment = new SwipeUserFragment();
        }
        mFragmentManager.beginTransaction()
                .addToBackStack(TAB_COMPOSITE)
                .add(R.id.fl_container, mUserFragment, TAB_USER)
                .commit();
        updateActionBarUI(String.format(getString(R.string.title_related_user_for_search), keyword));
    }

    @Override
    public void onActivityMoreClick(String keyword) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (mActivityFragment == null) {
            mActivityFragment = SwipeActivityFragment.newInstance(keyword);
        }
        mFragmentManager.beginTransaction()
                .addToBackStack(TAB_COMPOSITE)
                .add(R.id.fl_container, mActivityFragment, TAB_ACTIVITY)
                .commit();
        updateActionBarUI(String.format(getString(R.string.title_related_activity_for_search), keyword));
    }

    @Override
    public void onTopicMoreClick(String keyword) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (mTopicFragment == null) {
            mTopicFragment = SwipeTopicFragment.newInstance(keyword) ;
        }
        mFragmentManager.beginTransaction()
                .addToBackStack(TAB_COMPOSITE)
                .add(R.id.fl_container, mTopicFragment, TAB_TOPIC)
                .commit();
        updateActionBarUI(String.format(getString(R.string.title_related_topic_for_search), keyword));
    }

    @Override
    public void onDiscoverMoreClick(String keyword) {
        if (mFragmentManager == null) {
            mFragmentManager = getSupportFragmentManager();
        }
        if (mDiscoverFragment == null) {
            mDiscoverFragment = SwipeDiscoverFragment.newInstance(keyword);
        }
        mFragmentManager.beginTransaction()
                .addToBackStack(TAB_COMPOSITE)
                .add(R.id.fl_container, mDiscoverFragment, TAB_DISCOVER)
                .commit();
        updateActionBarUI(String.format(getString(R.string.title_related_discover_for_search), keyword));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateActionBarUI(null);
    }
}
