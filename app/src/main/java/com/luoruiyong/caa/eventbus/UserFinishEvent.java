package com.luoruiyong.caa.eventbus;

import com.luoruiyong.caa.bean.User;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description:
 **/
public class UserFinishEvent  extends BaseResponseEvent{

    public final static int TYPE_LOGIN = 0;

    public int type;

    public User data;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
