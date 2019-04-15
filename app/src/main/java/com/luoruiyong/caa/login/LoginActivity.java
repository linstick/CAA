package com.luoruiyong.caa.login;

import android.app.Dialog;
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

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.MyApplication;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.common.dialog.CommonDialog;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.eventbus.LoginStateChangedEvent;
import com.luoruiyong.caa.eventbus.UserFinishEvent;
import com.luoruiyong.caa.model.CommonPoster;
import com.luoruiyong.caa.utils.DialogHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.luoruiyong.caa.eventbus.UserFinishEvent.TYPE_LOGIN;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private final static String KEY_TAB = "key_tab";
    public final static String LOGIN_TAB = "login_tab";
    public final static String SIGN_TAB = "sign_tab";
    public final static String FIND_PASSWORD_TAB = "find_password_tab";

    private ImageView mBackIv;
    private TextView mTitleTv;
    private TextView mCancelTv;
    private Dialog mLoadingDialog;

    private String mCurTab;
    private boolean mHasCancelLogin = true;
    private boolean mHasCancelSignUp = true;

    public static void startAction(Context activity, String whichTab) {
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(KEY_TAB, whichTab);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);

        initView();

        handleIntent();
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
        mHasCancelLogin = false;
        mLoadingDialog = DialogHelper.showLoadingDialog(this, getString(R.string.fm_login_str_on_login), false, new CommonDialog.Builder.OnClickListener() {
            @Override
            public void onClick(String extras) {
                mHasCancelLogin = true;
            }
        });
        CommonPoster.doLogin(account, password);
    }

    public void doSignUp(String account, String nickname, String password) {
        mHasCancelSignUp = false;
        mLoadingDialog = DialogHelper.showLoadingDialog(this, getString(R.string.fm_login_str_on_sign_up), false, new CommonDialog.Builder.OnClickListener() {
            @Override
            public void onClick(String extras) {
                mHasCancelSignUp = true;
            }
        });
        CommonPoster.doSignUp(account, nickname, password);
    }

    public void doRequestCode(String cellPhoneNumber) {
        Toast.makeText(this, "doRequestCode", Toast.LENGTH_SHORT).show();
    }

    public void doVerifyAuth(String cellPhoneNumber, String code) {
        Toast.makeText(this, "doVerifyAuth", Toast.LENGTH_SHORT).show();
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

    private void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCommonEvent(CommonEvent<User> event) {
        switch (event.getType()) {
            case LOGIN:
                if (mHasCancelLogin) {
                    // 用户已经取消登录，不处理结果
                    return;
                }
                hideLoadingDialog();
                if (event.getCode() == Config.CODE_OK) {
                    Enviroment.setCurUser(event.getData());
                    EventBus.getDefault().postSticky(LoginStateChangedEvent.LOGIN_SUCCESS);
                    Toast.makeText(MyApplication.getAppContext(), R.string.fm_login_tip_login_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case SIGN_UP:
                if (mHasCancelSignUp) {
                    // 用户已经点击了取消注册，如果这时接收到注册成功结果，
                    // 这时需要把刚刚注册的用户删除掉
                    if (event.getCode() == Config.CODE_OK) {
                        User user = event.getData();
                        CommonPoster.doSignOut(user.getUid());
                    }
                    return;
                }
                hideLoadingDialog();
                if (event.getCode() == Config.CODE_OK) {
                    // 注册成功，顺便帮用户登录
                    Enviroment.setCurUser(event.getData());
                    EventBus.getDefault().postSticky(LoginStateChangedEvent.LOGIN_SUCCESS);
                    Toast.makeText(MyApplication.getAppContext(), R.string.fm_sign_tip_sign_up_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, event.getStatus(), Toast.LENGTH_SHORT).show();
                }
                break;
            case FETCH_AVATAR:

                break;
            case CHECK_ACCOUNT:

                break;
            case MODIFY_PASSWORD:

                break;
            default:
                break;
        }
    }
}
