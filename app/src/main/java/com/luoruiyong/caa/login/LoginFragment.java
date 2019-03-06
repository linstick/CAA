package com.luoruiyong.caa.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.R;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private ImageView mUserAvatarIv;
    private EditText mAccountEt;
    private EditText mPasswordEt;
    private TextView mLoginTv;
    private TextView mNoAccountTv;
    private TextView mForgetPasswordTv;

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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initView(view);
        return view;
    }

    private void initView(View rootView) {
        mUserAvatarIv = rootView.findViewById(R.id.iv_user_avatar);
        mAccountEt = rootView.findViewById(R.id.et_account);
        mPasswordEt = rootView.findViewById(R.id.et_password);
        mLoginTv = rootView.findViewById(R.id.tv_login);
        mNoAccountTv = rootView.findViewById(R.id.tv_no_account);
        mForgetPasswordTv = rootView.findViewById(R.id.tv_forget_password);

        mLoginTv.setOnClickListener(this);
        mNoAccountTv.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);
    }

    private void checkAndLogin() {
        String account = mAccountEt.getText().toString().trim();
        String password = mPasswordEt.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(getContext(), getString(R.string.fm_login_tip_empty_account), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getContext(), getString(R.string.fm_login_tip_empty_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCallBack != null) {
            mCallBack.doLogin(account, password);
        }
    }

    private void checkAndFetchAtavar() {
        String account = mAccountEt.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            return;
        }
        if (mCallBack != null) {
            mCallBack.doFetchAvatar(account);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                checkAndLogin();
                break;
            case R.id.tv_no_account:
                if (mCallBack != null) {
                    mCallBack.updateFragment(LoginActivity.SIGN_TAB);
                }
                break;
            case R.id.tv_forget_password:
                if (mCallBack != null) {
                    mCallBack.updateFragment(LoginActivity.FIND_PASSWORD_TAB);
                }
                break;
            default:
                break;
        }
    }
}
