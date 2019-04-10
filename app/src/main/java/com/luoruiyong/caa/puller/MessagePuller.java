package com.luoruiyong.caa.puller;

import com.luoruiyong.caa.bean.MessageData;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.http.HttpsUtils;
import com.luoruiyong.caa.utils.ListUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class MessagePuller implements
        IPuller<MessageData>,
        IPuller.RefreshCallback<MessageData>,
        IPuller.LoadMoreCallback<MessageData> {

    private List<MessageData> sMessageData;

    public List<MessageData> getData() {
        return sMessageData;
    }

    private synchronized void insertData(List<MessageData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        if (sMessageData == null) {
            sMessageData = new ArrayList<>();
        }
        sMessageData.addAll(0, list);
    }

    private synchronized void appendData(List<MessageData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        if (sMessageData == null) {
            sMessageData = new ArrayList<>();
        }
        sMessageData.addAll(list);
    }

    public void refresh(long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        doRefreshPull(params);
    }

    public void loadMore(long lastTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_LAST_ITEM_TIME, String.valueOf(lastTime));
        doLoadMorePull(params);
    }

    @Override
    public void doRefreshPull(Map<String, String> params) {
        if (params == null) {
            return;
        }
        Request request = HttpsUtils.buildRequestWithParams(REFRESH_MESSAGE_URL, params);
        HttpsUtils.sendMessageRefreshPullRequest(request, this);
    }

    @Override
    public void doLoadMorePull(Map<String, String> params) {
        if (params == null) {
            return;
        }
        Request request =  HttpsUtils.buildRequestWithParams(LOAD_MORE_MESSAGE_URL, params);
        HttpsUtils.sendMessageLoadMorePullRequest(request, this);
    }

    @Override
    public void onRefreshFail(String error) {
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_REFRESH_FAIL);
        event.setData(error);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onRefreshSuccess(int requestType, List<MessageData> result) {
        insertData(result);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_REFRESH_SUCCESS);
        event.setData(ListUtils.getSize(result));
        EventBus.getDefault().post(event);
    }

    @Override
    public void onLoadMoreFail(String error) {
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_LOAD_MORE_FAIL);
        event.setData(error);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onLoadMoreSuccess(int requestType, List<MessageData> result) {
        appendData(result);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_LOAD_MORE_SUCCESS);
        event.setData(ListUtils.getSize(result));
        EventBus.getDefault().post(event);
    }
}
