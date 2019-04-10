package com.luoruiyong.caa.puller;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: luoruiyong
 * Date: 2019/4/10/010
 * Description:
 **/
public class PullerHelper {

    public static final int TYPE_ACTIVITY = 0;
    public static final int TYPE_TOPIC = 1;
    public static final int TYPE_DISCOVER = 2;
    public static final int TYPE_MESSAGE = 3;

    public static Map<Integer, IPuller> sPullers;

    public static IPuller get(int type) {
        init();
        return sPullers.get(type);
    }

    public static void init() {
        if (sPullers == null) {
            synchronized (PullerHelper.class) {
                if (sPullers == null) {
                    sPullers = new HashMap<>();
                    sPullers.put(TYPE_ACTIVITY, new ActivityPuller());
                    sPullers.put(TYPE_TOPIC, new TopicPuller());
                    sPullers.put(TYPE_DISCOVER, new DiscoverPuller());
                    sPullers.put(TYPE_MESSAGE, new MessagePuller());
                }
            }
        }
    }

}
