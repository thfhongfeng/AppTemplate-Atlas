package com.pine.base.component.uploader.bean;

/**
 * Created by tanghongfeng on 2018/11/14
 */

public enum FileUploadState {
    UPLOAD_STATE_DEFAULT,
    UPLOAD_STATE_PREPARING,
    UPLOAD_STATE_UPLOADING,
    UPLOAD_STATE_CANCEL,
    UPLOAD_STATE_FAIL,
    UPLOAD_STATE_SUCCESS
}
