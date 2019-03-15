package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class UserBasicMap {

    private String label;
    private String value;

    public UserBasicMap() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserBasicMap{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
