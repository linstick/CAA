package com.luoruiyong.caa;

import com.luoruiyong.caa.bean.User;
import com.luoruiyong.caa.utils.ResourcesUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/14/014
 **/
public class Enviroment {

    private static User sUser;
    private static List<String> sCreateNewStringArray;
    private static List<String> sItemMoreStringArray;

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
        return user != null && isSelf(user.getUid());
    }

    public static boolean isSelf(long uid) {
        return sUser != null && sUser.getUid() == uid;
    }

    public static void clearCurUser() {
        sUser = null;
    }

    public static void createVirtualUser() {
        sUser = new User();
        sUser.setUid(1000);
        sUser.setAvatar("https://www.baidu.com/1.jpg");
        sUser.setNickName("会飞的猪");
        sUser.setRealName("罗瑞泳");
        sUser.setGender("男");
        sUser.setAge(23);
        sUser.setCellNumber("15102032936");
        sUser.setEmail("linstick@163.com");
        sUser.setDescription("Android开发入门Coder");

        User.CollegeInfo info = new User.CollegeInfo();
        info.setId(1);
        info.setName("Guangdong University Of Technology");
        info.setDepartment("Computer Department");
        info.setMajor("Software Project");
        info.setKlass("15 Class");
        sUser.setCollegeInfo(info);
    }

    public static List<String> getCreateNewStringArray() {
        if (sCreateNewStringArray == null) {
            sCreateNewStringArray = Arrays.asList(ResourcesUtils.getStringArray(R.array.str_array_create_new));
        }
        return sCreateNewStringArray;
    }

    public static List<String> getItemMoreNewStringArray() {
        if (sItemMoreStringArray == null) {
            sItemMoreStringArray = Arrays.asList(ResourcesUtils.getStringArray(R.array.str_array_item_more));
        }
        return sItemMoreStringArray;
    }
}
