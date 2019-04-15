package com.luoruiyong.caa.eventbus;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description:
 **/
public class DetailFinishEvent<T> extends BaseResponseEvent{

    private T data;

    public DetailFinishEvent() {
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
