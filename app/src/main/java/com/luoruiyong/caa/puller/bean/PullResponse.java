package com.luoruiyong.caa.puller.bean;


import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class PullResponse<T> {
    private int status;
    private String description;
    private int requestType;
    private List<T> data;

    public PullResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
