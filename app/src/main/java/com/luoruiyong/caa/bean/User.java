package com.luoruiyong.caa.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class User implements Serializable{

    private int uid;
    private String id;
    private String avatar;
    private String nickname;
    private String gender;
    private int age;
    private String cellNumber;
    private String email;
    private String description;
    private CollegeInfo collegeInfo;

    private String activityCount;
    private String topicCount;
    private String discoverCount;
    private String collectCount;

    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getEmail() {
        return TextUtils.equals(email, "null") ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CollegeInfo getCollegeInfo() {
        return collegeInfo;
    }

    public void setCollegeInfo(CollegeInfo collegeInfo) {
        this.collegeInfo = collegeInfo;
    }

    public String getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(String activityCount) {
        this.activityCount = activityCount;
    }

    public String getTopicCount() {
        return topicCount;
    }

    public void setTopicCount(String topicCount) {
        this.topicCount = topicCount;
    }

    public String getDiscoverCount() {
        return discoverCount;
    }

    public void setDiscoverCount(String discoverCount) {
        this.discoverCount = discoverCount;
    }

    public String getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(String collectCount) {
        this.collectCount = collectCount;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", cellNumber='" + cellNumber + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", collegeInfo=" + collegeInfo +
                '}';
    }

    public static class CollegeInfo implements Serializable {
        private String name;
        private String department;
        private String major;
        private String klass;

        public CollegeInfo() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getKlass() {
            return klass;
        }

        public void setKlass(String klass) {
            this.klass = klass;
        }

        @Override
        public String toString() {
            return "CollegeInfo{" +
                    ", name='" + name + '\'' +
                    ", department='" + department + '\'' +
                    ", major='" + major + '\'' +
                    ", klass='" + klass + '\'' +
                    '}';
        }
    }

}
