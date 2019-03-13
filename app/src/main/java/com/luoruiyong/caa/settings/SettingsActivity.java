package com.luoruiyong.caa.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.dialog.common.DialogHelper;
import com.luoruiyong.caa.feedback.FeedbackActivity;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class SettingsActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private TextView mTitleTv;
    private LinearLayout mFeedbackLayout;
    private LinearLayout mClearCacheLayout;
    private TextView mCacheSizeTv;
    private TextView mAboutUsTv;
    private TextView mCheckUpdateTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mFeedbackLayout = findViewById(R.id.ll_feedback_layout);
        mClearCacheLayout = findViewById(R.id.ll_clear_cache_layout);
        mCacheSizeTv = findViewById(R.id.tv_cache_size);
        mAboutUsTv = findViewById(R.id.tv_about_us);
        mCheckUpdateTv = findViewById(R.id.tv_check_for_update);

        mTitleTv.setText(R.string.title_settings);

        mBackIv.setOnClickListener(this);
        mFeedbackLayout.setOnClickListener(this);
        mClearCacheLayout.setOnClickListener(this);
        mAboutUsTv.setOnClickListener(this);
        mCheckUpdateTv.setOnClickListener(this);
    }

    private void doClearCache() {
        // do clear
        mCacheSizeTv.setText("");
        Toast.makeText(this, R.string.settings_tip_clear_success, Toast.LENGTH_SHORT).show();
    }

    private void doCheckForUpdate(){
        // do check
        Toast.makeText(this, R.string.settings_tip_no_new_version, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_feedback_layout:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case R.id.ll_clear_cache_layout:
                doClearCache();
                break;
            case R.id.tv_about_us:
                DialogHelper.showConfirmDialog(
                        this,
                        getString(R.string.settings_str_about_us),
                        getString(R.string.settings_tip_about_us_info),
                        getString(R.string.common_str_ok));
                break;
            case R.id.tv_check_for_update:
                doCheckForUpdate();
                break;
        }
    }
}
