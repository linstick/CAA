package com.luoruiyong.caa.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Author: luoruiyong
 * Date: 2019/5/9/009
 * Description:
 **/
public class FileUtils {

    public static void deleteFiles(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                deleteFiles(file);
            } else {
                file.delete();
            }
        }
    }

    /**
     * 获取指定文件大小
     * @param file
     * @return
     */
    public static long getFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                SourceUtils.close(fis);
            }
        }
        return size;
    }


    /**
     * 获取指定文件夹
     * @param dir
     * @return
     *
     */
    public static long getFileSizes(File dir){
        long size = 0;
        File files[] = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                size = size + getFileSizes(files[i]);
            } else {
                size = size + getFileSize(files[i]);
            }
        }
        return size;
    }



    /**
     * 格式化展示文件大小
     * @param fileSize
     * @return
     *
     */
    public static String formatFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString;
        String wrongSize = "0B";
        if (fileSize == 0) {
            return wrongSize;
        }
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "KB";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "GB";
        }
        return fileSizeString;
    }

}
