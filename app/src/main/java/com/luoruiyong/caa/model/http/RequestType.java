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
    LOGIN,  // post
    SIGN_UP,// post
    FETCH_AVATAR,  // get
    CHECK_ACCOUNT,  // get
    MODIFY_PASSWORD,    // pos
    FETCH_USER_DETAIL,  // get
    MODIFY_PROFILE, // post

    // 话题相关请求
    FETCH_TOPIC_DETAIL, // get
    FETCH_TOPIC_DYNAMIC_DATA,  // get
    FETCH_HOT_SIMPLE_TOPIC_LIST,    // get
    FETCH_SIMPLE_TOPIC_LIST,    // get
    CHECK_TOPIC_NAME,   // get
    CREATE_TOPIC,   // post
    DELETE_TOPIC,   // post

    // 搜索请求相关
    FETCH_COMPOSITE_SEARCH_LIST,    // get
    FETCH_COMPOSITE_SEARCH_HOT_TIP, // get
    FETCH_COMPOSITE_SEARCH_SIMPLE_TIP,  // get

    // 活动相关
    FETCH_ACTIVITY_DETAIL,  // get
    FETCH_ACTIVITY_DYNAMIC_DATA,  // get
    CREATE_ACTIVITY,    // post
    DELETE_ACTIVITY,    // post
    COLLECT_ACTIVITY,   // get
    ADD_ACTIVITY_COMMENT,   // post
    DELETE_ACTIVITY_COMMENT,   // get
    ADD_ACTIVITY_ADDITION,   // post
    DELETE_ACTIVITY_ADDITION,   // get

    // 动态相关
    FETCH_DISCOVER_DETAIL,  // get
    FETCH_DISCOVER_DYNAMIC_DATA,  // get
    CREATE_DISCOVER,    // post
    DELETE_DISCOVER,    // get
    LIKE_DISCOVER, // get
    ADD_DISCOVER_COMMENT,   // post
    DELETE_DISCOVER_COMMENT,   // post

    // 改进相关
    FEEDBACK,   // post
    IMPEACH,    // post

    // 消息相关
    DELETE_MESSAGE,
}
