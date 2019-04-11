package com.luoruiyong.caa;

/**
 * Author: luoruiyong
 * Date: 2019/4/11/011
 * Description: 客户端和服务器端之间的数据定义
 **/
public class Config {

     // 定义可拉加载数据的页面ID
    public final static int PAGE_ID_ACTIVITY_ALL = 0;
    public final static int PAGE_ID_ACTIVITY_ONE_KIND = 7;
    public final static int PAGE_ID_ACTIVITY_SELF = PAGE_ID_ACTIVITY_ONE_KIND + 1;
    public final static int PAGE_ID_ACTIVITY_SELF_COLLECT = PAGE_ID_ACTIVITY_SELF + 1;
    public final static int PAGE_ID_ACTIVITY_OTHER_USER = PAGE_ID_ACTIVITY_SELF_COLLECT + 1;
    public final static int PAGE_ID_ACTIVITY_SEARCH = PAGE_ID_ACTIVITY_OTHER_USER + 1;
    public final static int PAGE_ID_TOPIC_ALL = PAGE_ID_ACTIVITY_SEARCH + 1;
    public final static int PAGE_ID_TOPIC_SELF = PAGE_ID_TOPIC_ALL + 1;
    public final static int PAGE_ID_TOPIC_OTHER_USER = PAGE_ID_TOPIC_SELF + 1;
    public final static int PAGE_ID_TOPIC_SEARCH = PAGE_ID_TOPIC_OTHER_USER + 1;
    public final static int PAGE_ID_DISCOVER_ALL = PAGE_ID_TOPIC_SEARCH + 1;
    public final static int PAGE_ID_DISCOVER_SELF = PAGE_ID_DISCOVER_ALL + 1;
    public final static int PAGE_ID_DISCOVER_OTHER_USER = PAGE_ID_DISCOVER_SELF + 1;
    public final static int PAGE_ID_DISCOVER_TOPIC_HOT = PAGE_ID_DISCOVER_OTHER_USER + 1;
    public final static int PAGE_ID_DISCOVER_TOPIC_LAST = PAGE_ID_DISCOVER_TOPIC_HOT + 1;
    public final static int PAGE_ID_DISCOVER_SEARCH = PAGE_ID_DISCOVER_TOPIC_LAST + 1;
    public final static int PAGE_ID_MESSAGE = PAGE_ID_DISCOVER_SEARCH + 1;
    public final static int PAGE_ID_ACTIVITY_COMMENT = PAGE_ID_MESSAGE + 1;
    public final static int PAGE_ID_ACTIVITY_ADDITION = PAGE_ID_ACTIVITY_COMMENT + 1;
    public final static int PAGE_ID_DISCOVER_COMMENT = PAGE_ID_ACTIVITY_ADDITION + 1;

    // 定义可拉加载页面的数据加载类型
    public final static int PULL_TYPE_REFRESH = 0;
    public final static int PULL_TYPE_LOAD_MORE = 1;

    // 定义数据请求ULR
    public final static String URL_ACTIVITY_FETCH = "http://112.74.13.186/Graduation/fetch_activities.php";
    public final static String URL_TOPIC_FETCH = "http://112.74.13.186/Graduation/fetch_topics.php";
    public final static String URL_DISCOVER_FETCH = "http://112.74.13.186/Graduation/fetch_discovers.php";
    public final static String URL_MESSAGE_FETCH = "http://112.74.13.186/Graduation/fetch_messages.php";
    public final static String URL_ACTIVITY_COMMENT_FETCH = "http://112.74.13.186/Graduation/fetch_activity_comments.php";
    public final static String URL_ACTIVITY_ADDITION_FETCH = "http://112.74.13.186/Graduation/fetch_activity_additions.php";
    public final static String URL_DISCOVER_COMMENT_FETCH = "http://112.74.13.186/Graduation/fetch_discover_comments.php";

    public final static String URL_ACTIVITY_DETAIL_FETCH = "http://112.74.13.186/Graduation/fetch_discover_comments.php";
    public final static String URL_TOPIC_DETAIL_FETCH = "http://112.74.13.186/Graduation/fetch_discover_comments.php";
    public final static String URL_USER_DETAIL_FETCH = "http://112.74.13.186/Graduation/fetch_discover_comments.php";
    public final static String URL_COMPOSITE_SEARCH_FETCH = "http://112.74.13.186/Graduation/fetch_discover_comments.php";
}
