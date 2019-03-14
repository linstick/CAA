package com.luoruiyong.caa.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB = "key_tab";
    public final static String LOGIN_TAB = "login_tab";
    public final static String SIGN_TAB = "sign_tab";
    public final static String FIND_PASSWORD_TAB = "find_password_tab";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mCancelTv;

    private String mCurTab;

    public static void startAction(Context context, String whichTab) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(KEY_TAB, whichTab);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        handleIntent();
    }

    private void initView() {
        mBackIv = findViewById(R.id.iv_back);
        mTitleTv = findViewById(R.id.tv_title);
        mCancelTv = findViewById(R.id.tv_cancel);

        mBackIv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
    }

    private void handleIntent() {
        Intent intent = getIntent();
        String tab = intent == null ? LOGIN_TAB : intent.getStringExtra(KEY_TAB);
        updateFragment(tab);
    }

    public void updateFragment(String tab) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fm = manager.findFragmentByTag(tab);
        if (fm == null) {
            switch (tab) {
                case LOGIN_TAB:
                    fm = new LoginFragment();
                    break;
                case SIGN_TAB:
                    fm = new SignFragment();
                    break;
                case FIND_PASSWORD_TAB:
                    fm = new FindPasswordFragment();
                    break;
                default:
                    break;
            }
        }
        manager.beginTransaction().replace(R.id.fl_container, fm, tab).commit();
        mCurTab = tab;
        String title;
        int cancelVisibleFlag;
        switch (tab) {
            case LOGIN_TAB:
                title = getString(R.string.title_login);
                cancelVisibleFlag = View.GONE;
                break;
            case SIGN_TAB:
                title = getString(R.string.title_sign);
                cancelVisibleFlag = View.VISIBLE;
                break;
            case FIND_PASSWORD_TAB:
                title = getString(R.string.title_find_password);
                cancelVisibleFlag = View.VISIBLE;
                break;
            default:
                title = "";
                cancelVisibleFlag = View.GONE;
                break;
        }
        mTitleTv.setText(title);
        mCancelTv.setVisibility(cancelVisibleFlag);
    }

    public void doLogin(String account, String password) {
        Toast.makeText(this, "doLogin", Toast.LENGTH_SHORT).show();

        // for test
        Enviroment.createVirtualUser();
        Toast.makeText(this, "doLogin", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void doSignUp(String cellPhoneNumber, String nickname, String password) {
        Toast.makeText(this, "doSignUp", Toast.LENGTH_SHORT).show();
    }

    public void doRequestCode(String cellPhoneNumber) {
        Toast.makeText(this, "doRequestCode", Toast.LENGTH_SHORT).show();
    }

    public void doVerifyAuth(String cellPhoneNumber, String code) {
        Toast.makeText(this, "doVerifyAuth", Toast.LENGTH_SHORT).show();
    }

    public void doFetchAvatar(String account) {
        Toast.makeText(this, "doFetchAvatar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.equals(mCurTab, LOGIN_TAB)) {
            updateFragment(LOGIN_TAB);
        } else {
            finish();
        }
    }
}
