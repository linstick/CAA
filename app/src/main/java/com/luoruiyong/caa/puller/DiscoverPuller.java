package com.luoruiyong.caa.puller;

import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.bean.DiscoverData;
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
public class DiscoverPuller implements IPuller,
        IPuller.RefreshCallback<DiscoverData>,
        IPuller.LoadMoreCallback<DiscoverData> {

    public static final int TYPE_ALL = 0;
    public static final int TYPE_SELF = TYPE_ALL + 1;
    public static final int TYPE_OTHER_USER = TYPE_SELF + 1;
    public static final int TYPE_TOPIC_HOT = TYPE_OTHER_USER + 1;
    public static final int TYPE_TOPIC_LASTED = TYPE_TOPIC_HOT + 1;
    public static final int TYPE_SEARCH = TYPE_TOPIC_LASTED + 1;

    public static final String DEFAULT_REQUEST_TYPE = String.valueOf(TYPE_ALL);

    private Map<Integer, List<DiscoverData>> mDiscover;
    private Map<String, String> mDefaultParams;

    public synchronized List<DiscoverData> getData(int type) {
        initIfNeed();
        return mDiscover.get(type);
    }

    private synchronized void insertData(int type, List<DiscoverData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        initIfNeed();
        List<DiscoverData> dataList = mDiscover.get(type);
        if (dataList == null) {
            dataList = new ArrayList<>();
            mDiscover.put(type, dataList);
        }
        dataList.addAll(0, list);
    }

    private synchronized void appendData(int type, List<DiscoverData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        initIfNeed();
        List<DiscoverData> dataList = mDiscover.get(type);
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.addAll(list);
    }

    public void initIfNeed() {
        if (mDiscover == null) {
            synchronized (ActivityPuller.class) {
                if(mDiscover == null) {
                    mDiscover = new HashMap<>();

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

    public void refreshTopicHot(int topicId, long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_TOPIC_HOT));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        params.put(PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doRefreshPull(params);
    }

    public void loadMoreTopicHot(int topicId, long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_TOPIC_HOT));
        params.put(PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(lastItemTime));
        doLoadMorePull(params);
    }

    public void refreshTopicLasted(int topicId, long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_TOPIC_LASTED));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        params.put(PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        doRefreshPull(params);
    }

    public void loadMoreTopicLasted(int topicId, long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_TOPIC_LASTED));
        params.put(PARAM_KEY_TOPIC_ID, String.valueOf(topicId));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(lastItemTime));
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
        Request request = HttpsUtils.buildRequestWithParams(REFRESH_DISCOVER_URL, params, mDefaultParams);
        HttpsUtils.sendDiscoverRefreshPullRequest(request, this);
    }

    @Override
    public void doLoadMorePull(Map params) {
        if (params == null) {
            return;
        }
        Request request =  HttpsUtils.buildRequestWithParams(LOAD_MORE_DISCOVER_URL, params, mDefaultParams);
        HttpsUtils.sendDiscoverLoadMorePullRequest(request, this);
    }

    @Override
    public void onRefreshFail(String error) {
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_REFRESH_FAIL);
        event.setData(error);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onRefreshSuccess(int requestType, List<DiscoverData> result) {
        insertData(requestType, result);
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
    public void onLoadMoreSuccess(int requestType, List<DiscoverData> result) {
        appendData(requestType, result);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_LOAD_MORE_SUCCESS);
        event.setData(ListUtils.getSize(result));
        EventBus.getDefault().post(event);
    }
}
