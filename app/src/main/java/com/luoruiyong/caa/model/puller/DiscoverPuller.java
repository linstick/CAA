package com.luoruiyong.caa.model.puller;

import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.eventbus.PullFinishEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class DiscoverPuller {

    public static void refreshAll(String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_DISCOVER_ALL, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreAll(String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_DISCOVER_ALL, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSelf(String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_DISCOVER_SELF, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSelf(String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_DISCOVER_SELF, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshOtherUser(int uid, String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_DISCOVER_OTHER_USER, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreOtherUser(int uid, String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_DISCOVER_OTHER_USER, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshTopicHot(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_DISCOVER_TOPIC_HOT, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreTopicHot(int topicId, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_DISCOVER_TOPIC_HOT, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshTopicLasted(int topicId, String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_DISCOVER_TOPIC_LASTED, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreTopicLasted(int topicId, String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_DISCOVER_TOPIC_LASTED, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_DISCOVER_SEARCH, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_DISCOVER_SEARCH, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void doPull(int pageId, int pullType, Map<String, String> params) {
        CommonPuller.doPull(pageId, pullType, Config.URL_DISCOVER_FETCH,
                params, new TypeToken<PullFinishEvent<DiscoverData>>(){}.getType());
    }
}
