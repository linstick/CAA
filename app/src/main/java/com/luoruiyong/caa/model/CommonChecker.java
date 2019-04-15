package com.luoruiyong.caa.model;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.http.HttpsUtils;
import com.luoruiyong.caa.model.http.RequestType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/15/015
 * Description: 简单查询通用类
 **/
public class CommonChecker {

    public static void doCheckAccount(String account) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACCOUNT, account);
        doCheck(RequestType.CHECK_ACCOUNT, Config.URL_USER_CHECK_ACCOUNT, params);
    }

    public static void doCheck(RequestType requestType, String url, Map<String, String> params) {
        doCheck(requestType, url, params, CommonEvent.class);
    }

    public static void doCheck(RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildGetRequestWithParams(url, params);
        HttpsUtils.sendCommonRequest(requestType, request, reflectType);
    }

}
