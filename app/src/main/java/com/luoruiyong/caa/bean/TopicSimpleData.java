package com.luoruiyong.caa.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/17/017
 **/
public class TopicSimpleData implements Serializable{
    private int uid;
    private int id;
    private String name;
    private String coverUrl;
    private int visitCount;
    private int joinCount;
    private long publishTime;

    private List<DiscoverData> discoverList;

    public TopicSimpleData(int i) {
        uid = 1000 + i;
        id = 1000 + i;
        publishTime = 100000;
        name = "#Topic" + (i + 1) + "#";
        coverUrl = "https:www.baidu.com/1.jpg";
        visitCount = (int)(Math.random() * 100);
        joinCount = (int)(Math.random() * 100);

        discoverList = new ArrayList<>();
        int count = (int) (Math.random() * 9);
        for (int j = 0; j < count; j++) {
            discoverList.add(new DiscoverData(i));
        }
    }

    public TopicSimpleData() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getVisitedCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getJoinCount() {
        return joinCount;
    }

    public void setJoinCount(int joinCount) {
        this.joinCount = joinCount;
    }

    public List<DiscoverData> getDiscoverList() {
        return discoverList;
    }

    public void setDiscoverList(List<DiscoverData> discoverList) {
        this.discoverList = discoverList;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }
}
