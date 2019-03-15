package com.luoruiyong.caa.edit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class EditorActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB = "key_tab";
    public final static String TAB_CREATE_ACTIVITY = "create_activity";
    public final static String TAB_CREATE_TAG = "create_tag";
    public final static String TAB_CREATE_DISCOVER = "create_discover";

    private ImageView mBackIv;
    private TextView mTitleTv;

    public static void startAction(Context context, String whichTab) {
        Intent intent = new Intent(context, EditorActivity.class);
        intent.putExtra(KEY_TAB, whichTab);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        initView();

        handleIntent();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);

        mBackIv.setOnClickListener(this);
    }

    private void handleIntent() {
        String tab;
        Fragment fm;
        int titleResId;
        Intent intent = getIntent();
        tab = intent == null ? TAB_CREATE_ACTIVITY : intent.getStringExtra(KEY_TAB);
        switch (tab) {
            case TAB_CREATE_DISCOVER:
                fm = new CreateDiscoverFragment();
                titleResId = R.string.title_create_discover;
                break;
            case TAB_CREATE_TAG:
                fm = new CreateTagFragment();
                titleResId = R.string.title_create_tag;
                break;
            case TAB_CREATE_ACTIVITY:
            default:
                fm = new CreateActivityFragment();
                titleResId = R.string.title_create_activity;
                break;

        }
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fm).commit();
        mTitleTv.setText(titleResId);
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