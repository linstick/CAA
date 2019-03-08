package com.luoruiyong.caa.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;

public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener{

    private TextView mTitleTv;
    private TextView mCancelTv;

    private EditText mOriginalPassEt;
    private EditText mNewPassEt;
    private EditText mConfirmNewPassEt;
    private TextView mConfirmTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);

        initView();
    }

    private void initView() {
        mTitleTv = findViewById(R.id.tv_title);
        mTitleTv.setText(getString(R.string.title_modify_password));
        mCancelTv = findViewById(R.id.tv_cancel);
        mCancelTv.setVisibility(View.VISIBLE);

        mOriginalPassEt = findViewById(R.id.et_original_password);
        mNewPassEt = findViewById(R.id.et_new_password);
        mConfirmNewPassEt = findViewById(R.id.et_confirm_new_password);
        mConfirmTv = findViewById(R.id.tv_confirm_modify);

        findViewById(R.id.iv_back).setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
        mConfirmTv.setOnClickListener(this);
    }

    private void checkAndModifyPassword() {
        String originalPass = mOriginalPassEt.getText().toString().trim();
        String newPass = mNewPassEt.getText().toString().trim();
        String confirmNewPass = mConfirmNewPassEt.getText().toString().trim();
        if (TextUtils.isEmpty(originalPass)){
            Toast.makeText(this, getString(R.string.modify_password_tip_empty_original_pass), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPass)){
            Toast.makeText(this, getString(R.string.modify_password_tip_empty_new_pass), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmNewPass)){
            Toast.makeText(this, getString(R.string.modify_password_tip_empty_confirm_new_pass), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.equals(newPass, confirmNewPass)){
            Toast.makeText(this, getString(R.string.modify_password_tip_different_new_pass), Toast.LENGTH_SHORT).show();
            return;
        }

        // request modify password
        Toast.makeText(this, "do request modify", Toast.LENGTH_SHORT).show();
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
            case R.id.tv_confirm_modify:
                checkAndModifyPassword();
                break;
            default:
                break;
        }
    }
}
