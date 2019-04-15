package com.luoruiyong.caa.eventbus;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class PullFinishEvent<T> extends BaseResponseEvent {

    private int targetPage;
    private int pullType;
    private List<T> data;

    public PullFinishEvent() {
    }

    public int getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(int targetPage) {
        this.targetPage = targetPage;
    }

    public int getPullType() {
        return pullType;
    }

    public void setPullType(int pullType) {
        this.pullType = pullType;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
