package com.luoruiyong.caa.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Author: luoruiyong
 * Date: 2019/4/8/008
 * Description: 高德API调用返回的数据集解析兼容
 **/
public class LongTypeAdapter extends TypeAdapter<Long>{
    @Override
    public void write(JsonWriter out, Long value) throws IOException {
        out.value(value);
    }

    @Override
    public Long read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return -1L;
        }
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            in.endArray();
            return -1L;
        }
        return in.nextLong();
    }
}
