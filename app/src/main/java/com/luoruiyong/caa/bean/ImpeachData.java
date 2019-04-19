package com.luoruiyong.caa.bean;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/19/019
 * Description:
 **/
public class ImpeachData {

    private int targetType;
    private int targetId;
    private int reasonType;
    private String description;
    private List<String> pictureList;

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getReasonType() {
        return reasonType;
    }

    public void setReasonType(int reasonType) {
        this.reasonType = reasonType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }
}
