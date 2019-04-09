package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class TopicData {
    private int id;
    private int uid;
    private String avatarUrl;
    private String nickname;
    private String coverUrl;
    private String name;
    private String introduction;
    private int joinedCount;
    private int visitedCount;

    public TopicData() {
        id = 1001;
        uid = 1000;
        avatarUrl = "https://www.baidu.com";
        nickname = "会飞的猪";
        name = "都挺好";
        introduction = "这是话题导语部分，它可能是很长很长的一段文字";
        joinedCount = (int) (Math.random() * 100);
        visitedCount = (int) (Math.random() * 100);
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

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getJoinedCount() {
        return joinedCount;
    }

    public void setJoinedCount(int joinedCount) {
        this.joinedCount = joinedCount;
    }

    public int getVisitedCount() {
        return visitedCount;
    }

    public void setVisitedCount(int visitedCount) {
        this.visitedCount = visitedCount;
    }
}
