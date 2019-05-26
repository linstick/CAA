package com.luoruiyong.caa.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.Function;
import com.luoruiyong.caa.eventbus.LoginStateChangedEvent;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.user.ModifyPasswordActivity;
import com.luoruiyong.caa.utils.FileUtils;
import com.luoruiyong.caa.utils.PageUtils;
import com.luoruiyong.caa.widget.UniversalFunctionContainer;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class SettingsActivity extends BaseActivity implements View.OnClickListener, UniversalFunctionContainer.OnFunctionClickListener{

    private UniversalFunctionContainer mFunctionContainer;
    private List<Function> mFunctionList;

    private long mCacheSize = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
    }

    private void initView() {
        mFunctionContainer = findViewById(R.id.scroll_view_function_container);
        findViewById(R.id.iv_back).setOnClickListener(this);
        ((TextView)findViewById(R.id.tv_title)).setText(R.string.title_settings);

        mFunctionList = new ArrayList<>();
        // 修改密码
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_SIGN, getString(R.string.title_modify_password)));

        // 用户反馈
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_SIGN, getString(R.string.settings_str_feedback)));

        // 清理缓存
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_INFO, getString(R.string.settings_str_clear_cache)));

        // 关于我们
        mFunctionList.add(new Function(getString(R.string.settings_str_about_us)));

        // 退出登录
        mFunctionList.add(new Function(getString(R.string.settings_str_logout)));

        mFunctionContainer.setFunctionList(mFunctionList);
        mFunctionContainer.setOnFunctionClickListener(this);

        // 缓存大小
        mCacheSize = FileUtils.getFileSizes(new File(Enviroment.getCacheFolder()));
        mFunctionContainer.showRightInfo(2, FileUtils.formatFileSize(mCacheSize));
    }

    private void doClearCache(int position) {
        // do clear
        if (mCacheSize > 0) {
            FileUtils.deleteFiles(new File(Enviroment.getCacheFolder()));
            mCacheSize = 0;
            mFunctionContainer.showRightInfo(2, FileUtils.formatFileSize(mCacheSize));
            toast(R.string.settings_tip_clear_success);
        } else {
            toast(R.string.settings_tip_no_cache_data);
        }
    }

    private void doLogout() {
        Enviroment.clearCurUser();
        EventBus.getDefault().postSticky(LoginStateChangedEvent.LOGOUT_SUCCESS);
        toast(R.string.settings_str_has_logout);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFunctionClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;
//            case 1:
//                Toast.makeText(this, "该功能正在开发，敬请期待", Toast.LENGTH_SHORT).show();
//                break;
//            case 2:
//                Toast.makeText(this, "该功能正在开发，敬请期待", Toast.LENGTH_SHORT).show();
//                break;
            case 1:
                PageUtils.gotoFeedbackPage(this);
                break;
            case 2:
                doClearCache(position);
                break;
            case 3:
                DialogHelper.showConfirmDialog(
                        this,
                        getString(R.string.settings_str_about_us),
                        getString(R.string.settings_tip_about_us_info),
                        getString(R.string.common_str_ok));
                break;
            case 4:
                doLogout();
                break;
            default:
                break;
        }
    }
}
