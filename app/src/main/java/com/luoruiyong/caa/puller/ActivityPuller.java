package com.luoruiyong.caa.puller;

import com.luoruiyong.caa.Enviroment;
import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.eventbus.PullFinishEvent;
import com.luoruiyong.caa.http.HttpsUtils;
import com.luoruiyong.caa.utils.ListUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class ActivityPuller implements
        IPuller<ActivitySimpleData>,
        IPuller.RefreshCallback<ActivitySimpleData>,
        IPuller.LoadMoreCallback<ActivitySimpleData> {
    private static final String TAG = "ActivityPuller";

    public static final int TYPE_ALL = 0;
    public static final int TYPE_ONE_KIND = Enviroment.getActivityTypeMap().size();
    public static final int TYPE_SELF = TYPE_ONE_KIND + 1;
    public static final int TYPE_OTHER_USER = TYPE_SELF + 1;
    public static final int TYPE_SELF_COLLECT = TYPE_OTHER_USER + 1;
    public static final int TYPE_SEARCH = TYPE_SELF_COLLECT + 1;

    public static final String DEFAULT_REQUEST_TYPE = String.valueOf(TYPE_ALL);
    public static final String DEFAULT_TYPE = String.valueOf(0);

    private Map<Integer, List<ActivitySimpleData>> mActivityDatas;
    private Map<String, String> mDefaultParams;

    public synchronized List<ActivitySimpleData> getData(int type) {
        initIfNeed();
        return mActivityDatas.get(type);
    }

    private synchronized void insertData(int type, List<ActivitySimpleData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        initIfNeed();
        List<ActivitySimpleData> dataList = mActivityDatas.get(type);
        if (dataList == null) {
            dataList = new ArrayList<>();
            mActivityDatas.put(type, dataList);
        }
        dataList.addAll(0, list);
    }

    private synchronized void appendData(int type, List<ActivitySimpleData> list) {
        if (ListUtils.isEmpty(list)) {
            return;
        }
        initIfNeed();
        List<ActivitySimpleData> dataList = mActivityDatas.get(type);
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        dataList.addAll(list);
    }

    public void initIfNeed() {
        if (mActivityDatas == null) {
            synchronized (ActivityPuller.class) {
                if(mActivityDatas == null) {
                    mActivityDatas = new HashMap<>();

                    // 对请求参数默认值进行初始化
                    mDefaultParams = new HashMap<>();
                    mDefaultParams.put(PARAM_KEY_REQUEST_TYPE, DEFAULT_REQUEST_TYPE);
                    mDefaultParams.put(PARAM_KEY_REQUEST_COUNT, DEFAULT_REQUEST_COUNT);
                    mDefaultParams.put(PARAM_KEY_TYPE, DEFAULT_TYPE);
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

    public void refreshOneKind(int activityType, long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_ONE_KIND));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        params.put(PARAM_KEY_TYPE, String.valueOf(activityType));
        doRefreshPull(params);
    }

    public void loadMoreOneKind(int activityType, long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_ONE_KIND));
        params.put(PARAM_KEY_TYPE, String.valueOf(activityType));
        params.put(PARAM_KEY_LAST_ITEM_TIME, String.valueOf(lastItemTime));
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

    public void refreshSelfCollect(long firstItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_SELF_COLLECT));
        params.put(PARAM_KEY_FIRST_ITEM_TIME, String.valueOf(firstItemTime));
        doRefreshPull(params);
    }

    public void loadMoreSelfCollect(long lastItemTime) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_KEY_REQUEST_TYPE, String.valueOf(TYPE_SELF_COLLECT));
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
    public void doRefreshPull(Map<String, String> params) {
        if (params == null) {
            return;
        }
        Request request = HttpsUtils.buildRequestWithParams(REFRESH_ACTIVITY_URL, params, mDefaultParams);
        HttpsUtils.sendActivityRefreshPullRequest(request, this);
    }

    @Override
    public void doLoadMorePull(Map<String, String> params) {
        if (params == null) {
            return;
        }
        Request request =  HttpsUtils.buildRequestWithParams(LOAD_MORE_ACTIVITY_URL, params, mDefaultParams);
        HttpsUtils.sendActivityLoadMorePullRequest(request, this);
    }

    @Override
    public void onRefreshFail(String error) {
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_REFRESH_FAIL);
        event.setData(error);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onRefreshSuccess(int requestType, List<ActivitySimpleData> result) {
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
    public void onLoadMoreSuccess(int requestType, List<ActivitySimpleData> result) {
        appendData(requestType, result);
        PullFinishEvent event = new PullFinishEvent();
        event.setType(PullFinishEvent.TYPE_LOAD_MORE_SUCCESS);
        event.setData(ListUtils.getSize(result));
        EventBus.getDefault().post(event);
    }
}
