package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/4/18/018
 * Description:
 **/
public class ActivityCreateResult {

    private ActivityData activity;
    private TopicData topic;

    public ActivityData getActivity() {
        return activity;
    }

    public void setActivity(ActivityData activity) {
        this.activity = activity;
    }

    public TopicData getTopic() {
        return topic;
    }

    public void setTopic(TopicData topic) {
        this.topic = topic;
    }
}
