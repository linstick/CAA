package com.luoruiyong.caa.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/17/017
 **/
public class TagSimpleData {
    int id;
    String name;
    String coverUrl;
    int visitCount;
    int joinCount;

    List<String> discoverList;

    public TagSimpleData(int i) {
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
