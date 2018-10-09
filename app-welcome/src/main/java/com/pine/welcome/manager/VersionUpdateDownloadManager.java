package com.pine.welcome.manager;

import android.os.Environment;
import android.text.TextUtils;

import com.pine.base.http.HttpRequestManagerProxy;
import com.pine.base.http.callback.HttpDownloadCallback;
import com.pine.tool.util.LogUtils;
import com.pine.tool.util.SharePreferenceUtils;
import com.pine.welcome.WelcomeConstants;
import com.pine.welcome.bean.VersionEntity;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/9/25
 */

public class VersionUpdateDownloadManager {
    private final static String TAG = LogUtils.makeLogTag(VersionUpdateDownloadManager.class);
    private final static int HTTP_REQUEST_DOWNLOAD = 1;
    private static volatile VersionUpdateDownloadManager mInstance;
    private String mDownloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    private VersionEntity mVersionEntity;
    private UpdateListener mListener;

    private VersionUpdateDownloadManager() {
    }

    public static VersionUpdateDownloadManager getInstance() {
        if (mInstance == null) {
            synchronized (VersionUpdateDownloadManager.class) {
                if (mInstance == null) {
                    mInstance = new VersionUpdateDownloadManager();
                }
            }
        }
        return mInstance;
    }

    public void setVersionEntity(VersionEntity entity) {
        mVersionEntity = entity;
    }

    public void startUpdate(UpdateListener listener) {
        mListener = listener;
        startDownloadTask();
    }

    private void startDownloadTask() {
        deleteOldApk();
        HttpRequestManagerProxy.setDownloadRequest(mVersionEntity.getPath(), mDownloadDir,
                mVersionEntity.getFileName(), TAG, HTTP_REQUEST_DOWNLOAD, new HttpDownloadCallback() {
                    @Override
                    public void onStart(int what, boolean isResume, long rangeSize, long allCount) {
                        if (mListener != null) {
                            mListener.onDownloadStart(isResume, rangeSize, allCount);
                        }
                    }

                    @Override
                    public void onProgress(int what, int progress, long fileCount, long speed) {
                        if (mListener != null) {
                            mListener.onDownloadProgress(progress, fileCount, speed);
                        }
                    }

                    @Override
                    public void onFinish(int what, String filePath) {
                        SharePreferenceUtils.saveStringToConfig(WelcomeConstants.APK_DOWNLOAD_FILE_PATH, filePath);
                        if (mListener != null) {
                            mListener.onDownloadComplete(filePath);
                        }
                    }

                    @Override
                    public void onCancel(int what) {
                        if (mListener != null) {
                            mListener.onDownloadCancel();
                        }
                    }

                    @Override
                    public boolean onError(int what, Exception e) {
                        if (mListener != null) {
                            mListener.onDownloadError(e);
                        }
                        return true;
                    }
                });
    }

    private void deleteOldApk() {
        String apkFilePath = getDownLoadFilePath();
        if (TextUtils.isEmpty(apkFilePath)) {
            return;
        }
        File folder = new File(mDownloadDir);
        if (!folder.exists()) {
            return;
        }
        File downloadedFile = new File(apkFilePath);
        if (downloadedFile != null && downloadedFile.exists() && downloadedFile.isFile()) {
            downloadedFile.delete();
        }
    }

    public String getDownLoadFilePath() {
        return SharePreferenceUtils.readStringFromConfig(WelcomeConstants.APK_DOWNLOAD_FILE_PATH);
    }

    public File getDownLoadFile() {
        String apkFilePath = SharePreferenceUtils.readStringFromConfig(WelcomeConstants.APK_DOWNLOAD_FILE_PATH);
        return new File(apkFilePath);
    }

    public interface UpdateListener {

        void onDownloadStart(boolean isResume, long rangeSize, long allCount);

        void onDownloadProgress(int progress, long fileCount, long speed);

        void onDownloadComplete(String filePath);

        void onDownloadCancel();

        void onDownloadError(Exception exception);
    }
}
