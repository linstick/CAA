package com.luoruiyong.caa.model;

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
 * 如收藏活动，点赞动态，删除活动，删除动态等
 **/
public class CommonTargetOperator {

    /**
     * 删除指定的活动
     * @param activityId 待删除的活动编号
     */
    public static void doDeleteActivity(int activityId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY,
                Config.URL_ACTIVITY_DELETE, params,
                new TypeToken<CommonOperateEvent<ActivityData>>(){}.getType());
    }

    /**
     * 删除指定的动态
     * @param discoverId 待删除的动态编号
     */
    public static void doDeleteDiscover(int discoverId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        doGetOperate(discoverId, RequestType.DELETE_DISCOVER,
                Config.URL_DISCOVER_DELETE, params,
                new TypeToken<CommonOperateEvent<DiscoverData>>(){}.getType());
    }

    /**
     * 删除指定的话题
     * @param topicId 待删除的话题编号
     */
    public static void doDeleteTopic(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId, RequestType.DELETE_TOPIC,
                Config.URL_TOPIC_DELETE, params,
                new TypeToken<CommonOperateEvent<TopicData>>(){}.getType());
    }

    /**
     * 删除指定的消息信息
     * @param messageId 待删除的消息编号
     */
    public static void doDeleteMessage(int messageId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_MESSAGE_ID, String.valueOf(messageId));
        doGetOperate(messageId, RequestType.DELETE_MESSAGE,
                Config.URL_MESSAGE_DELETE, params,
                new TypeToken<CommonOperateEvent<MessageData>>(){}.getType());
    }

    /**
     * 添加活动评论
     * @param activityId 目标活动编号
     * @param text 评论内容
     */
    public static void doAddActivityComment(int activityId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_ACTIVITY));
        params.put(Config.PARAM_KEY_COMMENT, text);
        doPostOperate(activityId, RequestType.ADD_ACTIVITY_COMMENT, Config.URL_COMMENT_ADD, params,
                new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    /**
     * 补充活动内容
     * @param activityId 目标活动编号
     * @param text 补充内容
     */
    public static void doAddActivityAddition(int activityId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        params.put(Config.PARAM_KEY_ADDITION, text);
        doPostOperate(activityId, RequestType.ADD_ACTIVITY_ADDITION, Config.URL_ADDITION_ADD, params, new TypeToken<CommonOperateEvent<AdditionData>>(){}.getType());
    }

    /**
     * 添加动态评论
     * @param discoverId 目标动态编号
     * @param text 评论内容
     */
    public static void doAddDiscoverComment(int discoverId, String text) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_DISCOVER));
        params.put(Config.PARAM_KEY_COMMENT, text);
        doPostOperate(discoverId, RequestType.ADD_DISCOVER_COMMENT, Config.URL_COMMENT_ADD, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    /**
     * 删除指定的活动评论内容
     * @param activityId 评论所属的活动编号
     * @param commentId 评论编号
     */
    public static void doDeleteActivityComment(int activityId, int commentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(commentId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_ACTIVITY));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY_COMMENT, Config.URL_COMMENT_DELETE, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    /**
     * 删除指定的活动补充内容
     * @param activityId 补充内容所属的活动编号
     * @param additionId 补充内容编号
     */
    public static void doDeleteActivityAddition(int activityId, int additionId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ADDITION_ID, String.valueOf(additionId));
        doGetOperate(activityId, RequestType.DELETE_ACTIVITY_ADDITION, Config.URL_ADDITION_DELETE, params, new TypeToken<CommonOperateEvent<AdditionData>>(){}.getType());
    }

    /**
     * 删除指定的动态评论
     * @param discoverId 评论所属动态的编号
     * @param commentId 评论编号
     */
    public static void doDeleteDiscoverComment(int discoverId, int commentId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_COMMENT_ID, String.valueOf(commentId));
        params.put(Config.PARAM_KEY_COMMENT_TYPE, String.valueOf(Config.COMMENT_TYPE_DISCOVER));
        doGetOperate(discoverId, RequestType.DELETE_DISCOVER_COMMENT, Config.URL_COMMENT_DELETE, params, new TypeToken<CommonOperateEvent<CommentData>>(){}.getType());
    }

    /**
     * 收藏/取消收藏指定的活动
     * @param activityId 目标活动的编号
     * @param isCollect 是否是收藏
     */
    public static void doCollectActivity(int activityId, boolean isCollect) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        if (isCollect) {
            params.put(Config.PARAM_KEY_POSITIVE, String.valueOf(isCollect));
        }
        doGetOperate(activityId, RequestType.COLLECT_ACTIVITY, Config.URL_ACTIVITY_COLLECT, params, CommonOperateEvent.class);
    }

    /**
     * 点赞/取消点赞指定的动态
     * @param discoverId 目标动态的编号
     * @param isLike 是否是点赞
     */
    public static void doLikeDiscover(int discoverId, boolean isLike) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        if (isLike) {
            params.put(Config.PARAM_KEY_POSITIVE, String.valueOf(isLike));
        }
        doGetOperate(discoverId, RequestType.LIKE_DISCOVER, Config.URL_DISCOVER_LIKE, params, CommonOperateEvent.class);
    }

    /**
     * 记录用户浏览过指定话题
     * @param topicId 目标话题的编号
     */
    public static void doVisitTopic(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId, RequestType.NONE, Config.URL_TOPIC_VISIT, params, CommonOperateEvent.class);
    }

    /**
     * 进入活动详情页面时，收藏数量、评论数量和补充内容数量需要重新拉取
     * @param activityId 目标活动的编号
     */
    public static void doFetchActivityDynamicData(int activityId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACTIVITY_ID, String.valueOf(activityId));
        doGetOperate(activityId,
                RequestType.FETCH_ACTIVITY_DYNAMIC_DATA,
                Config.URL_ACTIVITY_DYNAMIC, params,
                new TypeToken<CommonOperateEvent<ActivityDynamicData>>(){}.getType());
    }

    /**
     * 进入动态详情页面时，点赞数量和评论数量需要重新拉取
     * @param discoverId 目标动态的编号
     */
    public static void doFetchDiscoverDynamicData(int discoverId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_DISCOVER_ID, String.valueOf(discoverId));
        doGetOperate(discoverId,
                RequestType.FETCH_DISCOVER_DYNAMIC_DATA,
                Config.URL_DISCOVER_DYNAMIC, params,
                new TypeToken<CommonOperateEvent<DiscoverDynamicData>>(){}.getType());
    }

    /**
     * 进入话题详情页面是，话题参与数量和浏览数量需要重新拉取
     * @param topicId 目标话题的编号
     */
    public static void doFetchTopicDynamicData(int topicId) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doGetOperate(topicId,
                RequestType.FETCH_TOPIC_DYNAMIC_DATA,
                Config.URL_TOPIC_DYNAMIC, params,
                new TypeToken<CommonOperateEvent<TopicDynamicData>>(){}.getType());
    }

    /**
     * 发起Get请求，如点赞动态、收藏活动操作
     * @param targetId 目标操作对象的编号
     * @param requestType 请求功能类型
     * @param url 请求接口
     * @param params 请求参数
     * @param reflectType 响应数据对应的Java类的类型
     */
    public static void doGetOperate(int targetId, RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildGetRequestWithParams(url, params);
        HttpsUtils.sendCommonOperateRequest(targetId, requestType, request, reflectType);
    }

    /**
     * 发起Post请求，如添加话题评论、添加动态评论操作
     * @param targetId 目标操作对象的编号
     * @param requestType 请求功能类型
     * @param url 请求接口
     * @param params 请求参数
     * @param reflectType 响应数据对应的Java类的类型
     */
    public static void doPostOperate(int targetId, RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildPostRequestWithParams(url, params);
        HttpsUtils.sendCommonOperateRequest(targetId, requestType, request, reflectType);
    }

}
