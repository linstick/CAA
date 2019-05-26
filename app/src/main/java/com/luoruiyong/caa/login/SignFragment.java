package com.luoruiyong.caa.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseFragment;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonChecker;
import com.luoruiyong.caa.utils.KeyboardUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.utils.PictureUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

public class SignFragment extends BaseFragment implements View.OnClickListener{

    private final int CHOOSE_AVATAR_REQUEST_CODE = 1;

    private SimpleDraweeView mUserAvatarIv;
    private EditText mAccountEt;
    private EditText mNicknameEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;
    private TextView mSignUpTv;
    private TextView mHasAccountTv;

    private LoginActivity mCallBack;

    private String mLastAccount;
    private boolean mHasCheckAccount;
    private boolean mIsAccountExists;
    private String mErrorTip;

    private String mAvatarPath;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginActivity) {
            mCallBack = (LoginActivity) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign, container, false);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initView(View rootView) {
        mUserAvatarIv = rootView.findViewById(R.id.iv_user_avatar);
        mAccountEt = rootView.findViewById(R.id.et_account);
        mNicknameEt = rootView.findViewById(R.id.et_nick_name);
        mPasswordEt = rootView.findViewById(R.id.et_password);
        mConfirmPasswordEt = rootView.findViewById(R.id.et_confirm_password);
        mSignUpTv = rootView.findViewById(R.id.tv_sign_up);
        mHasAccountTv = rootView.findViewById(R.id.tv_has_account);

        mUserAvatarIv.setOnClickListener(this);
        mSignUpTv.setOnClickListener(this);
        mHasAccountTv.setOnClickListener(this);

        mAccountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = mAccountEt.getText().toString().trim();
                if (!hasFocus && !TextUtils.isEmpty(text)) {
                    // 失去焦点时，静默检测账号是否存在
                    checkAccountQuietly(text);
                }
            }
        });
        mAccountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString().trim();
                if (mHasCheckAccount && !TextUtils.equals(mLastAccount, text)) {
                    mHasCheckAccount = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void checkAccountQuietly(String account) {
        if (TextUtils.equals(account, mLastAccount)) {
            return;
        }
        mLastAccount = account;
        CommonChecker.doCheckAccount(account);
    }

    private void checkAndSignUp() {
        if (mHasCheckAccount && mIsAccountExists) {
            toast(R.string.fm_sign_tip_account_exist);
            return;
        }
        String account = mAccountEt.getText().toString().trim();
        String nickname = mNicknameEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            toast(R.string.fm_sign_tip_sign_up_success);
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            toast(R.string.fm_sign_tip_empty_nickname);
            return;
        }
        if (TextUtils.isEmpty(password)) {
            toast(R.string.fm_sign_tip_empty_password);
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            toast(R.string.fm_sign_tip_empty_confirm_password);
            return;
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            toast(R.string.fm_sign_tip_different_password);
            return;
        }
        if (mCallBack != null) {
            mCallBack.doSignUp(account, nickname, password, mAvatarPath);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_avatar:
                PageUtils.gotoSystemAlbum(SignFragment.this, CHOOSE_AVATAR_REQUEST_CODE);
                break;
            case R.id.tv_sign_up:
                KeyboardUtils.hideKeyboard(mAccountEt);
                checkAndSignUp();
                break;
            case R.id.tv_has_account:
                mHasAccountTv.requestFocus();
                if (mCallBack != null) {
                    mCallBack.updateFragment(LoginActivity.LOGIN_TAB);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == CHOOSE_AVATAR_REQUEST_CODE) {
                mAvatarPath = PictureUtils.getPath(getContext(), data.getData());
                mUserAvatarIv.setImageURI(Uri.fromFile(new File(mAvatarPath)));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent<Boolean> event) {
        switch (event.getType()) {
            case CHECK_ACCOUNT:
                String text = mAccountEt.getText().toString().trim();
                if (TextUtils.equals(mLastAccount, text) && event.getCode() == Config.CODE_OK) {
                    mHasCheckAccount = true;
                    mIsAccountExists = event.getData();
                }
                break;
            default:
                break;
        }
    }
}
