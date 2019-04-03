package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class CommentData {
    private int id;
    private int uid;
    private String avatarUrl;
    private String nickname;
    private long publishTime;
    private String content;

    public CommentData(int i) {
        id = 1000 + i;
        uid = 1000 + i;
        avatarUrl = "https://www.baidu.com/1.jpg";
        nickname = "昵称" + i;
        publishTime = System.currentTimeMillis();
        content = "This is the content of comment " + i;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
