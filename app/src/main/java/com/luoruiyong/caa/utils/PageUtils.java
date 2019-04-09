package com.luoruiyong.caa.utils;

import android.content.Context;
import android.content.Intent;

import com.luoruiyong.caa.user.UserProfileActivity;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description: 页面跳转统一管理
 **/
public class PageUtils {

    public static final String KEY_USER_PROFILE_UID = "KEY_USER_PROFILE_UID";

    /**
     * 个人页跳转条件
     * 1. 用户id
     */
    public static void gotoUserProfilePage(Context context, int uid) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(KEY_USER_PROFILE_UID, uid);
        context.startActivity(intent);
    }

}
