package com.luoruiyong.caa.model.http;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: luoruiyong
 * Date: 2019/4/18/018
 * Description: 上传数据中带有文件的时候使用
 **/
public class FileUploader {

    private static final String TAG = "FileUploader";

    private final static int CONNECT_TIMEOUT = 10;
    private final static int WRITE_TIMEOUT = 8;
    private final static int READ_TIMEOUT = 8;

    private static OkHttpClient sClient;

    public static OkHttpClient getClient() {
        if (sClient == null) {
            synchronized (OkHttpClient.class) {
                if (sClient == null) {
                    sClient = new OkHttpClient.Builder()
                            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                            .build();
                }
            }
        }
        return sClient;
    }




    public static void doUpload(RequestType requestType, Request request, Type reflectType) {
        getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
                ResponseUtils.handleCommonFailEvent(requestType, Config.CODE_REQUEST_ERROR, ResourcesUtils.getString(R.string.common_tip_request_error));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String data = response.body().string();
                    CommonEvent event = new Gson().fromJson(data, reflectType);
                    ResponseUtils.handleCommonSuccessEvent(requestType, event);
                    Log.d(TAG, "onResponse: " + event);
                } else {
                    ResponseUtils.handleCommonFailEvent(requestType, Config.CODE_REQUEST_ERROR, ResourcesUtils.getString(R.string.common_tip_request_error));
                    Log.d(TAG, "onResponse: fail");
                }
            }
        });
    }

    public static class Builder {
        String url;
        List<String> paths;
        MediaType fileType;
        String name;
        Map<String, String> params;

        public Builder() {
        }

        public Builder filePaths(List<String> paths) {
            this.paths = paths;
            return this;
        }

        public Builder filePath(String path) {
            this.paths = new ArrayList<>();
            this.paths.add(path);
            return this;
        }

        public Builder fileType(MediaType fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder params(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Request build() {
            MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (String path : paths){
                File file = new File(path);
                if (file.exists()){
                    body.addFormDataPart(name, file.getName(), RequestBody.create(fileType, file));
                }
            }

            for (String key : params.keySet()) {
                body.addFormDataPart(key, params.get(key));
            }

            RequestBody requestBody = body.build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            return request;
        }
    }
}
