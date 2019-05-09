package com.luoruiyong.caa.utils;

import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.MessageData;
import com.luoruiyong.caa.bean.TopicData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ListUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static int getSize(List list) {
        return list == null ? 0 : list.size();
    }

    public static boolean isIndexBetween(List list, int index) {
        return list != null && index >= 0 && index < list.size();
    }

    public static void clear(List... lists) {
        for (List list : lists) {
            if (list != null) {
                list.clear();
            }
        }
    }

    public static List insertFirst(List src, List data) {
        if (src == null) {
            src = new ArrayList<>();
        }
        src.add(0, data);
        return src;
    }

    public static List appendLast(List src, List data) {
        if (src == null) {
            src = new ArrayList<>();
        }
        src.add(data);
        return src;
    }

    public static void deleteActivityItem(List<ActivityData> list, ActivityData data) {
        if (list == null || data == null) {
            return;
        }
        Iterator<ActivityData> iterator = list.iterator();
        while (iterator.hasNext()) {
            ActivityData item = iterator.next();
            if (item.getId() == data.getId()) {
                iterator.remove();
                break;
            }
        }
    }

    public static void deleteTopicItem(List<TopicData> list, TopicData data) {
        if (list == null || data == null) {
            return;
        }
        Iterator<TopicData> iterator = list.iterator();
        while (iterator.hasNext()) {
            TopicData item = iterator.next();
            if (item.getId() == data.getId()) {
                iterator.remove();
                break;
            }
        }
    }

    public static void deleteDiscoverItem(List<DiscoverData> list, DiscoverData data) {
        if (list == null || data == null) {
            return;
        }
        Iterator<DiscoverData> iterator = list.iterator();
        while (iterator.hasNext()) {
            DiscoverData item = iterator.next();
            if (item.getId() == data.getId()) {
                iterator.remove();
                break;
            }
        }
    }

    public static void deleteMessageItem(List<MessageData> list, MessageData data) {
        if (list == null || data == null) {
            return;
        }
        Iterator<MessageData> iterator = list.iterator();
        while (iterator.hasNext()) {
            MessageData item = iterator.next();
            if (item.getId() == data.getId()) {
                iterator.remove();
                break;
            }
        }
    }
}
