package com.pine.base.component.editor.bean;

import android.support.annotation.NonNull;

public class TextImageItemEntity {
    // 子View条目类型null 空条目
    public final static String TYPE_NULL = "null";
    // 子View条目类型text 文本输入条目
    public final static String TYPE_TEXT = "text";
    // 子View条目类型image 图片加图片描述输入条目
    public final static String TYPE_IMAGE = "image";

    private String type;
    private String index;
    private String text;
    private String localFilePath;
    private String remoteFilePath;
    private int orderNum;

    public TextImageItemEntity(@NonNull String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
