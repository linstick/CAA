package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/3/18/018
 **/
public class Function {
    public final static int TYPE_WITH_NONE = 1;
    public final static int TYPE_ONLY_WITH_RIGHT_SIGN = 2;
    public final static int TYPE_ONLY_WITH_RIGHT_INFO = 3;
    public final static int TYPE_ONLY_WITH_LITTLE_RED_POINT = 4;
    public final static int TYPE_WITH_RED_POINT_AND_RIGHT_SIGN = 5;
    public final static int TYPE_WITH_LITTLE_RED_POINT_AND_RIGHT_SIGN = 6;

    private int type;
    private String name;

    public Function(String name) {
        this.type = TYPE_WITH_NONE;
        this.name = name;
    }

    public Function(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
