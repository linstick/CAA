package com.luoruiyong.caa.model;

import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.CompositeSearchData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.http.HttpsUtils;
import com.luoruiyong.caa.model.http.RequestType;
import com.luoruiyong.caa.topic.TopicSearchResultFragment;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description: 公共的拉取简单数据的通用类
 **/
public class CommonFetcher {

    /**
     * 获取活动详情信息
     * @param activity_id
     */
    public static void doFetchActivityDetail(int activity_id) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activity_id));
        doFetch(RequestType.FETCH_ACTIVITY_DETAIL, Config.URL_ACTIVITY_DETAIL, params,
                new TypeToken<CommonEvent<ActivityData>>(){}.getType());
    }

    /**
     * 获取动态详情信息
     * @param discover_id
     */
    public static void doFetchDiscoverDetail(int discover_id) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discover_id));
        doFetch(RequestType.FETCH_DISCOVER_DETAIL, Config.URL_DISCOVER_DETAIL, params, new TypeToken<CommonEvent<DiscoverData>>(){}.getType());
    }

    /**
     * 获取话题详情信息
     * @param topic_id
     */
    public static void doFetchTopicDetail(int topic_id) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topic_id));
        doFetch(RequestType.FETCH_TOPIC_DETAIL, Config.URL_TOPIC_DETAIL, params, new TypeToken<CommonEvent<TopicData>>(){}.getType());
    }

    /**
     * 获取用户详情信息
     * @param uid
     */
    public static void doFetchOtherUserDetail(int uid) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        doFetch(RequestType.FETCH_USER_DETAIL, Config.URL_USER_DETAIL, params, new TypeToken<CommonEvent<User>>(){}.getType());
    }

    /**
     * 根据用户账号获取头像资源，用于登录界面
     * @param account
     */
    public static void doFetchAvatar(String account) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACCOUNT, account);
        doFetch(RequestType.FETCH_AVATAR, Config.URL_USER_FETCH_AVATAR, params, new TypeToken<CommonEvent<String>>(){}.getType());
    }

    /**
     * 根据关键字动态获取相关话题列表，用于关联话题选择页
     * @param keyword
     */
    public static void doFetchSimpleTopicList(String keyword, int requestCount) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_REQUEST_COUNT, String.valueOf(requestCount));
        doFetch(RequestType.FETCH_SIMPLE_TOPIC_LIST, Config.URL_TOPIC_SIMPLE_LIST, params, new TypeToken<CommonEvent<List<TopicData>>>(){}.getType());
    }

    /**
     * 拉取热门的话题列表，在关联话题页面中使用
     * @param requestCount
     */
    public static void doFetchHotSimpleTopicList(int requestCount) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_REQUEST_COUNT, String.valueOf(requestCount));
        doFetch(RequestType.FETCH_HOT_SIMPLE_TOPIC_LIST, Config.URL_TOPIC_HOT_SIMPLE_LIST, params, new TypeToken<CommonEvent<List<TopicData>>>(){}.getType());
    }

    /**
     * 根据关键字拉取热门相关联的信息列表，包括用户/活动/话题/动态
     * @param keyword
     * @param requestCount
     */
    public static void doFetchCompositeSearchList(String keyword, int requestCount) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_REQUEST_COUNT, String.valueOf(requestCount));
        doFetch(RequestType.FETCH_COMPOSITE_SEARCH_LIST, Config.URL_SEARCH_COMPOSITE, params,
                new TypeToken<CommonEvent<CompositeSearchData>>(){}.getType());
    }

    /**
     * 发起拉取信息请求
     * @param requestType
     * @param url
     * @param params
     * @param reflectType
     */
    public static void doFetch(RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildGetRequestWithParams(url, params);
        HttpsUtils.sendCommonRequest(requestType, request, reflectType);
    }
}
