package com.luoruiyong.caa.utils;

import java.util.ArrayList;
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

    public static void clear(List... lists) {
        for (List list : lists) {
            if (list != null) {
                list.clear();
            }
        }
    }

    public static List insertFirst(List src, List data) {
        if (src == null) {
            src = new ArrayList<>();
        }
        src.add(0, data);
        return src;
    }

    public static List appendLast(List src, List data) {
        if (src == null) {
            src = new ArrayList<>();
        }
        src.add(data);
        return src;
    }
}
