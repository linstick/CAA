package com.luoruiyong.caa;

import android.app.Application;

import com.luoruiyong.caa.user.User;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class MyApplication extends Application {

    private static User sCurUser;

    @Override
    public void onCreate() {
        super.onCreate();

        // for test
        sCurUser = createVirtualUser();
    }

    public static User createVirtualUser() {
        User user = new User();
        user.setUid(128463);
        user.setAvatar("https://www.baidu.com/1.jpg");
        user.setNickName("Linstick");
        user.setRealName("Ruiyong Luo");
        user.setGender("male");
        user.setAge(23);
        user.setCellNumber("15102032936");
        user.setEmail("linstick@163.com");
        user.setDescription("An Android Developer");

        User.CollegeInfo info = new User.CollegeInfo();
        info.setId(1);
        info.setName("Guangdong University Of Technology");
        info.setDepartment("Computer Department");
        info.setMajor("Software Project");
        info.setKlass("15 Class");
        user.setCollegeInfo(info);
        return user;
    }

    public static User getCurUser() {
        return sCurUser;
    }

    public static void setCurUser(User user) {
        sCurUser = user;
    }
}
