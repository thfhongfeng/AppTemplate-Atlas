package com.pine.base.component.editor.bean;

import android.support.annotation.NonNull;

import com.pine.base.component.uploader.bean.FileUploadState;

/**
 * Created by tanghongfeng on 2018/11/14
 */

public class TextImageEditorItemData extends TextImageItemEntity {
    private int uploadProgress;
    private FileUploadState uploadState = FileUploadState.UPLOAD_STATE_DEFAULT;

    public TextImageEditorItemData(@NonNull String type) {
        super(type);
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
