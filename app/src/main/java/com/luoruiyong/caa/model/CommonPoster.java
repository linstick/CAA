package com.luoruiyong.caa.model;

import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.eventbus.UserFinishEvent;
import com.luoruiyong.caa.model.http.RequestType;
import com.luoruiyong.caa.model.http.HttpsUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description: 表单数据提交的通用请求类
 **/
public class CommonPoster {

    /**
     * 用户登录
     * @param account
     * @param password
     */
    public static void doLogin(String account, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACCOUNT, account);
        params.put(Config.PARAM_KEY_PASSWORD, password);
        doPost(RequestType.LOGIN, Config.URL_USER_LOGIN, params, new TypeToken<CommonEvent<User>>(){}.getType());
    }

    /**
     * 用户注册
     * @param account
     * @param nickname
     * @param password
     */
    public static void doSignUp(String account, String nickname, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACCOUNT, account);
        params.put(Config.PARAM_KEY_NICKNAME, nickname);
        params.put(Config.PARAM_KEY_PASSWORD, password);
        doPost(RequestType.SIGN_UP, Config.URL_USER_SIGN_UP, params, new TypeToken<CommonEvent<User>>(){}.getType());
    }

    /**
     * 注销用户，主要是用于用户在等待注册时点击了取消
     * 最后如果成功创建了用户账号，需要将其注销
     * @param uid
     */
    public static void doSignOut(int uid) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(uid));
        doPost(RequestType.NONE, Config.URL_USER_SIGN_OUT, params);
    }

    public static void doModifyPassword(int uid, String password, String newPassword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_PASSWORD, password);
        params.put(Config.PARAM_KEY_NEW_PASSWORD, newPassword);
        doPost(RequestType.MODIFY_PASSWORD, Config.URL_USER_MODIFY_PASSWORD, params);
    }

    public static void doPost(RequestType requestType, String url, Map<String, String> params) {
        doPost(requestType, url, params, CommonEvent.class);
    }

    public static void doPost(RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildPostRequestWithParams(url, params);
        HttpsUtils.sendCommonRequest(requestType, request, reflectType);
    }


}
