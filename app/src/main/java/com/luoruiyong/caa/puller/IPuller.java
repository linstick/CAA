package com.luoruiyong.caa.puller;

import com.luoruiyong.caa.Enviroment;

import java.util.List;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public interface IPuller<T> {
    String REFRESH_ACTIVITY_URL = "http://112.74.13.186/test.php";
    String LOAD_MORE_ACTIVITY_URL = "http://112.74.13.186/test.php";

    String PARAM_KEY_REQUEST_TYPE = "request_type";
    String PARAM_KEY_REQUEST_COUNT= "request_count";
    String PARAM_KEY_TYPE = "type";
    String PARAM_KEY_UID = "uid";
    String PARAM_KEY_OTHER_UID = "other_uid";
    String PARAM_KEY_KEYWORD = "keyword";
    String PARAM_KEY_LAST_ITEM_TIME = "last_time";
    String PARAM_KEY_FIRST_ITEM_TIME = "first_time";

    String DEFAULT_UID = String.valueOf(Enviroment.getCurUid());
    String DEFAULT_OTHER_UID = String.valueOf(-1);
    String DEFAULT_KEYWORD = "";
    String DEFAULT_FIRST_ITEM_TIME = String.valueOf(0);
    String DEFAULT_LAST_ITEM_TIME = String.valueOf(0);
    String DEFAULT_REQUEST_COUNT = String.valueOf(30);

    void doRefreshPull(Map<String, String> params);
    void doLoadMorePull(Map<String, String> params);

    interface RefreshCallback<T> {
        void onRefreshFail(String error);
        void onRefreshSuccess(int requestType, List<T> result);
    }

    interface LoadMoreCallback<T> {
        void onLoadMoreFail(String error);
        void onLoadMoreSuccess(int requestType, List<T> result);
    }
}
