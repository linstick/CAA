package com.luoruiyong.caa.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.bean.AdditionData;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.eventbus.CommonOperateEvent;
import com.luoruiyong.caa.model.http.HttpsUtils;
import com.luoruiyong.caa.model.http.RequestType;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/19/019
 * Description: 针对某一种类型（活动/话题/用户）一些操作请求，
 * 如收藏活动，点赞动态
 **/
public class CommonTargetOperator {

    public static void doAddActivityComment(int activityId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT, text);
        doPostOperate(activityId, RequestType.ADD_ACTIVITY_COMMENT, Config.URL_ACTIVITY_ADD_COMMENT, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    public static void doAddActivityAddition(int activityId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION, text);
        doPostOperate(activityId, RequestType.ADD_ACTIVITY_ADDITION, Config.URL_ACTIVITY_ADD_ADDITION, params, new TypeToken<CommonOperateEvent<AdditionData>>(){}.getType());
    }

    public static void doDiscoverComment(int discoverId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT, text);
        doPostOperate(discoverId, RequestType.ADD_DISCOVER_COMMENT, Config.URL_DISCOVER_ADD_COMMENT, params, new TypeToken<CommonOperateEvent<AdditionData>>(){}.getType());
    }

    public static void doDeleteActivityComment(int activityId, int commentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(commentId));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY_COMMENT, Config.URL_ACTIVITY_DELETE_COMMENT, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    public static void doDeleteActivityAddition(int activityId, int additionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(additionId));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY_ADDITION, Config.URL_ACTIVITY_DELETE_ADDITION, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    public static void doDeleteDiscoverComment(int discoverId, int commentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(commentId));
        doGetOperate(discoverId, RequestType.DELETE_DISCOVER_COMMENT, Config.URL_DISCOVER_DELETE_COMMENT, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }


    public static void doCollectActivity(int activityId, boolean isCollect) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_POSITIVE, String.valueOf(isCollect));
        doGetOperate(activityId, RequestType.COLLECT_ACTIVITY, Config.URL_ACTIVITY_COLLECT, params, CommonOperateEvent.class);
    }

    public static void doLikeDiscover(int discoverId, boolean isLike) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_POSITIVE, String.valueOf(isLike));
        doGetOperate(discoverId, RequestType.LIKE_DISCOVER, Config.URL_DISCOVER_LIKE, params, CommonOperateEvent.class);
    }

    public static void doVisitTopic(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId, RequestType.VISIT_TOPIC, Config.URL_TOPIC_VISIT, params, CommonOperateEvent.class);
    }


    public static void doGetOperate(int targetId, RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildGetRequestWithParams(url, params);
        HttpsUtils.sendCommonOperateRequest(targetId, requestType, request, reflectType);
    }

    public static void doPostOperate(int targetId, RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildPostRequestWithParams(url, params);
        HttpsUtils.sendCommonOperateRequest(targetId, requestType, request, reflectType);
    }

}
