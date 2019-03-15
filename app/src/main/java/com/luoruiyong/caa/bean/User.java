package com.luoruiyong.caa.bean;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class User {

    private int uid;
    private String avatar;
    private String nickName;
    private String realName;
    private String gender;
    private int age;
    private String cellNumber;
    private String email;
    private String description;
    private CollegeInfo collegeInfo;

    public User() {
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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
                ", nickName='" + nickName + '\'' +
                ", realName='" + realName + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", cellNumber='" + cellNumber + '\'' +
                ", email='" + email + '\'' +
                ", description='" + description + '\'' +
                ", collegeInfo=" + collegeInfo +
                '}';
    }

    public static class CollegeInfo {
        private int id;
        private String name;
        private String department;
        private String major;
        private String klass;

        public CollegeInfo() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", department='" + department + '\'' +
                    ", major='" + major + '\'' +
                    ", klass='" + klass + '\'' +
                    '}';
        }
    }

}
