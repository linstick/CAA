package com.luoruiyong.caa.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;

public class SignFragment extends Fragment implements View.OnClickListener{

    private ImageView mUserAvatarIv;
    private EditText mAccountEt;
    private EditText mNicknameEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;
    private TextView mSignUpTv;
    private TextView mHasAccountTv;

    private LoginActivity mCallBack;

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
    }

    private void checkAndSignUp() {
        String account = mAccountEt.getText().toString().trim();
        String nickname = mNicknameEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(getContext(), getString(R.string.fm_sign_tip_sign_up_success), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getContext(), getString(R.string.fm_sign_tip_empty_nickname), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), getString(R.string.fm_sign_tip_empty_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(getContext(), getString(R.string.fm_sign_tip_empty_confirm_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals(password, confirmPassword)) {
            Toast.makeText(getContext(), getString(R.string.fm_sign_tip_different_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCallBack != null) {
            mCallBack.doSignUp(account, nickname, password);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_user_avatar:
                Toast.makeText(getContext(), "choose picture", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_sign_up:
                checkAndSignUp();
                break;
            case R.id.tv_has_account:
                if (mCallBack != null) {
                    mCallBack.updateFragment(LoginActivity.LOGIN_TAB);
                }
                break;
            default:
                break;
        }
    }
}
