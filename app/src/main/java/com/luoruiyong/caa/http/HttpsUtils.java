package com.luoruiyong.caa.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.bean.TopicSimpleData;
import com.luoruiyong.caa.puller.IPuller;
import com.luoruiyong.caa.puller.bean.PullResponse;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
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

    private static int RESPONSE_STATUS_OK = 0;
    private static int RESPONSE_STATUS_ERROR = -1;

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

    public static void sendActivityRefreshPullRequest(Request request, final IPuller.RefreshCallback<ActivitySimpleData> callback) {
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onRefreshFail(ResourcesUtils.getString(R.string.common_tip_no_network));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    PullResponse pullResponse = gson.fromJson(data, new TypeToken<PullResponse<ActivitySimpleData>>(){}.getType());
                    if (pullResponse.getCode() == RESPONSE_STATUS_OK) {
                        callback.onRefreshSuccess(pullResponse.getRequestType(), pullResponse.getData());
                    } else {
                        callback.onRefreshFail(pullResponse.getStatus());
                    }
                } else {
                    callback.onRefreshFail("Unknow Error");
                }
            }
        });
    }

    public static void sendActivityLoadMorePullRequest(Request request, final IPuller.LoadMoreCallback<ActivitySimpleData> callback) {
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onLoadMoreFail(ResourcesUtils.getString(R.string.common_tip_no_network));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    PullResponse pullResponse = gson.fromJson(data, new TypeToken<PullResponse<ActivitySimpleData>>(){}.getType());
                    if (pullResponse.getCode() == RESPONSE_STATUS_OK) {
                        callback.onLoadMoreSuccess(pullResponse.getRequestType(), pullResponse.getData());
                    } else {
                        callback.onLoadMoreFail(pullResponse.getStatus());
                    }
                } else {
                    callback.onLoadMoreFail("Unknow Error");
                }
            }
        });
    }

    public static void sendTopicRefreshPullRequest(Request request, final IPuller.RefreshCallback<TopicSimpleData> callback) {
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onRefreshFail(ResourcesUtils.getString(R.string.common_tip_no_network));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    PullResponse pullResponse = gson.fromJson(data, new TypeToken<PullResponse<TopicSimpleData>>(){}.getType());
                    if (pullResponse.getCode() == RESPONSE_STATUS_OK) {
                        callback.onRefreshSuccess(pullResponse.getRequestType(), pullResponse.getData());
                    } else {
                        callback.onRefreshFail(pullResponse.getStatus());
                    }
                } else {
                    callback.onRefreshFail("Unknow Error");
                }
            }
        });
    }

    public static void sendTopicLoadMorePullRequest(Request request, final IPuller.LoadMoreCallback<TopicSimpleData> callback) {
        HttpsUtils.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.onLoadMoreFail(ResourcesUtils.getString(R.string.common_tip_no_network));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    PullResponse pullResponse = gson.fromJson(data, new TypeToken<PullResponse<TopicSimpleData>>(){}.getType());
                    if (pullResponse.getCode() == RESPONSE_STATUS_OK) {
                        callback.onLoadMoreSuccess(pullResponse.getRequestType(), pullResponse.getData());
                    } else {
                        callback.onLoadMoreFail(pullResponse.getStatus());
                    }
                } else {
                    callback.onLoadMoreFail("Unknow Error");
                }
            }
        });
    }

    public static Request buildRequestWithParams(String url, Map<String, String> params, Map<String, String> defaultParams) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for (String key : params.keySet()) {
            String value = params.get(key);
            httpBuilder.addQueryParameter(key, value != null ? value : defaultParams.get(key));
        }
        return new Request.Builder().url(httpBuilder.build()).build();
    }



}
