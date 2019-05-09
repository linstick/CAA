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

    public static void refreshMessage(int firstId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_MESSAGE_ID, String.valueOf(firstId));
        doPull(Config.PAGE_ID_MESSAGE, Config.PULL_TYPE_REFRESH, Config.URL_MESSAGE_FETCH,
                params, new TypeToken<PullFinishEvent<MessageData>>(){}.getType());
    }

    public static void loadMoreMessage(int lastId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_MESSAGE_ID, String.valueOf(lastId));
        doPull(Config.PAGE_ID_MESSAGE, Config.PULL_TYPE_LOAD_MORE, Config.URL_MESSAGE_FETCH,
                params, new TypeToken<PullFinishEvent<MessageData>>(){}.getType());
    }

    public static void refreshActivityComment(int activityId, int firstCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(firstCommentId));
        doPull(Config.PAGE_ID_ACTIVITY_COMMENT, Config.PULL_TYPE_REFRESH, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    public static void loadMoreActivityComment(int activityId, int lastCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(lastCommentId));
        doPull(Config.PAGE_ID_ACTIVITY_COMMENT, Config.PULL_TYPE_LOAD_MORE, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    public static void refreshActivityAddition(int activityId, int firstAdditionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(firstAdditionId));
        doPull(Config.PAGE_ID_ACTIVITY_ADDITION, Config.PULL_TYPE_REFRESH, Config.URL_ADDITION_FETCH,
                params, new TypeToken<PullFinishEvent<AdditionData>>(){}.getType());
    }

    public static void loadMoreActivityAddition(int activityId, int lastAdditionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(lastAdditionId));
        doPull(Config.PAGE_ID_ACTIVITY_ADDITION, Config.PULL_TYPE_LOAD_MORE, Config.URL_ADDITION_FETCH,
                params, new TypeToken<PullFinishEvent<AdditionData>>(){}.getType());
    }

    public static void refreshDiscoverComment(int discoverId, int firstCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(firstCommentId));
        doPull(Config.PAGE_ID_DISCOVER_COMMENT, Config.PULL_TYPE_REFRESH, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    public static void loadMoreDiscoverComment(int discoverId, int lastCommentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(lastCommentId));
        doPull(Config.PAGE_ID_DISCOVER_COMMENT, Config.PULL_TYPE_LOAD_MORE, Config.URL_COMMENT_FETCH,
                params, new TypeToken<PullFinishEvent<CommentData>>(){}.getType());
    }

    public static void refreshUserSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(0));
        doPull(Config.PAGE_ID_USER_SEARCH, Config.PULL_TYPE_REFRESH, Config.URL_USER_PULL,
                params, new TypeToken<PullFinishEvent<User>>(){}.getType());
    }

    public static void loadMoreUserSearch(String keyword, int offset) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_KEYWORD, keyword);
        params.put(Config.PARAM_KEY_OFFSET, String.valueOf(offset));
        doPull(Config.PAGE_ID_USER_SEARCH, Config.PULL_TYPE_LOAD_MORE, Config.URL_USER_PULL,
                params, new TypeToken<PullFinishEvent<User>>(){}.getType());
    }

    public static void doPull(int pageId, int pullType, String url, Map<String, String> params, Type type) {
        params.put(Config.PARAM_KEY_PAGE_ID, String.valueOf(pageId));
        params.put(Config.PARAM_KEY_PULL_TYPE, String.valueOf(pullType));
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_REQUEST_COUNT, String.valueOf(DEFAULT_REQUEST_COUNT));
        Request request = HttpsUtils.buildGetRequestWithParams(url, params);
        HttpsUtils.sendPullRequest(pageId, pullType, request, type);
    }
}
