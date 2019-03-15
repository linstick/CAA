package com.luoruiyong.caa.utils;

import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/3/16/016
 **/
public class ListUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.size() == 0;
    }

    public static int getSize(List list) {
        return list == null ? 0 : list.size();
    }

    public static boolean isIndexBetween(List list, int index) {
        return list != null && index >= 0 && index < list.size();
    }

}
