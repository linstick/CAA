package com.luoruiyong.caa;

import android.os.Environment;

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

    public final static boolean VAR_RELEASE = false;
    public final static boolean VAR_DEBUG = true;
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
