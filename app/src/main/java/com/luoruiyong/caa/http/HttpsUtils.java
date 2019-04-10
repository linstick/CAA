package com.luoruiyong.caa.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.puller.IPuller;
import com.luoruiyong.caa.puller.bean.PullResponse;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
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
                    callback.onRefreshFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    PullResponse pullResponse = gson.fromJson(data, new TypeToken<PullResponse<ActivitySimpleData>>(){}.getType());
                    if (pullResponse.getStatus() == RESPONSE_STATUS_OK) {
                        callback.onRefreshSuccess(pullResponse.getRequestType(), pullResponse.getData());
                    } else {
                        callback.onRefreshFail(pullResponse.getDescription());
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
                    callback.onLoadMoreFail(e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    PullResponse pullResponse = gson.fromJson(data, new TypeToken<PullResponse<ActivitySimpleData>>(){}.getType());
                    if (pullResponse.getStatus() == RESPONSE_STATUS_OK) {
                        callback.onLoadMoreSuccess(pullResponse.getRequestType(), pullResponse.getData());
                    } else {
                        callback.onLoadMoreFail(pullResponse.getDescription());
                    }
                } else {
                    callback.onLoadMoreFail("Unknow Error");
                }
            }
        });
    }



}
