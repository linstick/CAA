package com.luoruiyong.caa.bean;

import com.luoruiyong.caa.Config;

import java.util.Date;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class AdditionData {
    private int id;
    private String content;
    private String publishTime;

    public AdditionData() {
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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
