package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/4/3/003
 **/
public class MessageData {

    private int uid;
    private String avatarUrl;
    private String nickname;
    private long publishTime;

    private int type;
    private String content;

    private int srcId;
    private String srcCoverUrl;
    private String srcTitle;
    private String srcContent;

    public MessageData(int i) {
        uid = 1000;
        avatarUrl = "https://www.baidu.com/1.jpg";
        nickname = "linstick" + i;
        publishTime = System.currentTimeMillis();

        type = (int) (Math.random() * 7);
        content = "This is the content of message " + i;

        srcId = 1000 + i;
        srcCoverUrl = "https://www.baidu.com/2.jpg";
        srcTitle = "This is the title of src note " + i;
        srcContent = "This is the content of src note, long long long long sentence " + i;
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

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public String getSrcCoverUrl() {
        return srcCoverUrl;
    }

    public void setSrcCoverUrl(String srcCoverUrl) {
        this.srcCoverUrl = srcCoverUrl;
    }

    public String getSrcTitle() {
        return srcTitle;
    }

    public void setSrcTitle(String srcTitle) {
        this.srcTitle = srcTitle;
    }

    public String getSrcContent() {
        return srcContent;
    }

    public void setSrcContent(String srcContent) {
        this.srcContent = srcContent;
    }
}
