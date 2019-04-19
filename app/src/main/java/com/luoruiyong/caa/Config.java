package com.luoruiyong.caa;

/**
 * Author: luoruiyong
 * Date: 2019/4/11/011
 * Description: 客户端和服务器端之间的数据定义
 **/
public class Config {

    /**
     * 状态码
     */
    public final static int CODE_OK = 0;
    public final static int CODE_OK_BUT_EMPTY = 1;
    public final static int CODE_ERROR = -1;
    public final static int CODE_NO_DATA = -2;
    public final static int CODE_NETWORK_ERROR = -3;
    public final static int CODE_REQUEST_ERROR = -4;
    public final static int CODE_SERVER_ERROR = -5;


    /**
     *  // 响应状态码
     const CODE_OK = 0;
     const CODE_OK_BUT_EMPTY = 1;
     const CODE_ILLEGAL_ACCESS = -1;
     const CODE_SERVER_ERROR = -3;
     */

    /**
     *  定义可拉加载数据的页面ID
     */
    // 需要全局缓存的页面数据
    public final static int PAGE_ID_NONE = -1;
    public final static int PAGE_ID_ACTIVITY_ALL = 0;
    public final static int PAGE_ID_ACTIVITY_ONE_KIND = 8;
    public final static int PAGE_ID_ACTIVITY_SELF = PAGE_ID_ACTIVITY_ONE_KIND + 1;
    public final static int PAGE_ID_ACTIVITY_SELF_COLLECT = PAGE_ID_ACTIVITY_SELF + 1;
    public final static int PAGE_ID_TOPIC_ALL = PAGE_ID_ACTIVITY_SELF_COLLECT + 1;
    public final static int PAGE_ID_TOPIC_SELF = PAGE_ID_TOPIC_ALL + 1;
    public final static int PAGE_ID_DISCOVER_ALL = PAGE_ID_TOPIC_SELF + 1;
    public final static int PAGE_ID_DISCOVER_SELF = PAGE_ID_DISCOVER_ALL + 1;
    public final static int PAGE_ID_MESSAGE = PAGE_ID_DISCOVER_SELF + 1;
    // 全局缓存数据的最大PAGE ID,分界线
    public final static int MAX_GLOBAL_CACHE_ID = PAGE_ID_MESSAGE;
    // 不需要全局缓存的页面数据
    public final static int PAGE_ID_ACTIVITY_OTHER_USER = MAX_GLOBAL_CACHE_ID + 1;
    public final static int PAGE_ID_ACTIVITY_SEARCH = PAGE_ID_ACTIVITY_OTHER_USER + 1;
    public final static int PAGE_ID_TOPIC_OTHER_USER = PAGE_ID_ACTIVITY_SEARCH + 1;
    public final static int PAGE_ID_TOPIC_SEARCH = PAGE_ID_TOPIC_OTHER_USER + 1;
    public final static int PAGE_ID_DISCOVER_OTHER_USER = PAGE_ID_TOPIC_SEARCH + 1;
    public final static int PAGE_ID_DISCOVER_TOPIC_HOT = PAGE_ID_DISCOVER_OTHER_USER + 1;
    public final static int PAGE_ID_DISCOVER_TOPIC_LASTED = PAGE_ID_DISCOVER_TOPIC_HOT + 1;
    public final static int PAGE_ID_DISCOVER_SEARCH = PAGE_ID_DISCOVER_TOPIC_LASTED + 1;
    public final static int PAGE_ID_ACTIVITY_COMMENT = PAGE_ID_DISCOVER_SEARCH + 1;
    public final static int PAGE_ID_ACTIVITY_ADDITION = PAGE_ID_ACTIVITY_COMMENT + 1;
    public final static int PAGE_ID_DISCOVER_COMMENT = PAGE_ID_ACTIVITY_ADDITION + 1;
    public final static int PAGE_ID_USER_SEARCH = PAGE_ID_DISCOVER_COMMENT + 1;

    // 定义可拉加载页面的数据加载类型
    public final static int PULL_TYPE_REFRESH = 0;
    public final static int PULL_TYPE_LOAD_MORE = 1;


    // 定义数据请求ULR
    public final static String URL_PREFIX = "http://112.74.13.186/CollegeAssistantServer/public/index/";
    // 活动相关的接口
    public final static String URL_ACTIVITY_FETCH = URL_PREFIX + "activities/pull";
    public final static String URL_ACTIVITY_DETAIL = URL_PREFIX + "activities/fetchDetail";
    public final static String URL_ACTIVITY_CREATE = URL_PREFIX + "activities/create";
    public final static String URL_ACTIVITY_COLLECT = URL_PREFIX + "activities/collect";
    public final static String URL_ACTIVITY_ADD_COMMENT = URL_PREFIX + "activities/addComment";
    public final static String URL_ACTIVITY_DELETE_COMMENT = URL_PREFIX + "activities/deleteComment";
    public final static String URL_ACTIVITY_ADD_ADDITION = URL_PREFIX + "activities/addAddition";
    public final static String URL_ACTIVITY_DELETE_ADDITION = URL_PREFIX + "activities/deleteAddition";
    // 话题相关的接口
    public final static String URL_TOPIC_FETCH = URL_PREFIX + "topics/pull";
    public final static String URL_TOPIC_DETAIL = URL_PREFIX + "topics/fetchDetail";
    public final static String URL_TOPIC_HOT_SIMPLE_LIST = URL_PREFIX + "topics/fetchHotSimpleList";
    public final static String URL_TOPIC_SIMPLE_LIST = URL_PREFIX + "topics/fetchSimpleList";
    public final static String URL_TOPIC_CHECK_NAME = URL_PREFIX + "topics/checkNameExist";
    public final static String URL_TOPIC_CREATE = URL_PREFIX + "topics/create";
    public final static String URL_TOPIC_VISIT = URL_PREFIX + "topics/visit";
    // 动态相关的接口
    public final static String URL_DISCOVER_FETCH = URL_PREFIX + "discovers/pull";
    public final static String URL_DISCOVER_DETAIL = URL_PREFIX + "discovers/fetchDetail";
    public final static String URL_DISCOVER_CREATE = URL_PREFIX + "discovers/create";
    public final static String URL_DISCOVER_LIKE = URL_PREFIX + "discovers/like";
    public final static String URL_DISCOVER_ADD_COMMENT = URL_PREFIX + "discovers/addComment";
    public final static String URL_DISCOVER_DELETE_COMMENT = URL_PREFIX + "discovers/deleteComment";
    // 用户相关的接口
    public final static String URL_USER_FETCH = URL_PREFIX + "users/pull";
    public final static String URL_USER_DETAIL = URL_PREFIX + "users/fetchDetail";
    public final static String URL_USER_LOGIN = URL_PREFIX + "users/login";
    public final static String URL_USER_SIGN_UP = URL_PREFIX + "users/signUp";
    public final static String URL_USER_SIGN_OUT = URL_PREFIX + "users/signOut";
    public final static String URL_USER_FETCH_AVATAR = URL_PREFIX + "users/fetchAvatar";
    public final static String URL_USER_CHECK_ACCOUNT = URL_PREFIX + "users/checkAccountExist";
    public final static String URL_USER_MODIFY_PASSWORD = URL_PREFIX + "users/modifyPassword";
    public final static String URL_USER_PULL = URL_PREFIX + "users/pull";
    public final static String URL_USER_MODIFY_PROFILE = URL_PREFIX + "users/modifyProfile";
    // 搜索相关的接口
    public final static String URL_SEARCH_COMPOSITE = URL_PREFIX + "search/composite";
    public final static String URL_SEARCH_COMPOSITE_SIMPLE = URL_PREFIX + "search/compositeSimple";
    public final static String URL_SEARCH_COMPOSITE_HOT = URL_PREFIX + "search/compositeHot";
    // 其他接口
    public final static String URL_MESSAGE_FETCH = URL_PREFIX + "messages/pull";
    public final static String URL_COMMENT_FETCH = URL_PREFIX + "comments/pull";
    public final static String URL_ADDITION_FETCH = URL_PREFIX + "additions/pull";

    public final static String URL_IMPROVE_FEEDBACK = URL_PREFIX + "improve/feedback";
    public final static String URL_IMPROVE_IMPEACH = URL_PREFIX + "improve/impeach";


    // 请求参数字段定义
    public final static String PARAM_KEY_PAGE_ID = "page_id";    // 页面ID
    public final static String PARAM_KEY_PULL_TYPE = "pull_type";    // 列表请求类型，刷新/加载更多
    public final static String PARAM_KEY_REQUEST_COUNT= "request_count"; // 请求数量
    public final static String PARAM_KEY_TYPE = "type"; // 活动类型，这个参数在其他请求中无效
    public final static String PARAM_KEY_UID = "uid";   // 当前用户的uid，默认为-1
    public final static String PARAM_KEY_OTHER_UID = "other_uid"; // 请求其他用户数据时的用户uid
    public final static String PARAM_KEY_KEYWORD = "keyword";   // 搜索页请求提供关键字参数
    public final static String PARAM_KEY_TIME_STAMP = "time_stamp"; // 列表中第一或最后一个item的时间戳
    public final static String PARAM_KEY_OFFSET = "offset"; // 列表中第一或最后一个item的时间戳
    public final static String PARAM_KEY_TOPIC_ID = "topic_id"; // 话题页中的话题id
    public final static String PARAM_KEY_TOPIC_NAME = "topic_name"; // 话题页中的话题id
    public final static String PARAM_KEY_ACTIVITY_ID = "activity_id";
    public final static String PARAM_KEY_DISCOVER_ID = "discover_id";
    public final static String PARAM_KEY_ACCOUNT = "account";
    public final static String PARAM_KEY_NICKNAME = "nickname";
    public final static String PARAM_KEY_PASSWORD = "password";
    public final static String PARAM_KEY_NEW_PASSWORD = "new_password";
    public final static String PARAM_KEY_USER = "user";
    public final static String PARAM_KEY_ACTIVITY = "activity";
    public final static String PARAM_KEY_TOPIC = "topic";
    public final static String PARAM_KEY_FEEDBACK = "feedback";
    public final static String PARAM_KEY_IMPEACH = "impeach";
    public final static String PARAM_KEY_DISCOVER = "discover";
    public final static String PARAM_KEY_POSITIVE = "positive";
    public final static String PARAM_KEY_COMMENT = "comment";
    public final static String PARAM_KEY_COMMENT_ID = "comment_id";
    public final static String PARAM_KEY_ADDITION = "addition";
    public final static String PARAM_KEY_ADDITION_ID = "addition_id";

    // 默认列表更新请求时提供的时间
    public final static String DEFAULT_TIME_STAMP = "1970-1-1 00:00:00";
    // 列表默认请求数量
    public final static int DEFAULT_REQUEST_COUNT = 50;
    // 关联话题页面中的提示列表请求数量
    public final static int DEFAULT_TOPIC_SEARCH_TIP_REQUEST_COUNT = 5;
    // 关联话题页面中用户搜索话题的请求数量
    public final static int DEFAULT_TOPIC_SEARCH_REQUEST_COUNT = 100;
    // 搜索页面中综合搜索每种类型的数据的请求数量
    public final static int COMPOSITE_SEARCH_REQUEST_COUNT = 10;

    public final static String FILE_TYPE_IMAGE = "image/*";
    public final static String UPLOAD_IMAGE_NAME = "image[]";
    public final static String UPLOAD_ACTIVITY_IMAGE_NAME = "activity_image[]";
    public final static String UPLOAD_TOPIC_IMAGE_NAME = "topic_image[]";
}
