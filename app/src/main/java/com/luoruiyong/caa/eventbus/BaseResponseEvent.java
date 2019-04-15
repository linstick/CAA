package com.luoruiyong.caa.eventbus;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description:
 **/
public class BaseResponseEvent {
    private int code;
    private String status;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
