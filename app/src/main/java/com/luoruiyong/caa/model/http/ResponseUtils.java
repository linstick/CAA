package com.luoruiyong.caa.model.http;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.R;
import com.luoruiyong.caa.eventbus.CommonEvent;
import com.luoruiyong.caa.eventbus.DetailFinishEvent;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.eventbus.UserFinishEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Author: luoruiyong
 * Date: 2019/4/14/014
 * Description:
 **/
public class ResponseUtils {


    public static void handleCommonFailEvent(RequestType type, int code, String status) {
        CommonEvent event = new CommonEvent();
        event.setCode(code);
        event.setStatus(status);
        event.setType(type);
        EventBus.getDefault().post(event);
    }

    public static void handleCommonSuccessEvent(RequestType type, CommonEvent event) {
        event.setType(type);
        EventBus.getDefault().post(event);
    }

    public static void handlePullFailEvent(int targetPage, int pullType, int code, String status) {
        PullFinishEvent event = new PullFinishEvent();
        event.setCode(code);
        event.setStatus(status);
        event.setTargetPage(targetPage);
        event.setPullType(pullType);
        EventBus.getDefault().post(event);
    }

    public static void handlePullSuccessEvent(int targetPage, int pullType, PullFinishEvent event) {
        event.setTargetPage(targetPage);
        event.setPullType(pullType);
        EventBus.getDefault().post(event);
    }
}
