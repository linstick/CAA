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
public class StringTypeAdapter extends TypeAdapter<String>{
    @Override
    public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
    }

    @Override
    public String read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            in.endArray();
            return null;
        }
        return in.nextString();
    }
}
