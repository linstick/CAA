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

    /**
     * 全部话题列表的更新操作
     * @param firstId 列表中第一个话题的编号
     */
    public static void refreshAll(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_TOPIC_ALL, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 全部话题列表的加载更多操作
     * @param lastId 列表中最后一个话题的编号
     */
    public static void loadMoreAll(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_TOPIC_ALL, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 当前用户自己的话题列表的更新操作
     * @param firstId 列表中第一个话题的编号
     */
    public static void refreshSelf(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_TOPIC_SELF, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 当前用户自己的话题列表的更新操作
     * @param lastId 列表中最后一个话题的编号
     */
    public static void loadMoreSelf(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_TOPIC_SELF, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 非当前用户的话题列表的更新操作
     * @param firstId 列表中第一个话题的编号
     */
    public static void refreshOtherUser(int uid, int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_TOPIC_OTHER_USER, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 非当前用户的话题列表的更新操作
     * @param lastId 列表中最后一个话题的编号
     */
    public static void loadMoreOtherUser(int uid, int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_TOPIC_OTHER_USER, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 关键字搜索话题中的更新操作
     * @param keyword 关键字
     */
    public static void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_TOPIC_SEARCH, Config.PULL_TYPE_REFRESH, params);
    }

    /**
     * 关键字搜索话题中的加载更多操作
     * @param keyword 关键字
     * @param offset 偏移量
     */
    public static void loadMoreSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_TOPIC_SEARCH, Config.PULL_TYPE_LOAD_MORE, params);
    }

    /**
     * 发起列表数据拉取请求
     * @param pageId 页面编号，用于区分当前请求是在哪一种活动列表发起的
     * @param pullType 拉取方式，有刷新和加载更多这两种
     * @param params 请求参数
     */
    private static void doPull(int pageId, int pullType, Map<String, String> params) {
        CommonPuller.doPull(pageId, pullType, Config.URL_TOPIC_FETCH,
                params, new TypeToken<PullFinishEvent<TopicData>>(){}.getType());
    }
}
