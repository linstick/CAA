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

    public static void handleUserFinishEvent(int code) {
        handleCommonFetchFailEvent(code, null);
    }

    public static void handleUserFinishEvent(int type, int code, String status) {
        UserFinishEvent event = new UserFinishEvent();
        event.setCode(code);
        event.setStatus(status);
        event.setType(type);
        EventBus.getDefault().post(event);
    }

    public static void handleCommonFetchFailEvent(int code) {
       handleCommonFetchFailEvent(code, null);
    }

    public static void handleCommonFetchFailEvent(int code, String status) {
        DetailFinishEvent event = new DetailFinishEvent();
        event.setCode(code);
        event.setStatus(status);
        EventBus.getDefault().post(event);
    }

    public static void handlePullSuccessEvent(PullFinishEvent event, int targetPage, int pullType) {
        if (event != null) {
            event.setTargetPage(targetPage);
            event.setPullType(pullType);
            EventBus.getDefault().post(event);
        } else {
            handlePullFailEvent(Config.CODE_ERROR, targetPage, pullType);
        }
    }

    public static void handlePullFailEvent(int code, int targetPage, int pullType) {
        handlePullFailEvent(code, targetPage, pullType, null);
    }

    public static void handlePullFailEvent(int code, int targetPage, int pullType, String status) {
        PullFinishEvent event = new PullFinishEvent();
        event.setCode(code);
        event.setStatus(status);
        event.setTargetPage(targetPage);
        event.setPullType(pullType);
        EventBus.getDefault().post(event);
    }
}
