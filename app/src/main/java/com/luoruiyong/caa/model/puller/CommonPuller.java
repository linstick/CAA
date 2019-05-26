package com.luoruiyong.caa.model.puller;

import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.bean.AdditionData;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.bean.MessageData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.model.http.HttpsUtils;
import com.luoruiyong.caa.utils.LogUtils;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

import static com.luoruiyong.caa.Config.DEFAULT_REQUEST_COUNT;

/**
 * Author: luoruiyong
 * Date: 2019/4/11/011
 * Description:
 **/
public class CommonPuller {

    private static final String TAG = "CommonPuller";

    /**
     * 消息列表中的更新操作
     * @param firstId 列表中第一条消息的编号
     */
    public static void refreshMessage(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_MESSAGE_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_MESSAGE, Config.PULL_TYPE_REFRESH, Config.URL_MESSAGE_FETCH,
                params, new TypeToken<PullFinishEvent<MessageData>>(){}.getType());
    }

    /**
     * 消息列表中的加载更多操作
     * @param lastId 列表中最后一条消息的编号
     */
    public static void loadMoreMessage(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_MESSAGE_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_MESSAGE, Config.PULL_TYPE_LOAD_MORE, Config.URL_MESSAGE_FETCH,
                params, new TypeToken<PullFinishEvent<MessageData>>(){}.getType());
    }

    /**
     * 活动评论列表中的刷新操作
     * @param activityId 目标活动的编号
     * @param firstCommentId 列表中第一条评论的编号
     */
    public static void refreshActivityComment(int activityId, int firstCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(firstCommentId));
        doPull(Config.PAGE_ID_ACTIVITY_COMMENT, Config.PULL_TYPE_REFRESH, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    /**
     * 活动评论列表中的加载更多操作
     * @param activityId 目标活动的编号
     * @param lastCommentId 列表中最后一条评论的编号
     */
    public static void loadMoreActivityComment(int activityId, int lastCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(lastCommentId));
        doPull(Config.PAGE_ID_ACTIVITY_COMMENT, Config.PULL_TYPE_LOAD_MORE, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }


    /**
     * 活动补充内容列表中的更新操作
     * @param activityId 目标活动的编号
     * @param firstAdditionId 列表中第一条补充内容的编号
     */
    public static void refreshActivityAddition(int activityId, int firstAdditionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(firstAdditionId));
        doPull(Config.PAGE_ID_ACTIVITY_ADDITION, Config.PULL_TYPE_REFRESH, Config.URL_ADDITION_FETCH,
                params, new TypeToken<PullFinishEvent<AdditionData>>(){}.getType());
    }

    /**
     * 活动补充内容列表中的加载操作
     * @param activityId 目标活动的编号
     * @param lastAdditionId 列表中最后一条补充内容的编号
     */
    public static void loadMoreActivityAddition(int activityId, int lastAdditionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(lastAdditionId));
        doPull(Config.PAGE_ID_ACTIVITY_ADDITION, Config.PULL_TYPE_LOAD_MORE, Config.URL_ADDITION_FETCH,
                params, new TypeToken<PullFinishEvent<AdditionData>>(){}.getType());
    }

    /**
     * 动态评论列表中的更新操作
     * @param discoverId 目标动态的编号
     * @param firstCommentId 列表中第一条评论的编号
     */
    public static void refreshDiscoverComment(int discoverId, int firstCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(firstCommentId));
        doPull(Config.PAGE_ID_DISCOVER_COMMENT, Config.PULL_TYPE_REFRESH, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    /**
     * 动态评论列表中的加载更多操作
     * @param discoverId 目标动态的编号
     * @param lastCommentId 列表中最后一条评论的编号
     */
    public static void loadMoreDiscoverComment(int discoverId, int lastCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(lastCommentId));
        doPull(Config.PAGE_ID_DISCOVER_COMMENT, Config.PULL_TYPE_LOAD_MORE, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    /**
     * 关键字搜索用户列表中的更新操作
     * @param keyword 关键字
     */
    public static void refreshUserSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_USER_SEARCH, Config.PULL_TYPE_REFRESH, Config.URL_USER_PULL,
                params, new TypeToken<PullFinishEvent<User>>(){}.getType());
    }

    /**
     * 关键字搜索用户列表中的加载更多操作
     * @param keyword 关键字
     * @param offset 偏移量
     */
    public static void loadMoreUserSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_USER_SEARCH, Config.PULL_TYPE_LOAD_MORE, Config.URL_USER_PULL,
                params, new TypeToken<PullFinishEvent<User>>(){}.getType());
    }

    /**
     * 发起列表数据拉取请求
     * @param pageId 列表页面编号
     * @param pullType 拉取方式
     * @param url 请求接口
     * @param params 请求参数
     * @param reflectType 响应数据对于的Java类的类型
     */
    public static void doPull(int pageId, int pullType, String url, Map<String, String> params, Type reflectType) {
        params.put(Config.PARAM_KEY_PAGE_ID, String.valueOf(pageId));
        params.put(Config.PARAM_KEY_PULL_TYPE, String.valueOf(pullType));
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_REQUEST_COUNT, String.valueOf(DEFAULT_REQUEST_COUNT));
        Request request = HttpsUtils.buildGetRequestWithParams(url, params);
        HttpsUtils.sendPullRequest(pageId, pullType, request, reflectType);
    }
}
