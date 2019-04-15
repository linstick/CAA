package com.luoruiyong.caa.utils;

import android.content.Context;
import android.content.Intent;

import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.TopicData;
import com.luoruiyong.caa.detail.DetailActivity;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.feedback.FeedbackActivity;
import com.luoruiyong.caa.topic.TopicActivity;
import com.luoruiyong.caa.user.UserProfileActivity;

import java.io.Serializable;

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
    public static final String KEY_DETAIL_PAGE_BROWSE_COMMENT = "KEY_DETAIL_PAGE_BROWSE_COMMENT";
    public static final String KEY_TOPIC_PAGE_ID = "KEY_TOPIC_PAGE_ID";
    public static final String KEY_TOPIC_PAGE_POSITION = "KEY_TOPIC_PAGE_POSITION";
    public static final String KEY_FEEDBACK_PAGE_DATA = "KEY_FEEDBACK_PAGE_DATA";
    public static final String KEY_FEEDBACK_PAGE_TYPE = "KEY_FEEDBACK_PAGE_TYPE";


    // 详情页中的类型数据
    public static final int DETAIL_TYPE_ACTIVITY_DATA = 0;
    public static final int DETAIL_TYPE_ACTIVITY_ID = 1;
    public static final int DETAIL_TYPE_DISCOVER_DATA = 2;
    public static final int DETAIL_TYPE_DISCOVER_ID = 3;

    // 话题页中的类型数据
    public static final int DETAIL_TYPE_TOPIC_DATA = 0;
    public static final int DETAIL_TYPE_TOPIC_ID = 1;

    public static final int FEEDBACK_TYPE_SUGGESTION_OR_PROBLEM = 0;
    public static final int FEEDBACK_TYPE_IMPEACH_ACTIVITY = 1;
    public static final int FEEDBACK_TYPE_IMPEACH_TOPIC = 2;
    public static final int FEEDBACK_TYPE_IMPEACH_DISCOVER = 3;
    public static final int FEEDBACK_TYPE_IMPEACH_USER = 4;
    public static final int FEEDBACK_TYPE_IMPEACH_COMMENT = 5;

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
     * 详情页跳转条件，123为并列条件
     * 1. 是否点击评论进入
     * 2. 详情类型type
     * 3. 活动基本信息{@link ActivityData} | 活动ID | 动态基本信息{@link DiscoverData} | 动态ID
     */
    public static void gotoActivityDetailPage(Context context, ActivityData data) {
        gotoActivityDetailPage(context, data, false);
    }

    public static void gotoActivityDetailPage(Context context, ActivityData data, boolean isComment) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_ACTIVITY_DATA);
        intent.putExtra(KEY_DETAIL_PAGE_DATA, data);
        intent.putExtra(KEY_DETAIL_PAGE_BROWSE_COMMENT, isComment);
        context.startActivity(intent);
    }

    public static void gotoActivityDetailPage(Context context, DiscoverData data) {
        gotoActivityDetailPage(context, data, false);
    }

    public static void gotoActivityDetailPage(Context context, DiscoverData data, boolean isComment) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_DISCOVER_DATA);
        intent.putExtra(KEY_DETAIL_PAGE_DATA, data);
        intent.putExtra(KEY_DETAIL_PAGE_BROWSE_COMMENT, isComment);
        context.startActivity(intent);
    }

    public static void gotoActivityDetailPage(Context context, int type, int id) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, type);
        intent.putExtra(KEY_DETAIL_PAGE_ID, id);
        intent.putExtra(KEY_DETAIL_PAGE_BROWSE_COMMENT, false);
        context.startActivity(intent);
    }

    /**
     * 话题页跳转条件
     * 1. 话题id和点击item的位置，位置默认为0
     */
    public static void gotoTopicPage(Context context, TopicData data) {
        gotoTopicPage(context, data, 0);
    }

    public static void gotoTopicPage(Context context, TopicData data, int position) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(KEY_DETAIL_PAGE_DATA, data);
        intent.putExtra(KEY_TOPIC_PAGE_POSITION, position);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_TOPIC_DATA);
        context.startActivity(intent);
    }

    public static void gotoTopicPage(Context context, int id) {
        Intent intent = new Intent(context, TopicActivity.class);
        intent.putExtra(KEY_TOPIC_PAGE_ID, id);
        intent.putExtra(KEY_DETAIL_PAGE_TYPE, DETAIL_TYPE_TOPIC_ID);
        context.startActivity(intent);
    }

    public static void gotoFeedbackPage(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(KEY_FEEDBACK_PAGE_TYPE, FEEDBACK_TYPE_SUGGESTION_OR_PROBLEM);
        context.startActivity(intent);
    }

    public static void gotoFeedbackPage(Context context, Serializable data) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(KEY_FEEDBACK_PAGE_DATA, data);
        context.startActivity(intent);
    }
}
