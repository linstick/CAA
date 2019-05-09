package com.luoruiyong.caa.model.puller;

import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.eventbus.PullFinishEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class ActivityPuller{
    private static final String TAG = "ActivityPuller";

    public static void refreshAll(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(Config.PAGE_ID_ACTIVITY_ALL));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_ACTIVITY_ALL, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreAll(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(Config.PAGE_ID_ACTIVITY_ALL));
        params.put(Config.PARAM_KEY_TIME_STAMP, String.valueOf(lastId));
        doPull(Config.PAGE_ID_ACTIVITY_ALL, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshOneKind(int activityType, int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(activityType));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(activityType, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreOneKind(int activityType, int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(activityType));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(activityType, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSelf(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_ACTIVITY_SELF, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSelf(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_ACTIVITY_SELF, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshOtherUser(int uid, int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_ACTIVITY_OTHER_USER, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreOtherUser(int uid, int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_ACTIVITY_OTHER_USER, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSelfCollect(String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, firstItemTime);
        doPull(Config.PAGE_ID_ACTIVITY_SELF_COLLECT, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSelfCollect(String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, lastItemTime);
        doPull(Config.PAGE_ID_ACTIVITY_SELF_COLLECT, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_ACTIVITY_SEARCH, Config.PULL_TYPE_REFRESH, params);
    }

    public static void loadMoreSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_ACTIVITY_SEARCH, Config.PULL_TYPE_LOAD_MORE, params);
    }

    public static void doPull(int pageId, int pullType, Map<String, String> params) {
        CommonPuller.doPull(pageId, pullType, Config.URL_ACTIVITY_FETCH,
                params, new TypeToken<PullFinishEvent<ActivityData>>(){}.getType());
    }
}
