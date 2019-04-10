package com.luoruiyong.caa.puller.bean;


import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class PullResponse<T> {
    private int code;
    private String status;
    private int requestType;
    private List<T> data;

    public PullResponse() {
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

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T>  data) {
        this.data = data;
    }
}
