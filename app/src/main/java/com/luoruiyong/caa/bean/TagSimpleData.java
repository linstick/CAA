package com.luoruiyong.caa.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/17/017
 **/
public class TagSimpleData {
    private long uid;
    private int id;
    private String name;
    private String coverUrl;
    private int visitCount;
    private int joinCount;

    private List<String> discoverList;

    public TagSimpleData(int i) {
        uid = 1000 + i;
        id = 1000 + i;
        name = "#Topic" + (i + 1) + "#";
        coverUrl = "https:www.baidu.com/1.jpg";
        visitCount = (int)(Math.random() * 100);
        joinCount = (int)(Math.random() * 100);

        discoverList = new ArrayList<>();
        int count = (int) (Math.random() * 9);
        for (int j = 0; j < count; j++) {
            discoverList.add("Inner item " + (j + 1));
        }
    }

    public TagSimpleData() {
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
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

    public List<String> getDiscoverList() {
        return discoverList;
    }

    public void setDiscoverList(List<String> discoverList) {
        this.discoverList = discoverList;
    }
}
