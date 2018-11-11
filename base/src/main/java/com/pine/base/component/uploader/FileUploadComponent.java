package com.pine.base.component.uploader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.pine.base.R;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.http.HttpRequestBean;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.base.http.callback.HttpUploadCallback;
import com.pine.tool.util.FileUtils;
import com.pine.tool.util.ImageUtils;
import com.pine.tool.util.LogUtils;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    private Map<Integer, Object> mRequestMap;
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

    public void startSingle(@NonNull final FileUploadBean fileBean, final FileUploadCallback callback) {
        File file = checkFile(fileBean, callback);
        if (file == null) {
            LogUtils.d(TAG, "upload check fail");
            return;
        }
        mRequestMap.put(fileBean.hashCode(), fileBean);
        HttpRequestManager.setUploadRequest(fileBean.getRequestUrl(), fileBean.getParams(),
                fileBean.getFileKey(), fileBean.getFileName(), file, fileBean.hashCode(),
                fileBean.hashCode(), new HttpUploadCallback() {
                    @Override
                    public void onStart(int what) {
                        LogUtils.d(TAG, "onStart what :" + what);
                        if (callback != null) {
                            callback.onStart(fileBean);
                        }
                    }

                    @Override
                    public void onCancel(int what) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "onCancel what :" + what);
//                        if (callback != null) {
//                            callback.onCancel(fileBean);
//                        }
                        deleteTempFile(fileBean);
                    }

                    @Override
                    public void onProgress(int what, int progress) {
                        LogUtils.d(TAG, "onProgress what:" + what + ", progress:" + progress);
                        if (callback != null) {
                            callback.onProgress(fileBean, progress);
                        }
                    }

                    @Override
                    public boolean onError(int what, Exception e) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "onError Exception :" + e);
                        if (callback != null) {
                            callback.onError(fileBean, e.toString());
                        }
                        deleteTempFile(fileBean);
                        return false;
                    }

                    @Override
                    public void onFinish(int what) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "onFinish what:" + what);
//                        if (callback != null) {
//                            callback.onFinish(fileBean);
//                        }
                        deleteTempFile(fileBean);
                    }
                }, new HttpJsonCallback() {

                    @Override
                    public void onResponse(int what, JSONObject jsonObject) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "onResponse what:" + what);
                        if (callback != null) {
                            callback.onSuccess(fileBean, jsonObject);
                        }
                    }

                    @Override
                    public boolean onFail(int what, Exception e) {
                        mRequestMap.remove(fileBean.hashCode());
                        LogUtils.d(TAG, "onError what:" + what);
                        if (callback != null) {
                            callback.onFailed(fileBean, e.toString());
                        }
                        return false;
                    }
                });
    }

    public void startOneByOne(@NonNull List<FileUploadBean> fileBeanList, FileUploadCallback callback) {
        if (fileBeanList != null && fileBeanList.size() > 0) {
            for (int i = 0; i < fileBeanList.size(); i++) {
                startSingle(fileBeanList.get(i), callback);
            }
        }
    }

    public void startTogether(@NonNull String url, @NonNull Map<String, String> params,
                              String fileKey, final @NonNull List<FileUploadBean> fileBeanList,
                              final FileUploadListCallback callback) {
        List<HttpRequestBean.HttpFileBean> checkFileList = checkFileList(fileBeanList, callback);
        if (checkFileList == null) {
            LogUtils.d(TAG, "upload check fail");
            return;
        }
        final Map<Integer, Integer> progressMap = new HashMap<>();
        final int totalProgress = checkFileList.size() * 100;
        HttpRequestManager.setUploadRequest(url, params, fileKey, checkFileList,
                fileBeanList.hashCode(), fileBeanList.hashCode(), new HttpUploadCallback() {
                    int preActualProgress = -10;

                    @Override
                    public void onStart(int what) {
                        LogUtils.d(TAG, "onStart what :" + what);
                    }

                    @Override
                    public void onCancel(int what) {
                        LogUtils.d(TAG, "onCancel what :" + what);
                    }

                    @Override
                    public void onProgress(int what, int progress) {
                        progressMap.put(what, progress);
                        int curProgress = 0;
                        Iterator<Map.Entry<Integer, Integer>> iterator = progressMap.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<Integer, Integer> entry = iterator.next();
                            curProgress += entry.getValue();
                        }
                        int actualPro = curProgress * 100 / totalProgress;
                        if (preActualProgress + 5 <= actualPro) {
                            LogUtils.d(TAG, "onProgress what:" + what + ", progress:" + actualPro);
                            if (callback != null) {
                                callback.onProgress(fileBeanList, actualPro);
                                preActualProgress = actualPro;
                            }
                        }
                    }

                    @Override
                    public boolean onError(int what, Exception e) {
                        LogUtils.d(TAG, "onError Exception :" + e);
                        return false;
                    }

                    @Override
                    public void onFinish(int what) {
                        LogUtils.d(TAG, "onFinish what:" + what);
                    }
                }, new HttpJsonCallback() {

                    @Override
                    public void onResponse(int what, JSONObject jsonObject) {
                        mRequestMap.remove(fileBeanList.hashCode());
                        LogUtils.d(TAG, "onResponse what:" + what);
                        if (callback != null) {
                            callback.onSuccess(fileBeanList, jsonObject);
                        }
                        deleteTempFileList(fileBeanList);
                    }

                    @Override
                    public boolean onFail(int what, Exception e) {
                        mRequestMap.remove(fileBeanList.hashCode());
                        LogUtils.d(TAG, "onFail what:" + what);
                        if (callback != null) {
                            callback.onFailed(fileBeanList, e.toString());
                        }
                        deleteTempFileList(fileBeanList);
                        return false;
                    }
                });
        mRequestMap.put(fileBeanList.hashCode(), fileBeanList);
    }

    private File checkFile(FileUploadBean fileBean, FileUploadCallback callback) {
        if (TextUtils.isEmpty(fileBean.getLocalFilePath()) || TextUtils.isEmpty(fileBean.getRequestUrl())) {
            LogUtils.d(TAG, "file bean is null");
            return null;
        }
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        if (mRequestMap.get(fileBean.hashCode()) != null) {
            LogUtils.d(TAG, "request is in processing");
            if (callback != null) {
                callback.onFailed(fileBean, context.getString(R.string.base_file_upload_file_is_uploading));
            }
            return null;
        }
        File file = new File(fileBean.getLocalFilePath());
        if (!file.exists()) {
            if (callback != null) {
                callback.onFailed(fileBean, context.getString(R.string.base_file_upload_file_null));
            }
            return null;
        }
        if (fileBean.getFileType() == TYPE_IMAGE) {
            String targetPath = context.getExternalCacheDir() + File.separator + fileBean.getFileName();
            file = compressImage(fileBean.getLocalFilePath(), targetPath);
            if (file == null) {
                callback.onFailed(fileBean, context.getString(R.string.base_file_upload_compress_file_null));
                return null;
            }
            fileBean.setLocalTempFilePath(file.getPath());
        }
        return file;
    }

    private List<HttpRequestBean.HttpFileBean> checkFileList(@NonNull List<FileUploadBean> fileBeanList,
                                                             FileUploadListCallback callback) {
        if (fileBeanList == null && fileBeanList.size() < 1) {
            LogUtils.d(TAG, "no file bean in list");
            return null;
        }
        Context context = mContext.get();
        if (context == null) {
            return null;
        }
        if (mRequestMap.get(fileBeanList.hashCode()) != null) {
            LogUtils.d(TAG, "request is in processing");
            if (callback != null) {
                callback.onFailed(fileBeanList, context.getString(R.string.base_file_upload_file_is_uploading));
            }
            return null;
        }
        List<HttpRequestBean.HttpFileBean> httpFileBeanList = new ArrayList<>();
        for (int i = 0; i < fileBeanList.size(); i++) {
            FileUploadBean fileBean = fileBeanList.get(i);
            if (TextUtils.isEmpty(fileBean.getLocalFilePath())) {
                if (callback != null) {
                    callback.onFailed(fileBeanList, context.getString(R.string.base_file_upload_file_null));
                }
                return null;
            }
            File file = new File(fileBean.getLocalFilePath());
            if (!file.exists()) {
                if (callback != null) {
                    callback.onFailed(fileBeanList, context.getString(R.string.base_file_upload_file_null));
                }
                return null;
            }
            if (fileBean.getFileType() == TYPE_IMAGE) {
                String targetPath = context.getExternalCacheDir() + File.separator + fileBean.getFileName();
                file = compressImage(fileBean.getLocalFilePath(), targetPath);
                if (file == null) {
                    callback.onFailed(fileBeanList, context.getString(R.string.base_file_upload_compress_file_null));
                    return null;
                }
                fileBean.setLocalTempFilePath(file.getPath());
            }
            HttpRequestBean.HttpFileBean httpFile = new HttpRequestBean.HttpFileBean(fileBean.getFileKey(),
                    fileBean.getFileName(), file);
            httpFileBeanList.add(httpFile);
        }
        return httpFileBeanList.size() < 1 ? null : httpFileBeanList;
    }

    private File compressImage(String srcFilePath, String targetFilePath) {
        FileUtils.deleteFile(targetFilePath);
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

    private void deleteTempFile(FileUploadBean bean) {
        if (bean != null && TextUtils.isEmpty(bean.getLocalTempFilePath())) {
            new File(bean.getLocalTempFilePath()).deleteOnExit();
            bean.setLocalTempFilePath("");
        }
    }

    private void deleteTempFileList(List<FileUploadBean> list) {
        if (list == null) {
            return;
        }
        for (FileUploadBean bean : list) {
            deleteTempFile(bean);
        }
    }

    public void cancel(FileUploadBean fileBean) {
        if (fileBean != null) {
            HttpRequestManager.cancelBySign(fileBean.hashCode());
        }
    }

    public void cancel(List<FileUploadBean> fileBeanList) {
        if (fileBeanList != null) {
            HttpRequestManager.cancelBySign(fileBeanList.hashCode());
        }
    }

    public void cancelAll() {
        for (Object object : mRequestMap.values()) {
            if (object instanceof List) {
                HttpRequestManager.cancelBySign(object.hashCode());
            } else if (object instanceof FileUploadBean) {
                cancel((FileUploadBean) object);
            }
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

//        // 文件上传完成回调
//        void onFinish(FileUploadBean fileBean);
//
//        // 文件上传取消回调
//        void onCancel(FileUploadBean fileBean);

        // 文件上传失败回调
        void onError(FileUploadBean fileBean, String message);

        // 请求出错回调
        void onFailed(FileUploadBean fileBean, String message);

        // 请求成功回调
        void onSuccess(FileUploadBean fileBean, JSONObject response);
    }

    /**
     * 多文件一起上传的回调（n个文件对应一次请求）
     */
    public interface FileUploadListCallback {

        // 文件开始上传进度回调
        void onProgress(List<FileUploadBean> fileBean, int progress);

        // 请求出错回调
        void onFailed(List<FileUploadBean> fileBean, String message);

        // 请求成功回调
        void onSuccess(List<FileUploadBean> fileBean, JSONObject response);
    }
}
