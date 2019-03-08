package com.luoruiyong.caa.profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;

public class EditProfileActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTitleTv;
    private TextView mFinishTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initView();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mFinishTv = findViewById(R.id.tv_cancel);

        mTitleTv.setText(getString(R.string.title_edit_profile));
        mFinishTv.setText(getString(R.string.common_str_finish));
        mFinishTv.setVisibility(View.VISIBLE);

        findViewById(R.id.iv_back).setOnClickListener(this);
        mFinishTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            default:
                break;
        }

    }
}
