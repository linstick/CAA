package com.luoruiyong.caa;

import android.os.Environment;
import android.text.TextUtils;

import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.utils.LogUtils;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class Enviroment {

    public final static int LOG_GRADE = LogUtils.DEBUG;

    public final static String AMAP_WEB_API_KEY = "c3bab68b9ed24b289631570eb02750fa";
    public final static String CACHE_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/CAA/";
    public final static String TEMP_FILE_NAME = "temp.jpeg";
    public final static String IMAGE_SUFFIX = ".jpg";

    private static User sUser;
    //创建新帖子的弹框列表
    private static List<String> sCreateNewStringArray;
    //子项中更多的弹框列表
    private static List<String> sItemMoreStringArray;
    //活动类型中编号与名字的映射表
    private static Map<Integer, String> sActivityTypeMap;
    //消息类型中编号与名字的映射表
    private static Map<Integer, String> sMessageTypeMap;
    //列表页面无数据时内容提示映射表
    private static Map<Integer, String> sNoDataTipMap;
    //列表页面无数据时操作提示映射表
    private static Map<Integer, String> sNoDataOperateTipMap;

    public static void setCurUser(User user) {
        sUser = user;
    }

    public static User getCurUser() {
        return sUser;
    }

    public static int getCurUid() {
        return sUser == null ? -1 : sUser.getUid();
    }

    public static boolean isVisitor() {
        return sUser == null;
    }

    public static boolean isSelf(User user) {
        return user != null && isSelf(user.getUid());
    }

    public static boolean isSelf(long uid) {
        return sUser != null && sUser.getUid() == uid;
    }

    public static void clearCurUser() {
        sUser = null;
    }

    public static List<String> getCreateNewStringArray() {
        if (sCreateNewStringArray == null) {
            sCreateNewStringArray = Arrays.asList(ResourcesUtils.getStringArray(R.array.str_array_create_new));
        }
        return sCreateNewStringArray;
    }

    public static List<String> getItemMoreNewStringArray() {
        if (sItemMoreStringArray == null) {
            sItemMoreStringArray = Arrays.asList(ResourcesUtils.getStringArray(R.array.str_array_item_more));
        }
        return sItemMoreStringArray;
    }

    private static void initActivityTypeMap() {
        if (sActivityTypeMap == null) {
            String[] items = ResourcesUtils.getStringArray(R.array.str_array_activity_type);
            sActivityTypeMap = new HashMap<>(items.length);
            for (int i = 0; i < items.length; i++) {
                sActivityTypeMap.put(i, items[i]);
            }
        }
    }

    public static Map<Integer, String> getActivityTypeMap() {
        initActivityTypeMap();
        return sActivityTypeMap;
    }

    public static String getActivityTypeNameById(int id) {
        initActivityTypeMap();
        return sActivityTypeMap.get(id);
    }

    private static void initMessageTypeMap() {
        if (sMessageTypeMap == null) {
            String[] items = ResourcesUtils.getStringArray(R.array.str_array_message_type);
            sMessageTypeMap = new HashMap<>(items.length);
            for (int i = 0; i < items.length; i++) {
                sMessageTypeMap.put(i, items[i]);
            }
        }
    }

    private static void initNoDataTipMapIfNeed() {
        if (sNoDataTipMap == null) {
            sNoDataTipMap = new HashMap<>();
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_ALL, ResourcesUtils.getString(R.string.common_tip_no_activity_data));
            sNoDataTipMap.put(Config.PAGE_ID_TOPIC_ALL, ResourcesUtils.getString(R.string.common_tip_no_topic_data));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_ALL, ResourcesUtils.getString(R.string.common_tip_no_discover_data));
            sNoDataTipMap.put(Config.PAGE_ID_MESSAGE, ResourcesUtils.getString(R.string.common_tip_no_message_data));
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_SELF, ResourcesUtils.getString(R.string.common_tip_no_self_activity_data));
            sNoDataTipMap.put(Config.PAGE_ID_TOPIC_SELF, ResourcesUtils.getString(R.string.common_tip_no_self_topic_data));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_SELF, ResourcesUtils.getString(R.string.common_tip_no_self_discover_data));
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_SELF_COLLECT, ResourcesUtils.getString(R.string.common_tip_no_self_collect_data));
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_OTHER_USER, ResourcesUtils.getString(R.string.common_tip_no_other_user_activity_data));
            sNoDataTipMap.put(Config.PAGE_ID_TOPIC_OTHER_USER, ResourcesUtils.getString(R.string.common_tip_no_other_user_topic_data));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_OTHER_USER, ResourcesUtils.getString(R.string.common_tip_no_other_user_discover_data));
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_COMMENT, ResourcesUtils.getString(R.string.common_tip_no_comment));
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_ADDITION, ResourcesUtils.getString(R.string.common_tip_no_addition));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_COMMENT, ResourcesUtils.getString(R.string.common_tip_no_comment));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_TOPIC_HOT, ResourcesUtils.getString(R.string.common_tip_no_user_join_topic));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_TOPIC_LASTED, ResourcesUtils.getString(R.string.common_tip_no_user_join_topic));
            sNoDataTipMap.put(Config.PAGE_ID_ACTIVITY_SEARCH, ResourcesUtils.getString(R.string.common_tip_no_related_activity));
            sNoDataTipMap.put(Config.PAGE_ID_USER_SEARCH, ResourcesUtils.getString(R.string.common_tip_no_related_user));
            sNoDataTipMap.put(Config.PAGE_ID_TOPIC_SEARCH, ResourcesUtils.getString(R.string.common_tip_no_related_topic));
            sNoDataTipMap.put(Config.PAGE_ID_DISCOVER_SEARCH, ResourcesUtils.getString(R.string.common_tip_no_related_discover));
            sNoDataTipMap.put(Config.PAGE_ID_NONE, ResourcesUtils.getString(R.string.common_tip_no_related_content));
        }
    }

    public static String getNoDataTipByPageId(int pageId) {
        initNoDataTipMapIfNeed();
        if (pageId <= Config.PAGE_ID_ACTIVITY_ONE_KIND) {
            return sNoDataTipMap.get(Config.PAGE_ID_ACTIVITY_ALL);
        }
        String tip = sNoDataTipMap.get(pageId);
        return TextUtils.isEmpty(tip) ? sNoDataTipMap.get(Config.PAGE_ID_NONE) : tip;
    }

    public static void initNoDataOperateTipMapIfNeed() {
        if (sNoDataOperateTipMap == null) {
            sNoDataOperateTipMap = new HashMap<>();
            sNoDataOperateTipMap.put(Config.PAGE_ID_ACTIVITY_SELF, ResourcesUtils.getString(R.string.common_tip_go_to_create_activity));
            sNoDataOperateTipMap.put(Config.PAGE_ID_TOPIC_SELF, ResourcesUtils.getString(R.string.common_tip_go_to_create_topic));
            sNoDataOperateTipMap.put(Config.PAGE_ID_DISCOVER_SELF, ResourcesUtils.getString(R.string.common_tip_go_to_create_discover));
            sNoDataOperateTipMap.put(Config.PAGE_ID_ACTIVITY_COMMENT, ResourcesUtils.getString(R.string.common_tip_add_comment));
            sNoDataOperateTipMap.put(Config.PAGE_ID_DISCOVER_COMMENT, ResourcesUtils.getString(R.string.common_tip_add_comment));
        }
    }

    public static String getNoDataOperateTipByPageId(int pageId) {
        initNoDataOperateTipMapIfNeed();
        if (pageId <= Config.MAX_GLOBAL_CACHE_ID || pageId == Config.PAGE_ID_DISCOVER_TOPIC_HOT || pageId == Config.PAGE_ID_DISCOVER_TOPIC_LASTED) {
            return ResourcesUtils.getString(R.string.common_str_refresh);
        }
        return sNoDataOperateTipMap.get(pageId);
    }

    public static String getCacheFolder() {
        File dir = new File(CACHE_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return CACHE_FOLDER;
    }

    public static String getTempFilePath() {
        return getCacheFolder() + TEMP_FILE_NAME;
    }

    public static Map<Integer, String> getMessageTypeMap() {
        initMessageTypeMap();
        return sMessageTypeMap;
    }

    public static String getMessageTypeNameById(int id) {
        initMessageTypeMap();
        return sMessageTypeMap.get(id);
    }
}
