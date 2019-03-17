package com.luoruiyong.caa.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.base.BaseActivity;
import com.luoruiyong.caa.bean.Function;
import com.luoruiyong.caa.utils.DialogHelper;
import com.luoruiyong.caa.user.EditBasicInfoActivity;
import com.luoruiyong.caa.user.ModifyPasswordActivity;
import com.luoruiyong.caa.widget.UniversalFunctionContainer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class SettingsActivity extends BaseActivity implements View.OnClickListener, UniversalFunctionContainer.OnFunctionClickListener{

    private UniversalFunctionContainer mFunctionContainer;
    private List<Function> mFunctionList;


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
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_SIGN, "Edit Profile"));
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_SIGN, "Modify Password"));
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_SIGN, "Feedback"));
        mFunctionList.add(new Function(Function.TYPE_ONLY_WITH_RIGHT_INFO, "Clear Cache"));
        mFunctionList.add(new Function("Check For Update"));
        mFunctionList.add(new Function("About Us"));
        mFunctionList.add(new Function("Logout"));

        mFunctionContainer.setFunctionList(mFunctionList);
        mFunctionContainer.setOnFunctionClickListener(this);

        // for test
        mFunctionContainer.showRightInfo(3, "6.3M");
    }

    private void doClearCache(int position) {
        // do clear
        mFunctionContainer.hideRightInfo(position);
        Toast.makeText(this, R.string.settings_tip_clear_success, Toast.LENGTH_SHORT).show();
    }

    private void doCheckForUpdate(){
        // do check
        Toast.makeText(this, R.string.settings_tip_no_new_version, Toast.LENGTH_SHORT).show();
    }

    private void doLogout() {
        // do logout
        Toast.makeText(this, "do logout", Toast.LENGTH_SHORT).show();

        // for test
        Enviroment.clearCurUser();
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
                startActivity(new Intent(this, EditBasicInfoActivity.class));
                break;
            case 1:
                startActivity(new Intent(this, ModifyPasswordActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, FeedbackActivity.class));
                break;
            case 3:
                doClearCache(position);
                break;
            case 4:
                DialogHelper.showConfirmDialog(
                        this,
                        getString(R.string.settings_str_about_us),
                        getString(R.string.settings_tip_about_us_info),
                        getString(R.string.common_str_ok));
                break;
            case 5:
                doCheckForUpdate();
                break;
            case 6:
                doLogout();
                break;
            default:
                break;
        }
    }
}
