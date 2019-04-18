package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/3/13/013
 **/
public class UserBasicMap {

    private String label;
    private Object value;

    public UserBasicMap() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
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
