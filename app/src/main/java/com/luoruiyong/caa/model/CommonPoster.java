package com.luoruiyong.caa.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.bean.ActivityCreateResult;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.DiscoverCreateResult;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.Feedback;
import com.luoruiyong.caa.bean.ImpeachData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.model.http.FileUploader;
import com.luoruiyong.caa.model.http.RequestType;
import com.luoruiyong.caa.model.http.HttpsUtils;
import com.luoruiyong.caa.utils.ListUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description: 表单数据提交的通用请求类
 **/
public class CommonPoster {

    /**
     * 用户登录
     * @param account 账号
     * @param password 密码
     */
    public static void doLogin(String account, String password) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACCOUNT, account);
        params.put(Config.PARAM_KEY_PASSWORD, password);
        doPost(RequestType.LOGIN, Config.URL_USER_LOGIN, params,
                new TypeToken<CommonEvent<User>>(){}.getType());
    }

    /**
     * 用户注册
     * @param account 账号
     * @param nickname 昵称
     * @param password 密码
     * @param avatarPath 头像资源文件路径，可为空
     */
    public static void doSignUp(String account, String nickname, String password, String avatarPath) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_ACCOUNT, account);
        params.put(Config.PARAM_KEY_NICKNAME, nickname);
        params.put(Config.PARAM_KEY_PASSWORD, password);
        if (!TextUtils.isEmpty(avatarPath)) {
            Request request = new FileUploader.Builder()
                    .filePath(avatarPath)
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .url(Config.URL_USER_SIGN_UP)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.SIGN_UP, request,
                    new TypeToken<CommonEvent<User>>(){}.getType());
        } else {
            doPost(RequestType.SIGN_UP, Config.URL_USER_SIGN_UP, params,
                    new TypeToken<CommonEvent<User>>(){}.getType());
        }
    }

    /**
     * 修改用户资料
     * @param user 修改之后的用户信息
     * @param avatarPath 修改之后的用户头像本地路径
     */
    public static void doModifyProfile(User user, String avatarPath) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_USER, new Gson().toJson(user));
        if (!TextUtils.isEmpty(avatarPath)) {
            Request request = new FileUploader.Builder()
                    .filePath(avatarPath)
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .url(Config.URL_USER_MODIFY_PROFILE)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.MODIFY_PROFILE, request, new TypeToken<CommonEvent<User>>(){}.getType());
        } else {
            doPost(RequestType.MODIFY_PROFILE, Config.URL_USER_MODIFY_PROFILE, params, new TypeToken<CommonEvent<User>>(){}.getType());
        }
    }

    /**
     * 注销用户，主要是用于用户在等待注册时点击了取消
     * 最后如果成功创建了用户账号，需要将其注销
     * @param uid
     */
    public static void doSignOut(int uid) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(uid));
        doPost(RequestType.NONE, Config.URL_USER_SIGN_OUT, params);
    }

    /**
     * 修改用户密码
     * @param password  原密码
     * @param newPassword 新密码
     */
    public static void doModifyPassword(String password, String newPassword) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_PASSWORD, password);
        params.put(Config.PARAM_KEY_NEW_PASSWORD, newPassword);
        doPost(RequestType.MODIFY_PASSWORD, Config.URL_USER_MODIFY_PASSWORD, params);
    }

    /**
     * 创建活动（可能会同时创建一个关联话题）
     * @param activity  带创建的活动信息
     * @param topic 待创建活动所关联的话题信息，可为空
     */
    public static void doCreateActivity(ActivityData activity, TopicData topic) {
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        // 如果是创建活动并创建话题的情况，如果话题有封面，那么会添加到图片文件数组的第一位
        List<String> pictures = new ArrayList<>();
        params.put(Config.PARAM_KEY_ACTIVITY, gson.toJson(activity));
        if (topic != null) {
            params.put(Config.PARAM_KEY_TOPIC, gson.toJson(topic));
            if (!TextUtils.isEmpty(topic.getCover())) {
                pictures.add(topic.getCover());
            }
        }
        if (!ListUtils.isEmpty(activity.getPictureList())) {
            pictures.addAll(activity.getPictureList());
        }
        if (!ListUtils.isEmpty(pictures)) {
            // 图片文件不为空，需要上传
            Request request = new FileUploader.Builder()
                    .url(Config.URL_ACTIVITY_CREATE)
                    .filePaths(pictures)
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.CREATE_ACTIVITY, request, new TypeToken<CommonEvent<ActivityCreateResult>>(){}.getType());
        } else {
            doPost(RequestType.CREATE_ACTIVITY, Config.URL_ACTIVITY_CREATE, params, new TypeToken<CommonEvent<ActivityCreateResult>>(){}.getType());
        }
    }

    /**
     * 创建话题
     * @param topic 待创建的话题信息
     */
    public static void doCreateTopic(TopicData topic) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_TOPIC, new Gson().toJson(topic));
        if (topic.getCover() != null) {
            Request request = new FileUploader.Builder()
                    .url(Config.URL_TOPIC_CREATE)
                    .filePath(topic.getCover())
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.CREATE_TOPIC, request, new TypeToken<CommonEvent<TopicData>>(){}.getType());
         } else {
            doPost(RequestType.CREATE_TOPIC, Config.URL_TOPIC_CREATE, params, new TypeToken<CommonEvent<TopicData>>(){}.getType());
        }
    }

    /**
     * 创建动态（可能会同时创建一个关联话题）
     * @param discover 待创建的动态信息
     * @param topic 待创建的动态所关联的话题信息
     */
    public static void doCreateDiscover(DiscoverData discover, TopicData topic) {
        Map<String, String> params = new HashMap<>();
        Gson gson = new Gson();
        // 如果是创建活动并创建话题的情况，如果话题有封面，那么会添加到图片文件数组的第一位
        List<String> pictures = new ArrayList<>();
        params.put(Config.PARAM_KEY_DISCOVER, gson.toJson(discover));
        if (topic != null) {
            params.put(Config.PARAM_KEY_TOPIC, gson.toJson(topic));
            if (!TextUtils.isEmpty(topic.getCover())) {
                pictures.add(topic.getCover());
            }
        }
        if (!ListUtils.isEmpty(discover.getPictureList())) {
            pictures.addAll(discover.getPictureList());
        }
        if (!ListUtils.isEmpty(pictures)) {
            // 图片文件不为空，需要上传
            Request request = new FileUploader.Builder()
                    .url(Config.URL_DISCOVER_CREATE)
                    .filePaths(pictures)
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.CREATE_DISCOVER, request, new TypeToken<CommonEvent<DiscoverCreateResult>>(){}.getType());
        } else {
            doPost(RequestType.CREATE_DISCOVER, Config.URL_DISCOVER_CREATE, params, new TypeToken<CommonEvent<DiscoverCreateResult>>(){}.getType());
        }
    }

    /**
     * 用户反馈
     * @param data 反馈信息
     */
    public static void doFeedback(Feedback data) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_FEEDBACK, new Gson().toJson(data));
        if (data.getPictureList() != null) {
            Request request = new FileUploader.Builder()
                    .url(Config.URL_IMPROVE_FEEDBACK)
                    .filePaths(data.getPictureList())
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.FEEDBACK, request, CommonEvent.class);
        } else {
            doPost(RequestType.FEEDBACK, Config.URL_IMPROVE_FEEDBACK, params, CommonEvent.class);
        }
    }

    /**
     * 举报
     * @param data 举报信息
     */
    public static void doImpeach(ImpeachData data) {
        Map<String, String> params = new HashMap<>();
        params.put(Config.PARAM_KEY_UID, String.valueOf(Enviroment.getCurUid()));
        params.put(Config.PARAM_KEY_IMPEACH, new Gson().toJson(data));
        if (data.getPictureList() != null) {
            Request request = new FileUploader.Builder()
                    .url(Config.URL_IMPROVE_IMPEACH)
                    .filePaths(data.getPictureList())
                    .fileType(MediaType.parse(Config.FILE_TYPE_IMAGE))
                    .name(Config.UPLOAD_IMAGE_NAME)
                    .params(params)
                    .build();
            HttpsUtils.sendCommonRequest(RequestType.IMPEACH, request, CommonEvent.class);
        } else {
            doPost(RequestType.IMPEACH, Config.URL_IMPROVE_IMPEACH, params, CommonEvent.class);
        }
    }

    public static void doPost(RequestType requestType, String url, Map<String, String> params) {
        doPost(requestType, url, params, CommonEvent.class);
    }

    /**
     * 提交表单
     * @param requestType 请求功能类型
     * @param url 请求的接口
     * @param params 请求的参数
     * @param reflectType 响应结果数据所对应的Java类的类型
     */
    public static void doPost(RequestType requestType, String url, Map<String, String> params, Type reflectType) {
        Request request = HttpsUtils.buildPostRequestWithParams(url, params);
        HttpsUtils.sendCommonRequest(requestType, request, reflectType);
    }

}
