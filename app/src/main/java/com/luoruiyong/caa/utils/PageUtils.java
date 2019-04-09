package com.luoruiyong.caa.utils;

import android.content.Context;
import android.content.Intent;

import com.luoruiyong.caa.bean.ActivitySimpleData;
import com.luoruiyong.caa.detail.DetailActivity;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description: 页面跳转统一管理
 **/
public class PageUtils {

    public static final String KEY_USER_PROFILE_PAGE_UID = "KEY_USER_PROFILE_PAGE_UID";
    public static final String KEY_DETAIL_PAGE_TYPE = "KEY_USER_PROFILE_PAGE_UID";
    public static final String KEY_DETAIL_PAGE_DATA = "KEY_DETAIL_PAGE_DATA";
    public static final String KEY_DETAIL_PAGE_ID = "KEY_DETAIL_PAGE_ID";
    public static final String KEY_TOPIC_PAGE_ID = "KEY_TOPIC_PAGE_ID";
    public static final String KEY_TOPIC_PAGE_POSITION = "KEY_TOPIC_PAGE_POSITION";


    // 详情页中的类型数据
    public static final int DETAIL_TYPE_ACTIVITY_DATA = 0;
    public static final int DETAIL_TYPE_ACTIVITY_ID = 1;
    public static final int DETAIL_TYPE_DISCOVER_DATA = 2;
    public static final int DETAIL_TYPE_DISCOVER_ID = 3;

    /**
     * 个人页跳转条件
     * 1. 用户id
     */
    public static void gotoUserProfilePage(Context context, int uid) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra(KEY_USER_PROFILE_PAGE_UID, uid);
        context.startActivity(intent);
    }

    /**
     * 详情页跳转条件，满足其一即可
     * 1. 活动基本信息{@link ActivitySimpleData}
     * 2. 详情类型type和活动id
     * 3. 动态基本信息{@link DiscoverData}
     * 4. 详情类型type和动态id
     */
    public static void gotoActivityDetailPage(Context context, ActivitySimpleData data) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_ACTIVITY_DATA);
        intent.putExtra(KEY_DETAIL_PAGE_DATA, data);
        context.startActivity(intent);
    }

    public static void gotoActivityDetailPage(Context context, DiscoverData data) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_DISCOVER_DATA);
        intent.putExtra(KEY_DETAIL_PAGE_DATA, data);
        context.startActivity(intent);
    }

    public static void gotoActivityDetailPage(Context context, int type, long id) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, type);
        intent.putExtra(KEY_DETAIL_PAGE_ID, id);
        context.startActivity(intent);
    }

    /**
     * 话题页跳转条件
     * 1. 话题id和点击item的位置，位置默认为0
     */
    public static void gotoTopicPage(Context context, int id) {
        gotoTopicPage(context, id, 0);
    }

    public static void gotoTopicPage(Context context, int id, int position) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(KEY_TOPIC_PAGE_ID, id);
        intent.putExtra(KEY_TOPIC_PAGE_POSITION, position);
        context.startActivity(intent);
    }
}
