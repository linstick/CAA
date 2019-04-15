package com.luoruiyong.caa.bean;

import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: luoruiyong
 * Date: 2019/4/15/015
 * Description:
 **/
public class ImageBean {

    public final static int TYPE_RESOURCE_ID = 0;
    public final static int TYPE_LOCAL_FILE = 1;
    public final static int TYPE_REMOTE_FILE = 2;

    private int type;
    private String path;
    private String url;
    private int resId;

    public ImageBean() {
    }

    public ImageBean(String url) {
        this.type = TYPE_REMOTE_FILE;
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        if (type == TYPE_RESOURCE_ID) {
            return String.valueOf(resId);
        }
        if (type == TYPE_LOCAL_FILE) {
            return path;
        }
        if (type == TYPE_REMOTE_FILE) {
            return url;
        }
        return null;
    }

    public static List<String> toStringList(List<ImageBean> list) {
        if (list == null) {
            return null;
        }
        List<String> result = new ArrayList<>();
        for (ImageBean bean : list) {
            result.add(bean.toString());
        }
        return result;
    }
}
