package com.pine.base.component.uploader.bean;

import android.view.View;

import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/5
 */

public class FileUploadBean {
    private int id;
    private int bizId;
    private int requestCode;
    private String requestUrl;
    private String fileKey;
    private int fileType;
    private String fileName;
    private String localFilePath;
    private String localTempFilePath;
    private String remoteFilePath;
    private Map<String, String> params;
    private Object originalBean;
    private View attachView;
    private int orderIndex;
    private int uploadProgress;
    private FileUploadState uploadState = FileUploadState.UPLOAD_STATE_DEFAULT;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBizId() {
        return bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getLocalTempFilePath() {
        return localTempFilePath;
    }

    public void setLocalTempFilePath(String localTempFilePath) {
        this.localTempFilePath = localTempFilePath;
    }

    public String getRemoteFilePath() {
        return remoteFilePath;
    }

    public void setRemoteFilePath(String remoteFilePath) {
        this.remoteFilePath = remoteFilePath;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Object getOriginalBean() {
        return originalBean;
    }

    public void setOriginalBean(Object originalBean) {
        this.originalBean = originalBean;
    }

    public View getAttachView() {
        return attachView;
    }

    public void setAttachView(View attachView) {
        this.attachView = attachView;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
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
}
