package com.luoruiyong.caa.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/19/019
 * Description:
 **/
public class Feedback implements Serializable{

    int type;
    String description;
    List<String> pictureList;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
