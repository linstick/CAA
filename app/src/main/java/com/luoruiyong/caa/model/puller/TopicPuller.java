package com.luoruiyong.caa.model.puller;

import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.eventbus.PullFinishEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class TopicPuller {

    private static final String TAG = "TopicPuller";

    public static void refreshAll(String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_TOPIC_ALL, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreAll(String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_TOPIC_ALL, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSelf(String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_TOPIC_SELF, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSelf(String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_TOPIC_SELF, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshOtherUser(int uid, String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(firstItemTime));
        doPull(Config.PAGE_ID_TOPIC_OTHER_USER, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreOtherUser(int uid, String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_TOPIC_OTHER_USER, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        doPull(Config.PAGE_ID_TOPIC_SEARCH, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSearch(String keyword, String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastItemTime));
        doPull(Config.PAGE_ID_TOPIC_SEARCH, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void doPull(int pageId, int pullType, Map<String, String> params) {
        CommonPuller.doPull(pageId, pullType, Config.URL_TOPIC_FETCH,
                params, new TypeToken<PullFinishEvent<TopicData>>(){}.getType());
    }
}
