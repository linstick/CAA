package com.luoruiyong.caa.model.gson;

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
public class DoubleTypeAdapter extends TypeAdapter<Double>{
    @Override
    public void write(JsonWriter out, Double value) throws IOException {
        out.value(value);
    }

    @Override
    public Double read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return -1D;
        }
        if (in.peek() == JsonToken.BEGIN_ARRAY) {
            in.beginArray();
            in.endArray();
            return -1D;
        }
        return in.nextDouble();
    }
}
