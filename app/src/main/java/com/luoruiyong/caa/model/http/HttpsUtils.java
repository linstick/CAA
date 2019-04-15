package com.luoruiyong.caa.model.http;

import android.util.Log;

import com.google.gson.Gson;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.eventbus.UserFinishEvent;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.utils.ResourcesUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class HttpsUtils {
    private static final String TAG = "HttpsUtils";

    private static OkHttpClient sClient;

    public static OkHttpClient getClient() {
        if (sClient == null) {
            synchronized (HttpsUtils.class) {
                if (sClient == null) {
                    sClient = new OkHttpClient();
                }
            }
        }
        return sClient;
    }

    public static void sendCommonRequest(RequestType type, Request request, Type reflectType) {
        Log.d(TAG, "sendUserRequest: " + request);
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (type == RequestType.NONE) {
                    return;
                }
                ResponseUtils.handleCommonFailEvent(type, Config.CODE_REQUEST_ERROR, ResourcesUtils.getString(R.string.common_tip_request_error));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (type == RequestType.NONE) {
                    return;
                }
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    CommonEvent event = new Gson().fromJson(data, reflectType);
                    ResponseUtils.handleCommonSuccessEvent(type, event);
                } else {
                    ResponseUtils.handleCommonFailEvent(type, Config.CODE_SERVER_ERROR, ResourcesUtils.getString(R.string.common_tip_server_error));
                }
            }
        });
    }

    public static void sendUserRequest(int type, Request request) {
        Log.d(TAG, "sendUserRequest: " + request);
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponseUtils.handleUserFinishEvent(type, Config.CODE_REQUEST_ERROR, ResourcesUtils.getString(R.string.common_tip_request_error));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    UserFinishEvent event = new Gson().fromJson(data, UserFinishEvent.class);
                    EventBus.getDefault().post(event);
                } else {
                    ResponseUtils.handleUserFinishEvent(type, Config.CODE_SERVER_ERROR, ResourcesUtils.getString(R.string.common_tip_server_error));
                }
            }
        });
    }

    /**
     * 拉取详情数据请求
     * @param request
     * @param type
     */
    public static void sendCommonFetchRequest(Request request, Type type) {
        Log.d(TAG, "sendCommonFetchRequest: " + request);
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponseUtils.handleCommonFetchFailEvent(Config.CODE_REQUEST_ERROR, ResourcesUtils.getString(R.string.common_tip_request_error));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    CommonEvent event = new Gson().fromJson(data, type);
                    EventBus.getDefault().post(event);
                } else {
                    ResponseUtils.handleCommonFetchFailEvent(Config.CODE_SERVER_ERROR, ResourcesUtils.getString(R.string.common_tip_server_error));
                }
            }
        });
    }

    /**
     * 拉取列表数据请求
     * @param targetPage
     * @param pullType
     * @param request
     * @param typeToken
     */
    public static void sendPullRequest(int targetPage, int pullType, Request request, Type typeToken) {
        Log.d(TAG, "sendPullRequest: " + request);
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ResponseUtils.handlePullFailEvent( Config.CODE_REQUEST_ERROR, targetPage, pullType);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    PullFinishEvent event = new Gson().fromJson(data, typeToken);
                    ResponseUtils.handlePullSuccessEvent(event, targetPage, pullType);
                } else {
                    ResponseUtils.handlePullFailEvent(Config.CODE_SERVER_ERROR, targetPage, pullType);
                }
            }
        });
    }

    public static Request buildGetRequestWithParams(String url, Map<String, String> params) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            httpBuilder.addQueryParameter(key, value);
        }
        return new Request.Builder().url(httpBuilder.build()).build();
    }

    public static Request buildPostRequestWithParams(String url, Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            builder.add(key, value);
        }
        return new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
    }
}
