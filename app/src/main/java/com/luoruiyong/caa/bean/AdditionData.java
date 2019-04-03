package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class AdditionData {
    private int id;
    private String content;
    private long publishTime;

    public AdditionData(int i) {
        id = 1000 + i;
        content = "This is the addition of activity " + i;
        publishTime = System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
