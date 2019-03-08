package com.luoruiyong.caa.feedback;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;

public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mCancelTv;

    private LinearLayout mFeedbackContentLayout;
    private EditText mFeedbackInputEt;
    private TextView mSubmitTv;
    private TextView mFeedbackResultTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initView();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mCancelTv = findViewById(R.id.tv_cancel);
        mFeedbackContentLayout = findViewById(R.id.ll_feedback_content);
        mFeedbackInputEt = findViewById(R.id.et_feedback_input);
        mSubmitTv = findViewById(R.id.tv_submit);
        mFeedbackResultTv = findViewById(R.id.tv_feedback_result);

        mTitleTv.setText(R.string.title_feedback);
        mCancelTv.setVisibility(View.VISIBLE);

        mBackIv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
        mSubmitTv.setOnClickListener(this);
    }

    private void checkAndSubmit() {
        String content = mFeedbackInputEt.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, getString(R.string.feedback_tip_empty_content), Toast.LENGTH_SHORT).show();
            return;
        }

        // do feedback
        mFeedbackContentLayout.setVisibility(View.GONE);
        mFeedbackResultTv.setVisibility(View.VISIBLE);
        mCancelTv.setVisibility(View.GONE);
        Toast.makeText(this, "do feedback：" + content, Toast.LENGTH_SHORT).show();
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
            case R.id.tv_submit:
                checkAndSubmit();
                break;
            default:
                break;
        }
    }
}
