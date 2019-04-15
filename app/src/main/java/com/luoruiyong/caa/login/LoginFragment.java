package com.luoruiyong.caa.login;

import android.content.Context;
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
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.CommonFetcher;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class LoginFragment extends Fragment implements View.OnClickListener{

    private SimpleDraweeView mUserAvatarIv;
    private EditText mAccountEt;
    private EditText mPasswordEt;
    private TextView mLoginTv;
    private TextView mNoAccountTv;
    private TextView mForgetPasswordTv;

    private String mLastCheckAccount;

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
        mPasswordEt = rootView.findViewById(R.id.et_password);
        mLoginTv = rootView.findViewById(R.id.tv_login);
        mNoAccountTv = rootView.findViewById(R.id.tv_no_account);
        mForgetPasswordTv = rootView.findViewById(R.id.tv_forget_password);

        mLoginTv.setOnClickListener(this);
        mNoAccountTv.setOnClickListener(this);
        mForgetPasswordTv.setOnClickListener(this);


        mAccountEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = mAccountEt.getText().toString().trim();
                if (!hasFocus && !TextUtils.isEmpty(text)) {
                    tryFetchAvatar(text);
                }
            }
        });
        mAccountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.equals(s, mLastCheckAccount)) {
                    mUserAvatarIv.setImageURI(Uri.EMPTY);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
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

    private void tryFetchAvatar(String account) {
        if (TextUtils.equals(account, mLastCheckAccount)) {
            return;
        }
        mLastCheckAccount = account;
        mUserAvatarIv.setImageURI(Uri.EMPTY);
        CommonFetcher.doFetchAvatar(account);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent<String> event) {
        switch (event.getType()) {
            case FETCH_AVATAR:
                if (event.getCode() == Config.CODE_OK) {
                    if (TextUtils.equals(mLastCheckAccount, mAccountEt.getText().toString().trim())) {
                        // 头像请求完成之后并且账号信息没改变，更新头像
                        String url = event.getData();
                        mUserAvatarIv.setImageURI(url);
                    }
                }
                break;
            default:
                break;
        }
    }
}
