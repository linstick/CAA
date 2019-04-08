package com.luoruiyong.caa.feedback;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.edit.EditorActivity;

import java.io.Serializable;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_DATA = "key_data";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private ImageView mFinishIv;
    private EditorActivity.OnActionBarClickListener mActionBarListener;

    public static void startAction(Context context, Serializable data) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(KEY_DATA, data);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();

        handleIntent();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mFinishIv = findViewById(R.id.iv_right_operate);

        mTitleTv.setText(R.string.title_feedback);
        mBackIv.setImageResource(R.drawable.ic_clear_white);
        mFinishIv.setImageResource(R.drawable.ic_send_white);
        mFinishIv.setVisibility(View.VISIBLE);
        mBackIv.setOnClickListener(this);
        mFinishIv.setOnClickListener(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        ImpeachFragment fm;
        if (intent == null) {
            fm = new ImpeachFragment();
        } else {
            fm  = ImpeachFragment.newInstance(intent.getSerializableExtra(FeedbackActivity.KEY_DATA));
        }
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, fm).commit();
        mActionBarListener = fm;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (mActionBarListener != null) {
                    mActionBarListener.onBackClick();
                } else {
                    finish();
                }
                break;
            case R.id.iv_right_operate:
                if (mActionBarListener != null) {
                    mActionBarListener.onFinishClick();
                }
                break;
            default:
                break;
        }
    }

    public void setTitle(@StringRes int resId) {
        mTitleTv.setText(resId);
    }
}
