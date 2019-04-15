package com.luoruiyong.caa.model.bean;

import com.luoruiyong.caa.Config;
import com.luoruiyong.caa.bean.ActivityData;
import com.luoruiyong.caa.bean.DiscoverData;
import com.luoruiyong.caa.bean.MessageData;
import com.luoruiyong.caa.bean.TopicData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/4/11/011
 * Description:
 **/
public class GlobalSource {

    private static Map<Integer, List<ActivityData>> sActivityData;
    private static Map<Integer, List<DiscoverData>> sDiscoverData;
    private static Map<Integer, List<TopicData>> sTopicData;
    private static List<MessageData> sMessageData;

    private static boolean sHasInit;

    public static List getData(int pageId) {
        if (pageId < Config.PAGE_ID_TOPIC_ALL) {
            return sActivityData != null ? sActivityData.get(pageId) : null;
        } else if (pageId < Config.PAGE_ID_DISCOVER_ALL) {
            return sTopicData != null ? sTopicData.get(pageId) : null;
        } else if (pageId < Config.PAGE_ID_MESSAGE) {
            return sDiscoverData != null ? sDiscoverData.get(pageId) : null;
        } else if (pageId == Config.PAGE_ID_MESSAGE) {
            return sMessageData;
        }
        return null;
    }

    public static void clearAll() {
        if (!sHasInit) {
            return;
        }
        for(Integer key : sActivityData.keySet()) {
            sActivityData.get(key).clear();
        }
        sActivityData.clear();

        for(Integer key : sTopicData.keySet()) {
            sTopicData.get(key).clear();
        }
        sTopicData.clear();

        for(Integer key : sDiscoverData.keySet()) {
            sDiscoverData.get(key).clear();
        }
        sDiscoverData.clear();

        if (sMessageData != null) {
            sMessageData.clear();
            sMessageData = null;
        }
    }

    public static void insertData(int pageId, List data) {
        initIfNeed();
        if (pageId < Config.PAGE_ID_TOPIC_ALL) {
           insertActivityData(pageId, data);
        } else if (pageId < Config.PAGE_ID_DISCOVER_ALL) {
            appendDiscoverData(pageId, data);
        } else if (pageId < Config.PAGE_ID_MESSAGE) {
            insertTopicData(pageId, data);
        } else if (pageId == Config.PAGE_ID_MESSAGE) {
            insertMessageData(data);
        }
    }

    public static void appendData(int pageId, List data) {
        initIfNeed();
        if (pageId < Config.PAGE_ID_TOPIC_ALL) {
            appendActivityData(pageId, data);
        } else if (pageId < Config.PAGE_ID_DISCOVER_ALL) {
            appendTopicData(pageId, data);
        } else if (pageId < Config.PAGE_ID_MESSAGE) {
            appendDiscoverData(pageId, data);
        } else if (pageId == Config.PAGE_ID_MESSAGE) {
            appendMessageData(data);
        }
    }

    private static void insertActivityData(int pageId, List<ActivityData> data) {
        List<ActivityData> src = sActivityData.get(pageId);
        if (src == null) {
            sActivityData.put(pageId, data);
        } else {
            src.addAll(0, data);
        }
    }

    private static void appendActivityData(int pageId, List<ActivityData> data) {
        List<ActivityData> src = sActivityData.get(pageId);
        if (src == null) {
            sActivityData.put(pageId, data);
        } else {
            src.addAll(data);
        }
    }

    private static void insertDiscoverData(int pageId, List<DiscoverData> data) {
        List<DiscoverData> src = sDiscoverData.get(pageId);
        if (src == null) {
            sDiscoverData.put(pageId, data);
        } else {
            src.addAll(0, data);
        }
    }

    private static void appendDiscoverData(int pageId, List<DiscoverData> data) {
        List<DiscoverData> src = sDiscoverData.get(pageId);
        if (src == null) {
            sDiscoverData.put(pageId, data);
        } else {
            src.addAll(data);
        }
    }

    private static void insertTopicData(int pageId, List<TopicData> data) {
        List<TopicData> src = sTopicData.get(pageId);
        if (src == null) {
            sTopicData.put(pageId, data);
        } else {
            src.addAll(0, data);
        }
    }

    private static void appendTopicData(int pageId, List<TopicData> data) {
        List<TopicData> src = sTopicData.get(pageId);
        if (src == null) {
            sTopicData.put(pageId, data);
        } else {
            src.addAll(data);
        }
    }

    private static void insertMessageData(List<MessageData> data) {
        if (sMessageData == null) {
            sMessageData = data;
        } else {
            sMessageData.addAll(0, data);
        }
    }

    private static void appendMessageData(List<MessageData> data) {
        if (sMessageData == null) {
            sMessageData = data;
        } else {
            sMessageData.addAll(data);
        }
    }

    private static List<ActivityData> getActivityData(int pageId) {
        if (sActivityData != null) {
            return sActivityData.get(pageId);
        }
        return null;
    }

    private static List<DiscoverData> getDiscoverData(int pageId) {
        if (sDiscoverData != null) {
            return sDiscoverData.get(pageId);
        }
        return null;
    }

    private static List<TopicData> getTopicData(int pageId) {
        if (sTopicData != null) {
            return sTopicData.get(pageId);
        }
        return null;
    }

    private static List<MessageData> getMessageData() {
        return sMessageData;
    }

    private static void initIfNeed() {
        if (!sHasInit) {
            sActivityData = new HashMap<>();
            sDiscoverData = new HashMap<>();
            sTopicData = new HashMap<>();
            sHasInit = true;
        }
    }
}
