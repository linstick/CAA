package com.luoruiyong.caa.puller;

import android.util.Log;

import com.luoruiyong.caa.bean.TopicSimpleData;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.http.HttpsUtils;
import com.luoruiyong.caa.puller.IPuller.LoadMoreCallback;
import com.luoruiyong.caa.puller.IPuller.RefreshCallback;
import com.luoruiyong.caa.utils.ListUtils;
import com.luoruiyong.caa.utils.LogUtils;

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
public class TopicPuller implements
        IPuller,
        RefreshCallback<TopicSimpleData>,
        LoadMoreCallback<TopicSimpleData> {

    private static final String TAG = "TopicPuller";

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SELF = TYPE_ALL + 1;
    public static final int TYPE_OTHER_USER = TYPE_SELF + 1;
    public static final int TYPE_SEARCH = TYPE_OTHER_USER + 1;

    public static final String DEFAULT_REQUEST_TYPE = String.valueOf(TYPE_ALL);

    private Map<Integer, List<TopicSimpleData>> mTopicDatas;
    private Map<String, String> mDefaultParams;

    public synchronized List<TopicSimpleData> getData(int type) {
        initIfNeed();
        return mTopicDatas.get(type);
    }

    private synchronized void insertData(int type, List<TopicSimpleData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        initIfNeed();
        List<TopicSimpleData> dataList = mTopicDatas.get(type);
        if (dataList == null) {
            dataList = new ArrayList<>();
            mTopicDatas.put(type, dataList);
        }
        dataList.addAll(0, list);
    }

    private synchronized void appendData(int type, List<TopicSimpleData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        initIfNeed();
        List<TopicSimpleData> dataList = mTopicDatas.get(type);
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.addAll(list);
    }

    public void initIfNeed() {
        if (mTopicDatas == null) {
            synchronized (ActivityPuller.class) {
                if(mTopicDatas == null) {
                    mTopicDatas = new HashMap<>();
                    // 对请求参数默认值进行初始化
                    mDefaultParams = new HashMap<>();
                    mDefaultParams.put(PARAM_KEY_REQUEST_TYPE, DEFAULT_REQUEST_TYPE);
                    mDefaultParams.put(PARAM_KEY_REQUEST_COUNT, DEFAULT_REQUEST_COUNT);
                    mDefaultParams.put(PARAM_KEY_UID, DEFAULT_UID);
                    mDefaultParams.put(PARAM_KEY_OTHER_UID, DEFAULT_OTHER_UID);
                    mDefaultParams.put(PARAM_KEY_KEYWORD, DEFAULT_KEYWORD);
                    mDefaultParams.put(PARAM_KEY_LAST_ITEM_TIME, DEFAULT_LAST_ITEM_TIME);
                    mDefaultParams.put(PARAM_KEY_FIRST_ITEM_TIME, DEFAULT_FIRST_ITEM_TIME);
                }
            }
        }
    }

    public void refreshAll(long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_ALL));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        doRefreshPull(params);
    }

    public void loadMoreAll(long lastTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_ALL));
        params.put(PARAM_KEY_LAST_ITEM_TIME, String.valueOf(lastTime));
        doLoadMorePull(params);
    }

    public void refreshSelf(long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_SELF));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        doRefreshPull(params);
    }

    public void loadMoreSelf(long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_SELF));
        params.put(PARAM_KEY_LAST_ITEM_TIME, String.valueOf(lastItemTime));
        doLoadMorePull(params);
    }

    public void refreshOtherUser(int uid, long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_OTHER_USER));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        params.put(PARAM_KEY_OTHER_UID, String.valueOf(uid));
        doRefreshPull(params);
    }

    public void loadMoreOtherUser(int uid, long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_OTHER_USER));
        params.put(PARAM_KEY_OTHER_UID, String.valueOf(uid));
        params.put(PARAM_KEY_LAST_ITEM_TIME, String.valueOf(lastItemTime));
        doLoadMorePull(params);
    }

    public void refreshSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_SEARCH));
        params.put(PARAM_KEY_KEYWORD, keyword);
        doRefreshPull(params);
    }

    public void loadMoreSearch(String keyword, long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_SEARCH));
        params.put(PARAM_KEY_KEYWORD, keyword);
        params.put(PARAM_KEY_LAST_ITEM_TIME, String.valueOf(lastItemTime));
        doLoadMorePull(params);
    }

    @Override
    public void doRefreshPull(Map params) {
        if (params == null) {
            return;
        }
        Request request = HttpsUtils.buildRequestWithParams(REFRESH_TOPIC_URL, params, mDefaultParams);
        HttpsUtils.sendTopicRefreshPullRequest(request, this);
    }

    @Override
    public void doLoadMorePull(Map params) {
        if (params == null) {
            return;
        }
        Request request =  HttpsUtils.buildRequestWithParams(LOAD_MORE_TOPIC_URL, params, mDefaultParams);
        HttpsUtils.sendTopicLoadMorePullRequest(request, this);
    }

    @Override
    public void onRefreshFail(String error) {
        LogUtils.d(TAG, "onRefreshFail: " + error);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_REFRESH_FAIL);
        event.setData(error);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onRefreshSuccess(int requestType, List<TopicSimpleData> result) {
        LogUtils.d(TAG, "onRefreshSuccess: requestType = " + requestType + "  result = " + result);
        insertData(requestType, result);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_REFRESH_SUCCESS);
        event.setData(ListUtils.getSize(result));
        EventBus.getDefault().post(event);
    }

    @Override
    public void onLoadMoreFail(String error) {
        LogUtils.d(TAG, "onLoadMoreFail: " + error);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_LOAD_MORE_FAIL);
        event.setData(error);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onLoadMoreSuccess(int requestType, List<TopicSimpleData> result) {
        LogUtils.d(TAG, "onLoadMoreSuccess: requestType = " + requestType + "  result = " + result);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                appendData(requestType, result);
                PullFinishEvent event = new PullFinishEvent();
                event.setType(PullFinishEvent.TYPE_LOAD_MORE_SUCCESS);
                event.setData(ListUtils.getSize(result));
                EventBus.getDefault().post(event);
            }
        }).start();
    }
}
