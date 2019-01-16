package com.pine.welcome.manager;

import android.os.Environment;
import android.text.TextUtils;

import com.pine.base.exception.MessageException;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpDownloadCallback;
import com.pine.tool.util.AppUtils;
import com.pine.tool.util.LogUtils;
import com.pine.tool.util.PathUtils;
import com.pine.tool.util.SharePreferenceUtils;
import com.pine.welcome.R;
import com.pine.welcome.WelcomeConstants;
import com.pine.welcome.bean.VersionEntity;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/9/25
 */

public class ApkVersionManager {
    private final static String TAG = LogUtils.makeLogTag(ApkVersionManager.class);
    private final static int HTTP_REQUEST_DOWNLOAD = 1;
    private static volatile ApkVersionManager mInstance;
    public final Object CANCEL_SIGN = new Object();
    private String mDownloadDir = PathUtils.getExternalPublicPath(Environment.DIRECTORY_DOWNLOADS);
    private VersionEntity mVersionEntity;
    private UpdateListener mListener;

    private ApkVersionManager() {
    }

    public static ApkVersionManager getInstance() {
        if (mInstance == null) {
            synchronized (ApkVersionManager.class) {
                if (mInstance == null) {
                    mInstance = new ApkVersionManager();
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
        if (TextUtils.isEmpty(mDownloadDir)) {
            if (mListener != null) {
                mListener.onDownloadError(new MessageException(AppUtils.getApplicationByReflect()
                        .getString(R.string.wel_version_get_download_path_fail, mDownloadDir)));
            }
            return;
        }
        deleteOldApk();
        HttpRequestManager.setDownloadRequest(mVersionEntity.getPath(), mDownloadDir,
                mVersionEntity.getFileName(), HTTP_REQUEST_DOWNLOAD, CANCEL_SIGN, new HttpDownloadCallback() {
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
                        SharePreferenceUtils.saveToConfig(WelcomeConstants.APK_DOWNLOAD_FILE_PATH, filePath);
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
        return SharePreferenceUtils.readStringFromConfig(WelcomeConstants.APK_DOWNLOAD_FILE_PATH, "");
    }

    public File getDownLoadFile() {
        String apkFilePath = SharePreferenceUtils.readStringFromConfig(WelcomeConstants.APK_DOWNLOAD_FILE_PATH, "");
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
