package com.luoruiyong.caa.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * Author: luoruiyong
 * Date: 2019/4/15/015
 * Description:
 **/
public class SourceUtils {

    public static void close(Closeable source) {
        if (source != null) {
            try {
                source.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
