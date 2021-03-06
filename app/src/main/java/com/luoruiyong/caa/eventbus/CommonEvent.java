package com.luoruiyong.caa.eventbus;

import com.luoruiyong.caa.model.http.RequestType;

/**
 * Author: luoruiyong
 * Date: 2019/4/15/015
 * Description:
 **/
public class CommonEvent<T> {
    private RequestType type;
    private int code;
    private String status;
    private T data;

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
