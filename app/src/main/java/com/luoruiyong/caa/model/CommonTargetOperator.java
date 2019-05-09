package com.luoruiyong.caa.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.ActivityDynamicData;
import com.luoruiyong.caa.bean.AdditionData;
import com.luoruiyong.caa.bean.CommentData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.DiscoverDynamicData;
import com.luoruiyong.caa.bean.MessageData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.bean.TopicDynamicData;
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

    public static void doDeleteActivity(int activityId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY,
                Config.URL_ACTIVITY_DELETE, params,
                new TypeToken<CommonOperateEvent<ActivityData>>(){}.getType());
    }

    public static void doDeleteDiscover(int discoverId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        doGetOperate(discoverId, RequestType.DELETE_DISCOVER,
                Config.URL_DISCOVER_DELETE, params,
                new TypeToken<CommonOperateEvent<DiscoverData>>(){}.getType());
    }

    public static void doDeleteTopic(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId, RequestType.DELETE_TOPIC,
                Config.URL_TOPIC_DELETE, params,
                new TypeToken<CommonOperateEvent<TopicData>>(){}.getType());
    }

    public static void doDeleteMessage(int messageId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_MESSAGE_ID, String.valueOf(messageId));
        doGetOperate(messageId, RequestType.DELETE_MESSAGE,
                Config.URL_MESSAGE_DELETE, params,
                new TypeToken<CommonOperateEvent<MessageData>>(){}.getType());
    }

    public static void doAddActivityComment(int activityId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_ACTIVITY));
        params.put(Config.PARAM_KEY_COMMENT, text);
        doPostOperate(activityId, RequestType.ADD_ACTIVITY_COMMENT, Config.URL_COMMENT_ADD, params,
                new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    public static void doAddActivityAddition(int activityId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION, text);
        doPostOperate(activityId, RequestType.ADD_ACTIVITY_ADDITION, Config.URL_ADDITION_ADD, params, new TypeToken<CommonOperateEvent<AdditionData>>(){}.getType());
    }

    public static void doAddDiscoverComment(int discoverId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_DISCOVER));
        params.put(Config.PARAM_KEY_COMMENT, text);
        doPostOperate(discoverId, RequestType.ADD_DISCOVER_COMMENT, Config.URL_COMMENT_ADD, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    public static void doDeleteActivityComment(int activityId, int commentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(commentId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_ACTIVITY));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY_COMMENT, Config.URL_COMMENT_DELETE, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    public static void doDeleteActivityAddition(int activityId, int additionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(additionId));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY_ADDITION, Config.URL_ADDITION_DELETE, params, new TypeToken<CommonOperateEvent<AdditionData>>(){}.getType());
    }

    public static void doDeleteDiscoverComment(int discoverId, int commentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(commentId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_DISCOVER));
        doGetOperate(discoverId, RequestType.DELETE_DISCOVER_COMMENT, Config.URL_COMMENT_DELETE, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }


    public static void doCollectActivity(int activityId, boolean isCollect) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        if (isCollect) {
            params.put(Config.PARAM_KEY_POSITIVE, String.valueOf(isCollect));
        }
        doGetOperate(activityId, RequestType.COLLECT_ACTIVITY, Config.URL_ACTIVITY_COLLECT, params, CommonOperateEvent.class);
    }

    public static void doLikeDiscover(int discoverId, boolean isLike) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        if (isLike) {
            params.put(Config.PARAM_KEY_POSITIVE, String.valueOf(isLike));
        }
        doGetOperate(discoverId, RequestType.LIKE_DISCOVER, Config.URL_DISCOVER_LIKE, params, CommonOperateEvent.class);
    }

    public static void doVisitTopic(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId, RequestType.NONE, Config.URL_TOPIC_VISIT, params, CommonOperateEvent.class);
    }

    public static void doFetchActivityDynamicData(int activityId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        doGetOperate(activityId,
                RequestType.FETCH_ACTIVITY_DYNAMIC_DATA,
                Config.URL_ACTIVITY_DYNAMIC, params,
                new TypeToken<CommonOperateEvent<ActivityDynamicData>>(){}.getType());
    }

    public static void doFetchDiscoverDynamicData(int discoverId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        doGetOperate(discoverId,
                RequestType.FETCH_DISCOVER_DYNAMIC_DATA,
                Config.URL_DISCOVER_DYNAMIC, params,
                new TypeToken<CommonOperateEvent<DiscoverDynamicData>>(){}.getType());
    }

    public static void doFetchTopicDynamicData(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId,
                RequestType.FETCH_TOPIC_DYNAMIC_DATA,
                Config.URL_TOPIC_DYNAMIC, params,
                new TypeToken<CommonOperateEvent<TopicDynamicData>>(){}.getType());
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
