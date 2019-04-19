package com.luoruiyong.caa.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonFetcher;
import com.luoruiyong.caa.model.CommonPoster;
import com.luoruiyong.caa.utils.DialogHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
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
        if (!TextUtils.equals(newPass, confirmNewPass)){
            Toast.makeText(this, getString(R.string.modify_password_tip_different_new_pass), Toast.LENGTH_SHORT).show();
            return;
        }
        DialogHelper.showLoadingDialog(this, getString(R.string.modify_password_str_on_modify), false);
        CommonPoster.doModifyPassword(originalPass, newPass);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent event) {
        switch (event.getType()) {
            case MODIFY_PASSWORD:
                if (event.getCode() == Config.CODE_OK) {
                    Toast.makeText(MyApplication.getAppContext(), R.string.modify_password_tip_modify_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
