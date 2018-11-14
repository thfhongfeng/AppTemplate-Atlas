package com.pine.base.component.editor.bean;

import android.support.annotation.NonNull;

import com.pine.base.component.uploader.bean.FileUploadState;

/**
 * Created by tanghongfeng on 2018/11/14
 */

public class EditorItemData {
    private String type;
    private String index;
    private String text;
    private String localFilePath;
    private String remoteFilePath;
    private int uploadProgress;
    private FileUploadState uploadState = FileUploadState.UPLOAD_STATE_DEFAULT;
    private int orderNum;

    public EditorItemData(@NonNull String type) {
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

    public int getUploadProgress() {
        return uploadProgress;
    }

    public void setUploadProgress(int uploadProgress) {
        this.uploadProgress = uploadProgress;
    }

    public FileUploadState getUploadState() {
        return uploadState;
    }

    public void setUploadState(FileUploadState uploadState) {
        this.uploadState = uploadState;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
