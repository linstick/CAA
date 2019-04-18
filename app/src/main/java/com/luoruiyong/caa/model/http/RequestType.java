package com.luoruiyong.caa.model.http;

/**
 * Author: luoruiyong
 * Date: 2019/4/15/015
 * Description: 简单普通的请求类型定义(与后台无关)。
 * 注意：列表请求类型定义在{@link com.luoruiyong.caa.Config}中
 **/
public enum  RequestType {
    // 不需要客户端接收结果的请求
    NONE,
    // 用户相关请求
    LOGIN,
    SIGN_UP,
    FETCH_AVATAR,
    CHECK_ACCOUNT,
    MODIFY_PASSWORD,
    FETCH_USER_DETAIL,

    // 话题相关请求
    FETCH_TOPIC_DETAIL,
    FETCH_HOT_SIMPLE_TOPIC_LIST,
    FETCH_SIMPLE_TOPIC_LIST,
    CHECK_TOPIC_NAME,

    // 搜索请求相关
    FETCH_COMPOSITE_SEARCH_LIST,
    FETCH_COMPOSITE_SEARCH_HOT_TIP,
    FETCH_COMPOSITE_SEARCH_SIMPLE_TIP,

    // 活动相关
    FETCH_ACTIVITY_DETAIL,

    // 动态相关
    FETCH_DISCOVER_DETAIL,
}
