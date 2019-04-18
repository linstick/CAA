package com.luoruiyong.caa.bean;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/17/017
 * Description:
 **/
public class CompositeSearchData {
    private List<ActivityData> activities;
    private List<User> users;
    private List<DiscoverData> discovers;
    private List<TopicData> topics;

    public List<ActivityData> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityData> activities) {
        this.activities = activities;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<DiscoverData> getDiscovers() {
        return discovers;
    }

    public void setDiscovers(List<DiscoverData> discovers) {
        this.discovers = discovers;
    }

    public List<TopicData> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicData> topics) {
        this.topics = topics;
    }
}
