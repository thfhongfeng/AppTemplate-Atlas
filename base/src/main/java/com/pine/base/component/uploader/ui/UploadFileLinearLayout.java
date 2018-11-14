package com.pine.base.component.uploader.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.component.image_selector.ImageSelector;
import com.pine.base.component.image_selector.ImageViewer;
import com.pine.base.component.uploader.FileUploadComponent;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.component.uploader.bean.FileUploadState;
import com.pine.base.ui.BaseActivity;
import com.pine.base.widget.ILifeCircleView;
import com.pine.tool.util.FileUtils;
import com.pine.tool.util.LogUtils;
import com.pine.tool.util.PathUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by tanghongfeng on 2018/11/13
 */

public abstract class UploadFileLinearLayout extends LinearLayout implements ILifeCircleView {
    private static final String TAG = LogUtils.makeLogTag(UploadFileLinearLayout.class);
    private final int REQUEST_CODE_CROP = 10000 + hashCode() % 10000 + new Random().nextInt(100);
    private final int REQUEST_CODE_SELECT_IMAGE = REQUEST_CODE_CROP + 1;
    // 每次选择文件最大允数
    private final int MAX_PER_UPLOAD_FILE_COUNT = 10;
    protected BaseActivity mActivity;
    // 图片需要压缩时（图片大小大于允许上传大小时）的输出宽度和高度
    protected int mCompressImageWidth = 1440;
    protected int mCompressImageHeight = 2550;
    // 每张图片最大允许上传大小
    protected long mMaxImageSize = 1024 * 1024;
    protected int mCropWidth = 360;
    protected int mCropHeight = 360;
    // 最大允许上传文件数
    protected int mMaxFileCount = 30;
    // 文件上传组件
    protected FileUploadComponent mFileUploadComponent;
    private int mFileType = FileUploadComponent.TYPE_IMAGE;
    // 是否支持图片裁剪
    private boolean mEnableCrop;
    private String mCurCropPhotoPath;
    private List<String> mCropPathList = new ArrayList<>();
    // 文件上传线程
    private HandlerThread mHandlerThread;
    // 文件上传线程的Handler
    private Handler mThreadHandler;
    // 主线程Handler
    private Handler mMainHandler;
    // 上传文件的服务器地址
    private String mUploadFileUrl;
    private boolean mIsInit;
    private OneByOneUploadAdapter mOneByOneUploadAdapter;
    private TogetherUploadAdapter mTogetherUploadAdapter;
    private boolean mTogetherUploadMode;

    public UploadFileLinearLayout(Context context) {
        super(context);
    }

    public UploadFileLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UploadFileLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initUpload(@NonNull BaseActivity activity, @NonNull String uploadUrl,
                              int fileType, @NonNull OneByOneUploadAdapter adapter) {
        mActivity = activity;
        if (mActivity == null) {
            throw new IllegalStateException("Activity should not be empty");
        }
        activity.attachCircleView(this);
        mTogetherUploadMode = false;
        mUploadFileUrl = uploadUrl;
        if (TextUtils.isEmpty(mUploadFileUrl)) {
            throw new IllegalStateException("Upload url should not be empty");
        }
        mFileType = fileType;
        mOneByOneUploadAdapter = adapter;
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        mIsInit = true;
    }

    public void initUpload(@NonNull BaseActivity activity, @NonNull String uploadUrl,
                           int fileType, @NonNull TogetherUploadAdapter adapter) {
        mActivity = activity;
        if (mActivity == null) {
            throw new IllegalStateException("Activity should not be empty");
        }
        activity.attachCircleView(this);
        mTogetherUploadMode = true;
        mUploadFileUrl = uploadUrl;
        if (TextUtils.isEmpty(mUploadFileUrl)) {
            throw new IllegalStateException("Upload url should not be empty");
        }
        mFileType = fileType;
        mTogetherUploadAdapter = adapter;
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        mIsInit = true;
    }

    public void setCropEnable() {
        mEnableCrop = mFileType == FileUploadComponent.TYPE_IMAGE;
    }

    public void setCropEnable(int cropWidth, int cropHeight) {
        if (mFileType == FileUploadComponent.TYPE_IMAGE) {
            mEnableCrop = true;
            mCropWidth = cropWidth;
            mCropHeight = cropHeight;
        } else {
            mEnableCrop = false;
        }
    }

    public void setMaxFileCount(int maxFileCount) {
        mMaxFileCount = maxFileCount;
    }

    public void setMaxImageSize(int maxImageSize) {
        mMaxImageSize = maxImageSize;
    }

    /**
     * 打开系统裁剪功能
     */
    protected void startCropImage(String filePath) {
        String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        String targetFilePath = PathUtils.getAppFilePath(Environment.DIRECTORY_PICTURES) +
                File.separator + "crop_" + System.currentTimeMillis() + "_" + fileName;

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
        intent.putExtra("crop", true);
        if (mCropWidth >= mCropHeight) {
            // 设置x,y的比例，截图方框就按照这个比例来截 若设置为0,0，或者不设置 则自由比例截图
            intent.putExtra("aspectX", mCropWidth / mCropHeight);
            intent.putExtra("aspectY", 1);
        } else {
            // 设置x,y的比例，截图方框就按照这个比例来截 若设置为0,0，或者不设置 则自由比例截图
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", mCropHeight / mCropWidth);
        }
        // 裁剪区的宽和高 其实就是裁剪后的显示区域 若裁剪的比例不是显示的比例，
        // 则自动压缩图片填满显示区域。若设置为0,0 就不显示。若不设置，则按原始大小显示
        intent.putExtra("outputX", mCropWidth);
        intent.putExtra("outputY", mCropHeight);
        intent.putExtra("scale", true);
        // true的话直接返回bitmap，可能会很占内存 不建议
        intent.putExtra("return-data", false);
        // 上面设为false的时候将MediaStore.EXTRA_OUTPUT即"output"关联一个Uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(targetFilePath)));
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        mCurCropPhotoPath = targetFilePath;
        mCropPathList.add(targetFilePath);
        LogUtils.d(TAG, "startCropPhoto filePath:" + filePath);
        mActivity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    protected void selectUploadObjects() {
        if (mFileType == FileUploadComponent.TYPE_IMAGE) {
            selectImages();
        } else {
            selectFiles();
        }
    }

    /**
     * 打开相册或者照相机选择图片，最多mMaxFileCount张
     */
    private void selectImages() {
        int validCount = getValidImageCount();
        if (validCount >= mMaxFileCount) {
            Toast.makeText(mActivity, getResources().getString(R.string.base_upload_file_count_exceeding_msg,
                    mMaxFileCount), Toast.LENGTH_SHORT).show();
            return;
        }
        int allowCount = (mMaxFileCount - validCount > MAX_PER_UPLOAD_FILE_COUNT ?
                MAX_PER_UPLOAD_FILE_COUNT : mMaxFileCount - validCount);
        LogUtils.d(TAG, "selectImages mEnableCrop:" + mEnableCrop +
                ", allowCount:" + allowCount);
        ImageSelector.create()
                .count(mEnableCrop ? 1 : allowCount)
                .start(mActivity, REQUEST_CODE_SELECT_IMAGE);
    }

    /**
     * 打开文件管理选择文件，最多mMaxFileCount张
     */
    private void selectFiles() {
        int validCount = getValidImageCount();
        if (validCount >= mMaxFileCount) {
            Toast.makeText(mActivity, getResources().getString(R.string.base_upload_file_count_exceeding_msg,
                    mMaxFileCount), Toast.LENGTH_SHORT).show();
            return;
        }
        int allowCount = (mMaxFileCount - validCount > MAX_PER_UPLOAD_FILE_COUNT ?
                MAX_PER_UPLOAD_FILE_COUNT : mMaxFileCount - validCount);
        LogUtils.d(TAG, "selectFiles allowCount:" + allowCount);
//        ImageSelector.create()
//                .count(mEnableCrop ? 1 : allowCount)
//                .start(mActivity, REQUEST_CODE_SELECT_IMAGE);
    }

    protected void displayUploadObject(ArrayList<String> displayList, int position) {
        if (mFileType == FileUploadComponent.TYPE_IMAGE) {
            displayBigImages(displayList, position);
        } else {
            displayFiles(displayList, position);
        }
    }

    private void displayBigImages(ArrayList<String> displayList, int position) {
        ImageViewer.create()
                .origin(displayList)
                .position(position)
                .start(mActivity);
    }

    private void displayFiles(ArrayList<String> displayList, int position) {
//        ImageViewer.create()
//                .origin(displayList)
//                .position(position)
//                .start(mActivity);
    }

    @Override
    public void onDetachedFromWindow() {
        if (mHandlerThread != null) {
            mFileUploadComponent.cancelAll();
            mThreadHandler.removeCallbacksAndMessages(null);
            mThreadHandler = null;
            mHandlerThread.quit();
            mThreadHandler = null;
        }
        if (mMainHandler != null) {
            mMainHandler.removeCallbacksAndMessages(null);
            mMainHandler = null;
        }
        if (mCropPathList.size() > 0) {
            for (String path : mCropPathList) {
                FileUtils.deleteFile(path);
            }
        }
        super.onDetachedFromWindow();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> newSelectList = new ArrayList<>();
                newSelectList.add(mCurCropPhotoPath);
                LogUtils.d(TAG, "onActivityResult REQUEST_CODE_CROP" +
                        " mCurCropPhotoPath:" + mCurCropPhotoPath);
                uploadFileOneByOne(newSelectList);
            }
        } else if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> newSelectList = data.getStringArrayListExtra(
                        ImageSelector.INTENT_SELECTED_IMAGE_LIST);
                if (newSelectList.size() < 1) {
                    return;
                }
                LogUtils.d(TAG, "onActivityResult REQUEST_CODE_SELECT_IMAGE" +
                        " mEnableCrop:" + mEnableCrop + ", mTogetherUploadMode:" + mTogetherUploadMode +
                        ", newSelectList.size():" + newSelectList.size());
                if (mEnableCrop) {
                    startCropImage(newSelectList.get(0));
                } else {
                    if (mTogetherUploadMode) {
                        uploadFileList(newSelectList);
                    } else {
                        uploadFileOneByOne(newSelectList);
                    }
                }
            }
        }
    }

    private void uploadFileOneByOne(final List<String> list) {
        if (!mIsInit) {
            throw new IllegalStateException("You should call init() method before use this view");
        }
        if (list == null || list.size() < 1 || mOneByOneUploadAdapter == null) {
            return;
        }
        LogUtils.d(TAG, "uploadImageOneByOne list.size():" + list.size());
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("UploadFileRecyclerView");
            mHandlerThread.start();
            mThreadHandler = new Handler(mHandlerThread.getLooper());
        }
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        final List<FileUploadBean> uploadBeanList = new ArrayList<>();
        FileUploadBean fileUploadBean = null;
        for (int i = 0; i < list.size(); i++) {
            fileUploadBean = new FileUploadBean();
            String filePath = list.get(i);
            fileUploadBean.setFileKey(mOneByOneUploadAdapter.getFileKey(fileUploadBean));
            fileUploadBean.setFileType(mFileType);
            fileUploadBean.setLocalFilePath(filePath);
            fileUploadBean.setFileName(filePath.substring(filePath.lastIndexOf(File.separator) + 1));
            fileUploadBean.setParams(mOneByOneUploadAdapter.getUploadParam(fileUploadBean));
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setRequestUrl(mUploadFileUrl);
            fileUploadBean.setUploadState(FileUploadState.UPLOAD_STATE_PREPARING);
            uploadBeanList.add(fileUploadBean);
        }
        if (uploadBeanList.size() < 1) {
            return;
        }
        onFileUploadPrepare(uploadBeanList);
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAttachedToWindow()) {
                    return;
                }
                mFileUploadComponent = new FileUploadComponent(mActivity, mMaxImageSize,
                        mCompressImageWidth, mCompressImageHeight);
                mFileUploadComponent.startOneByOne(uploadBeanList,
                        new FileUploadComponent.OneByOneUploadCallback() {

                            @Override
                            public void onStart(final FileUploadBean fileBean) {
                            }

                            @Override
                            public void onProgress(final FileUploadBean fileBean, final int progress) {
                                if (mMainHandler == null) {
                                    return;
                                }
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fileBean.setUploadProgress(progress);
                                        onFileUploadProgress(fileBean);
                                    }
                                });
                            }

                            @Override
                            public void onCancel(FileUploadBean uploadBean) {

                            }

                            @Override
                            public void onFailed(final FileUploadBean fileBean, String message) {
                                if (mMainHandler == null) {
                                    return;
                                }
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        fileBean.setUploadState(FileUploadState.UPLOAD_STATE_FAIL);
                                        onFileUploadFail(fileBean);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(final FileUploadBean fileBean, final JSONObject response) {
                                if (mMainHandler == null) {
                                    return;
                                }
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        String url = mOneByOneUploadAdapter
                                                .getRemoteUrlFromResponse(fileBean, response);
                                        if (TextUtils.isEmpty(url)) {
                                            onFailed(fileBean, "");
                                            return;
                                        }
                                        fileBean.setRemoteFilePath(url);
                                        fileBean.setUploadState(FileUploadState.UPLOAD_STATE_SUCCESS);
                                        onFileUploadSuccess(fileBean);
                                    }
                                });
                            }
                        });
            }
        });
    }

    private void uploadFileList(final List<String> list) {
        if (!mIsInit) {
            throw new IllegalStateException("You should call init() method before use this view");
        }
        if (list == null || list.size() < 1 || mTogetherUploadAdapter == null) {
            return;
        }
        LogUtils.d(TAG, "uploadImageList list.size():" + list.size());
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("ImageUploadView");
            mHandlerThread.start();
            mThreadHandler = new Handler(mHandlerThread.getLooper());
        }
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
        final List<FileUploadBean> uploadBeanList = new ArrayList<>();
        FileUploadBean fileUploadBean = null;
        for (int i = 0; i < list.size(); i++) {
            fileUploadBean = new FileUploadBean();
            String filePath = list.get(i);
            fileUploadBean.setFileKey(mTogetherUploadAdapter.getFileKey(fileUploadBean));
            fileUploadBean.setFileType(mFileType);
            fileUploadBean.setLocalFilePath(filePath);
            fileUploadBean.setFileName(filePath.substring(filePath.lastIndexOf(File.separator) + 1));
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setRequestUrl(mUploadFileUrl);
            fileUploadBean.setUploadState(FileUploadState.UPLOAD_STATE_PREPARING);
            uploadBeanList.add(fileUploadBean);
        }
        if (uploadBeanList.size() < 1) {
            return;
        }
        onFileUploadPrepare(uploadBeanList);
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAttachedToWindow()) {
                    return;
                }
                mFileUploadComponent = new FileUploadComponent(mActivity, mMaxImageSize,
                        mCompressImageWidth, mCompressImageHeight);
                mFileUploadComponent.startTogether(mUploadFileUrl,
                        mTogetherUploadAdapter.getUploadParam(uploadBeanList),
                        mTogetherUploadAdapter.getFilesKey(uploadBeanList),
                        uploadBeanList, new FileUploadComponent.SimpleTogetherUploadListCallback() {
                            @Override
                            public void onProgress(final List<FileUploadBean> fileBeanList, final int progress) {
                                if (mMainHandler == null) {
                                    return;
                                }
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (FileUploadBean fileBean : fileBeanList) {
                                            fileBean.setUploadProgress(progress);
                                        }
                                        onFileUploadProgress(fileBeanList);
                                    }
                                });
                            }

                            @Override
                            public void onFailed(final List<FileUploadBean> fileBeanList, String message) {
                                if (mMainHandler == null) {
                                    return;
                                }
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        for (FileUploadBean fileBean : fileBeanList) {
                                            fileBean.setUploadState(FileUploadState.UPLOAD_STATE_FAIL);
                                        }
                                        onFileUploadFail(fileBeanList);
                                    }
                                });
                            }

                            @Override
                            public void onSuccess(final List<FileUploadBean> fileBeanList, final JSONObject response) {
                                if (mMainHandler == null) {
                                    return;
                                }
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        List<String> urlList = mTogetherUploadAdapter
                                                .getRemoteUrlListFromResponse(fileBeanList, response);
                                        if (urlList == null || urlList.size() < 1 ||
                                                urlList.size() != fileBeanList.size()) {
                                            onFailed(fileBeanList, "");
                                            return;
                                        }
                                        for (int i = 0; i < fileBeanList.size(); i++) {
                                            FileUploadBean fileBean = fileBeanList.get(i);
                                            fileBean.setRemoteFilePath(urlList.get(i));
                                            fileBean.setUploadState(FileUploadState.UPLOAD_STATE_SUCCESS);
                                        }
                                        onFileUploadSuccess(fileBeanList);
                                    }
                                });
                            }
                        });
            }
        });
    }

    public abstract int getValidImageCount();

    public abstract void onFileUploadPrepare(List<FileUploadBean> uploadBeanList);

    public abstract void onFileUploadProgress(FileUploadBean uploadBean);

    public abstract void onFileUploadFail(FileUploadBean uploadBean);

    public abstract void onFileUploadSuccess(FileUploadBean uploadBean);

    public abstract void onFileUploadProgress(List<FileUploadBean> uploadBeanList);

    public abstract void onFileUploadFail(List<FileUploadBean> uploadBeanList);

    public abstract void onFileUploadSuccess(List<FileUploadBean> uploadBeanList);


    public interface OneByOneUploadAdapter {
        String getFileKey(FileUploadBean fileUploadBean);

        Map<String, String> getUploadParam(FileUploadBean fileUploadBean);

        String getRemoteUrlFromResponse(FileUploadBean fileUploadBean, JSONObject response);
    }

    public interface TogetherUploadAdapter {
        String getFileKey(FileUploadBean fileUploadBean);

        String getFilesKey(List<FileUploadBean> fileUploadBeanList);

        Map<String, String> getUploadParam(List<FileUploadBean> fileUploadBeanList);

        List<String> getRemoteUrlListFromResponse(List<FileUploadBean> fileUploadBeanList, JSONObject response);
    }
}