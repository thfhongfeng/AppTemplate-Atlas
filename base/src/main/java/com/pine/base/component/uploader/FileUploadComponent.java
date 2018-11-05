package com.pine.base.component.uploader;

import android.content.Context;
import android.text.TextUtils;

import com.pine.base.R;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.base.http.callback.HttpUploadCallback;
import com.pine.tool.util.ImageUtils;
import com.pine.tool.util.LogUtils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/11/1
 */

public class FileUploadComponent {
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_WORD_DOC = 2;
    public static final int TYPE_TXT = 3;
    private static final String TAG = LogUtils.makeLogTag(FileUploadComponent.class);
    private WeakReference<Context> mContext;
    private Map<Integer, FileUploadBean> mRequestMap;
    private long mMaxFileSize = 1024 * 1024;
    private int mOutFileWidth = 1440;
    private int mOutFileHeight = 2550;

    public FileUploadComponent(Context context) {
        mContext = new WeakReference<>(context);
        mRequestMap = new HashMap<>();
    }

    public FileUploadComponent(Context context, long maxFileSize) {
        mContext = new WeakReference<>(context);
        mRequestMap = new HashMap<>();
        mMaxFileSize = maxFileSize;
    }

    public FileUploadComponent(Context context, long maxFileSize, int outFileWidth, int outFileHeight) {
        mContext = new WeakReference<>(context);
        mRequestMap = new HashMap<>();
        mMaxFileSize = maxFileSize;
        mOutFileWidth = outFileWidth;
        mOutFileHeight = outFileHeight;
    }

    private void start(final FileUploadBean fileBean, final FileUploadCallback callback) {
        if (TextUtils.isEmpty(fileBean.getLocalFilePath()) || TextUtils.isEmpty(fileBean.getRequestUrl())) {
            return;
        }
        if (mRequestMap.get(fileBean.getLocalFilePath()) != null) {
            return;
        }
        Context context = mContext.get();
        if (context == null) {
            return;
        }

        File file = new File(fileBean.getLocalFilePath());
        if (!file.exists()) {
            if (callback != null) {
                callback.onFailed(fileBean, context.getString(R.string.base_file_upload_file_null));
            }
            return;
        }
        if (fileBean.getFileType() == TYPE_IMAGE) {
            String targetPath = context.getExternalCacheDir() + File.separator + fileBean.getFileName();
            file = compressImage(fileBean.getLocalFilePath(), targetPath);
            fileBean.setLocalTempFilePath(file.getPath());
        }
        HashMap<String, String> params = fileBean.getParams();
        HttpRequestManager.setPostFileRequest(fileBean.getRequestUrl(), fileBean.hashCode(), params,
                file, fileBean.getFileName(), fileBean.getFileKey(), fileBean.hashCode(), new HttpUploadCallback() {
                    @Override
                    public void onStart(int what) {
                        if (callback != null) {
                            callback.onStart(fileBean);
                        }
                    }

                    @Override
                    public void onCancel(int what) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "Response-onCancel what :" + what);
                        if (callback != null) {
                            callback.onCancel(fileBean);
                        }
                        deleteTempFile(fileBean);
                    }

                    @Override
                    public void onProgress(int what, int progress) {
                        LogUtils.d(TAG, "Response-inProgress what:" + what + ", progress:" + progress);
                        if (callback != null) {
                            callback.onProgress(fileBean, progress);
                        }
                    }

                    @Override
                    public boolean onError(int what, Exception e) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "Response-onError Exception :" + e);
                        if (callback != null) {
                            callback.onFailed(fileBean, e.toString());
                        }
                        deleteTempFile(fileBean);
                        return false;
                    }

                    @Override
                    public void onFinish(int what) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "Response-onFinish what:" + what);
                        if (callback != null) {
                            callback.onFinish(fileBean);
                        }
                        deleteTempFile(fileBean);
                    }
                }, new HttpJsonCallback() {

                    @Override
                    public void onResponse(int what, JSONObject jsonObject) {
                        if (callback != null) {
                            callback.onSuccess(fileBean, jsonObject);
                        }
                    }

                    @Override
                    public boolean onError(int what, Exception e) {
                        if (callback != null) {
                            callback.onError(fileBean, e.toString());
                        }
                        return false;
                    }
                });
        mRequestMap.put(fileBean.hashCode(), fileBean);
    }

    private void deleteTempFile(FileUploadBean bean) {
        if (bean != null && TextUtils.isEmpty(bean.getLocalTempFilePath())) {
            new File(bean.getLocalTempFilePath()).deleteOnExit();
            bean.setLocalTempFilePath("");
        }
    }

    private File compressImage(String srcFilePath, String targetFilePath) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageUtils.compressBySize(srcFilePath, mMaxFileSize, mOutFileWidth, mOutFileHeight, bao);
        File targetFile = new File(targetFilePath);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(targetFile));
            bao.writeTo(bos);
            bos.flush();
            bos.close();
            return targetFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void start(List<FileUploadBean> fileBeanList, FileUploadCallback callback) {
        if (fileBeanList != null && fileBeanList.size() > 0) {
            for (int i = 0; i < fileBeanList.size(); i++) {
                start(fileBeanList.get(i), callback);
            }
        }
    }

    public void cancel(FileUploadBean fileBean) {
        if (fileBean != null) {
            HttpRequestManager.cancelBySign(fileBean.hashCode());
        }
    }

    public void cancelAll() {
        for (FileUploadBean bean : mRequestMap.values()) {
            cancel(bean);
        }
        mRequestMap.clear();
    }

    /**
     * 一个一个上传文件的回调（n个文件对应n次请求）
     */
    public interface FileUploadCallback {
        // 文件开始上传回调
        void onStart(FileUploadBean fileBean);

        // 文件开始上传进度回调
        void onProgress(FileUploadBean fileBean, int progress);

        // 文件上传完成回调
        void onFinish(FileUploadBean fileBean);

        // 文件上传取消回调
        void onCancel(FileUploadBean fileBean);

        // 文件上传失败回调
        void onFailed(FileUploadBean fileBean, String message);

        // 请求出错回调
        void onError(FileUploadBean fileBean, String message);

        // 请求成功回调
        void onSuccess(FileUploadBean fileBean, JSONObject response);
    }

    /**
     * 多文件一起上传的回调（n个文件对应一次请求）
     */
    public interface FileUploadListCallback {

    }
}
