package com.luoruiyong.caa.bean;

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

    public User() {
//        uid = 1000;
//        id = "PSC52193";
//        avatar = "https://www.baidu.com/1.jpg";
//        nickname = "会飞的猪" + ((int) (Math.random() * 10));
//        gender = "男";
//        age = 23;
//        cellNumber = "15102032936";
//        email = "linstick@163.com";
//        description = "这是用户的个性签名，我是一个Android开发入门Coder";
//        collegeInfo = new CollegeInfo();
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
        return email;
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

    public static class CollegeInfo {
        private String name;
        private String department;
        private String major;
        private String klass;

        public CollegeInfo() {
//            name = "广东工业大学";
//            department  = "计算机学院";
//            major = "软件工程";
//            klass = "15级";
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
