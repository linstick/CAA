package com.luoruiyong.caa.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/17/017
 **/
public class DiscoverData {
    private int uid;
    private String avatarUrl;
    private String nickname;
    private long publishTime;
    private String college;
    private String content;
    private String location;
    private String topic;
    private int topicId;
    private int likeCount;
    private int commentCount;
    private List<String> pictureList;


    public DiscoverData(int i) {
        // for test
        uid = 1000 + i;
        avatarUrl = "https://www.baidu.com/1.jpg";
        nickname = "nickname " + i;
        publishTime = System.currentTimeMillis();
        content = "This is the content of activity, long long long long long long long long long sentence!!! " + i;
        college = Math.random() > 0.5 ? "Guangdong University Of Technology " + i : null;
        location = Math.random() > 0.5 ? "Guangdong·Guangzhou " + i : null;
        topic = Math.random() > 0.5 ? "#Topic" + i + "#" : null;
        topicId = 10000 + i;
        likeCount = (int) (Math.random() * 100);
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

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
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

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
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
}
