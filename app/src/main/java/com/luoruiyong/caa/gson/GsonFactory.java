package com.luoruiyong.caa.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.TypeAdapters;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description: 高德后台返回的Json数据，如果某个值为空，都是使用[]来代替，
 * 所以这里在使用Gson来解析时需要处理一下
 **/
public class GsonFactory {

    public static Gson buildGson() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(TypeAdapters.newFactory(String.class, new StringTypeAdapter()))
                .registerTypeAdapterFactory(TypeAdapters.newFactory(int.class, Integer.class, new IntegerTypeAdapter()))
                .registerTypeAdapterFactory(TypeAdapters.newFactory(double.class, Double.class, new DoubleTypeAdapter()))
                .registerTypeAdapterFactory(TypeAdapters.newFactory(long.class, Long.class, new LongTypeAdapter()))
                .create();
        return gson;
    }
}
