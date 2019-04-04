package com.luoruiyong.caa.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.bean.DiscoverData;

/**
 * Author: luoruiyong
 * Date: 2019/4/1/001
 **/
public class DetailActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TYPE = "key_type";
    private final static String KEY_ACTIVITY_DATA = "key_activity_data";
    private final static String KEY_DISCOVER_DATA = "ket_discover_data";

    private final static int TYPE_ACTIVITY_DETAIL = 0;
    private final static int TYPE_DISCOVER_DETAIL = 1;

    private ImageView mBackIv;
    private TextView mTitleTv;

    private int mType;
    private ActivitySimpleData mActivityData;
    private DiscoverData mDiscoverData;

    public static void startAction(Context context, ActivitySimpleData data) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_TYPE, TYPE_ACTIVITY_DETAIL);
        intent.putExtra(KEY_ACTIVITY_DATA, data);
        context.startActivity(intent);
    }

    public static void startAction(Context context, DiscoverData data) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_TYPE, TYPE_DISCOVER_DETAIL);
        intent.putExtra(KEY_DISCOVER_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        initView();

        handleIntent();

        initFragment();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);

        mBackIv.setOnClickListener(this);
    }


    private void handleIntent() {
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        mType = intent.getIntExtra(KEY_TYPE, -1);
        mActivityData = (ActivitySimpleData) intent.getSerializableExtra(KEY_ACTIVITY_DATA);
        mDiscoverData = (DiscoverData) intent.getSerializableExtra(KEY_DISCOVER_DATA);
        if (mActivityData == null && mDiscoverData == null) {
            finish();
            return;
        }
    }

    private void initFragment() {
        Fragment fm = null;
        String title = null;
        switch (mType) {
            case TYPE_DISCOVER_DETAIL:
                fm = DiscoverDetailFragment.newInstance(mDiscoverData);
                title = getString(R.string.title_discover_detail);
                break;
            case TYPE_ACTIVITY_DETAIL:
                fm = ActivityDetailFragment.newInstance(mActivityData);
                title = getString(R.string.title_activity_detail);
                break;
            default:
                break;
        }
        if (fm != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, fm).commit();
            mTitleTv.setText(title);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }
}
