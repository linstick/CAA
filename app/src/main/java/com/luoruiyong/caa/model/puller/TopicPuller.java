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

    public static void refreshAll(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_TOPIC_ALL, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreAll(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_TOPIC_ALL, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSelf(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_TOPIC_SELF, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSelf(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_TOPIC_SELF, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshOtherUser(int uid, int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_TOPIC_OTHER_USER, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreOtherUser(int uid, int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_TOPIC_OTHER_USER, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_TOPIC_SEARCH, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_TOPIC_SEARCH, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void doPull(int pageId, int pullType, Map<String, String> params) {
        CommonPuller.doPull(pageId, pullType, Config.URL_TOPIC_FETCH,
                params, new TypeToken<PullFinishEvent<TopicData>>(){}.getType());
    }
}
