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
 * Description: 活动列表数据拉取类
 **/
public class ActivityPuller{
    private static final String TAG = "ActivityPuller";

    /**
     * 全部活动列表的更新操作
     * @param firstId 列表中第一个活动的编号
     */
    public static void refreshAll(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(Config.PAGE_ID_ACTIVITY_ALL));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_ACTIVITY_ALL, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 全部活动列表的加载更多操作
     * @param lastId 列表中最后一个活动的编号
     */
    public static void loadMoreAll(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(Config.PAGE_ID_ACTIVITY_ALL));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_ACTIVITY_ALL, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 某一种活动列表中的更新操作
     * @param activityType 活动类型
     * @param firstId 列表中第一个活动的编号
     */
    public static void refreshOneKind(int activityType, int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(activityType));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(activityType, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 某一种活动列表中的加载更多操作
     * @param activityType 活动类型
     * @param lastId 列表中最后一个活动的编号
     */
    public static void loadMoreOneKind(int activityType, int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TYPE, String.valueOf(activityType));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(activityType, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 当前用户自己的活动列表的更新操作
     * @param firstId 列表中第一个活动的编号
     */
    public static void refreshSelf(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_ACTIVITY_SELF, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 当前用户自己的活动列表的加载更多操作
     * @param lastId 列表中最后一个活动的编号
     */
    public static void loadMoreSelf(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_ACTIVITY_SELF, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 非当前用户的活动列表的更新操作
     * @param firstId 列表中第一个活动的编号
     */
    public static void refreshOtherUser(int uid, int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_ACTIVITY_OTHER_USER, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 非当前用户的活动列表的加载更多操作
     * @param lastId 列表中最后一个活动的编号
     */
    public static void loadMoreOtherUser(int uid, int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_ACTIVITY_OTHER_USER, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 当前用户所收藏的活动列表的更新操作
     * @param firstItemTime 列表中第一个活动的收藏时间
     */
    public static void refreshSelfCollect(String firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, firstItemTime);
        doPull(Config.PAGE_ID_ACTIVITY_SELF_COLLECT, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 当前用户所收藏的活动列表的更新操作
     * @param lastItemTime 列表中最后一个活动的收藏时间
     */
    public static void loadMoreSelfCollect(String lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TIME_STAMP, lastItemTime);
        doPull(Config.PAGE_ID_ACTIVITY_SELF_COLLECT, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 关键字搜索活动中的更新操作
     * @param keyword 关键字
     */
    public static void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_ACTIVITY_SEARCH, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 关键字搜索活动中的加载更多操作
     * @param keyword 关键字
     * @param offset 偏移量
     */
    public static void loadMoreSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_ACTIVITY_SEARCH, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 发起列表数据拉取请求
     * @param pageId 页面编号，用于区分当前请求是在哪一种活动列表发起的
     * @param pullType 拉取方式，有刷新和加载更多这两种
     * @param params 请求参数
     */
    private static void doPull(int pageId, int pullType, Map<String, String> params) {
        CommonPuller.doPull(pageId, pullType, Config.URL_ACTIVITY_FETCH,
                params, new TypeToken<PullFinishEvent<ActivityData>>(){}.getType());
    }
}
