package com.luoruiyong.caa;

import com.luoruiyong.caa.bean.User;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class Enviroment {

    private static User sUser;

    public static void setCurUser(User user) {
        sUser = user;
    }

    public static User getCurUser() {
        return sUser;
    }

    public static boolean isVistor() {
        return sUser == null;
    }

    public static boolean isSelf(User user) {
        if (sUser == null || user == null) {
            return false;
        }
        return sUser.getUid() == user.getUid();
    }

    public static void clearCurUser() {
        sUser = null;
    }

    public static void createVirtualUser() {
        sUser = new User();
        sUser.setUid(128463);
        sUser.setAvatar("https://www.baidu.com/1.jpg");
        sUser.setNickName("Linstick");
        sUser.setRealName("Ruiyong Luo");
        sUser.setGender("male");
        sUser.setAge(23);
        sUser.setCellNumber("15102032936");
        sUser.setEmail("linstick@163.com");
        sUser.setDescription("An Android Developer");

        User.CollegeInfo info = new User.CollegeInfo();
        info.setId(1);
        info.setName("Guangdong University Of Technology");
        info.setDepartment("Computer Department");
        info.setMajor("Software Project");
        info.setKlass("15 Class");
        sUser.setCollegeInfo(info);
    }
}
