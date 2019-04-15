package com.luoruiyong.caa.bean;

import com.luoruiyong.caa.Config;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class MessageData {

    private int uid;
    private String avatarUrl;
    private String nickname;
    private String publishTime;

    private int type;
    private String content;

    private int targetId;
    private String targetCoverUrl;
    private String targetTitle;
    private String targetContent;

    public MessageData(int i) {
        uid = 1000;
        avatarUrl = "https://www.baidu.com/1.jpg";
        nickname = "linstick" + i;
        publishTime = Config.DEFAULT_TIME_STAMP;

        type = (int) (Math.random() * 7);
        content = "This is the content of message " + i;

        targetId = 1000 + i;
        targetCoverUrl = "https://www.baidu.com/2.jpg";
        targetTitle = "This is the title of src note " + i;
        targetContent = "This is the content of src note, long long long long sentence " + i;
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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetCoverUrl() {
        return targetCoverUrl;
    }

    public void setTargetCoverUrl(String targetCoverUrl) {
        this.targetCoverUrl = targetCoverUrl;
    }

    public String getTargetTitle() {
        return targetTitle;
    }

    public void setTargetTitle(String targetTitle) {
        this.targetTitle = targetTitle;
    }

    public String getTargetContent() {
        return targetContent;
    }

    public void setTargetContent(String targetContent) {
        this.targetContent = targetContent;
    }
}
