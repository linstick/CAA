package com.luoruiyong.caa.eventbus;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class PullFinishEvent {

    public final static int TYPE_REFRESH_FAIL = 0;
    public final static int TYPE_REFRESH_SUCCESS = 1;
    public final static int TYPE_LOAD_MORE_FAIL = 2;
    public final static int TYPE_LOAD_MORE_SUCCESS = 3;

    private int type;
    private Object data;

    public PullFinishEvent() {
    }

    public PullFinishEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
