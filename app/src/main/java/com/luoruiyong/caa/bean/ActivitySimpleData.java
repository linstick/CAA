package com.luoruiyong.caa.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/15/015
 **/
public class ActivitySimpleData {

    private int uid;
    private String avatarUrl;
    private String nickname;
    private long publishTime;
    private int type;
    private String title;
    private String content;
    private String location;
    private String topic;
    private int topicId;
    private int collectCount;
    private int commentCount;
    private List<String> pictureList;

    public ActivitySimpleData() {
    }

    // for text
    public ActivitySimpleData(int i, int type) {
        // for test
        uid = 1000 + i;
        avatarUrl = "https://www.baidu.com/1.jpg";
        nickname = "昵称" + i;
        publishTime = System.currentTimeMillis();
        this.type = type;
        title = "This is the title of activity " + i;
        content = "This is the content of activity, long long long long long long long long long sentence!!! " + i;
        location = Math.random() > 0.5 ? "广东·广州" + i : null;
        topic = Math.random() > 0.5 ? "#话题" + i + "#" : null;
        topicId = 10000 + i;
        collectCount = (int) (Math.random() * 100);
        commentCount = (int) (Math.random() * 100);
        pictureList = new ArrayList<>();
        int pictureCount = (int) (Math.random() * 9);
        for (int j = 0; j < pictureCount; j++) {
            pictureList.add("https://www.baidu.com/" + (j + 1) + ".jpg");
        }
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    @Override
    public String toString() {
        return "ActivitySimpleData{" +
                "uid='" + uid + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", nickname='" + nickname + '\'' +
                ", publishTime=" + publishTime +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", location='" + location + '\'' +
                ", topic='" + topic + '\'' +
                ", topicId='" + topicId + '\'' +
                ", collectCount=" + collectCount +
                ", commentCount=" + commentCount +
                ", pictureList=" + pictureList +
                '}';
    }
}
