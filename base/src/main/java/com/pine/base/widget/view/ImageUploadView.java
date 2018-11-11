package com.pine.base.widget.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.component.image_selector.ImageSelector;
import com.pine.base.component.image_selector.ImageViewer;
import com.pine.base.component.uploader.FileUploadComponent;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseNoPaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemPropertyEntity;
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
 * Created by tanghongfeng on 2018/11/1
 */

public class ImageUploadView extends RecyclerView {
    private static final String TAG = LogUtils.makeLogTag(ImageUploadView.class);
    private final int REQUEST_CODE_CROP = 10000 + new Random().nextInt(10000);
    private final int REQUEST_CODE_SELECT_IMAGE = REQUEST_CODE_CROP + 1;
    private Activity mActivity;
    private boolean mNeedCrop;
    // 最大可上传image数量
    private int mMaxImageCount = 10;
    // RecyclerView列数（一行可容纳image数量）
    private int mColumnSize = 5;
    // 每张image允许的最大字节数
    private long mMaxImageSize = 1024 * 1024;
    // 如果需要压缩，压缩后的image的宽度
    private int mCompressImageWidth = 1440;
    // 如果需要压缩，压缩后的image的高度
    private int mCompressImageHeight = 2550;
    private HandlerThread mHandlerThread;
    private Handler mThreadHandler;
    private Handler mMainHandler;
    private UploadImageAdapter mUploadImageAdapter;
    private String mAttachId;
    // 上传地址
    private String mUploadImageUrl;
    private FileUploadComponent mFileUploadComponent;
    private boolean mIsInit;
    private OneByOneUploadAdapter mOneByOneUploadAdapter;
    private TogetherUploadAdapter mTogetherUploadAdapter;
    private boolean mTogetherUploadMode;
    private String mCurCropPhotoPath;
    private List<String> mCropPathList = new ArrayList<>();
    private int mCropWidth = 360;
    private int mCropHeight = 360;

    public ImageUploadView(Context context) {
        super(context);
        setup();
    }

    public ImageUploadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int defaultColumnSize = getResources().getDisplayMetrics().widthPixels / getResources().getDimensionPixelOffset(R.dimen.dp_106);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseImageUploadView);
        mMaxImageCount = typedArray.getInt(R.styleable.BaseImageUploadView_baseMaxImageCount, 10);
        mColumnSize = typedArray.getInt(R.styleable.BaseImageUploadView_baseColumnSize, defaultColumnSize);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseImageUploadView_baseMaxImageSize, 1024 * 1024);
        setup();
    }

    public ImageUploadView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int defaultColumnSize = getResources().getDisplayMetrics().widthPixels / getResources().getDimensionPixelOffset(R.dimen.dp_106);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseImageUploadView);
        mMaxImageCount = typedArray.getInt(R.styleable.BaseImageUploadView_baseMaxImageCount, 10);
        mColumnSize = typedArray.getInt(R.styleable.BaseImageUploadView_baseColumnSize, defaultColumnSize);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseImageUploadView_baseMaxImageSize, 1024 * 1024);
        setup();
    }

    private void setup() {
        if (mMainHandler == null) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
    }

    public void init(Activity activity, @NonNull String uploadUrl, boolean editable,
                     @NonNull OneByOneUploadAdapter adapter) {
        init(activity, "", uploadUrl, editable, adapter);
    }

    public void init(Activity activity, String attachId, @NonNull String uploadUrl,
                     boolean editable, @NonNull OneByOneUploadAdapter adapter) {
        mTogetherUploadMode = false;
        mActivity = activity;
        mAttachId = attachId;
        mUploadImageUrl = uploadUrl;
        if (TextUtils.isEmpty(mUploadImageUrl)) {
            throw new IllegalStateException("Upload url should not be empty");
        }
        mOneByOneUploadAdapter = adapter;
        mUploadImageAdapter = new UploadImageAdapter(
                UploadImageAdapter.UPLOAD_IMAGE_VIEW_HOLDER, editable, mMaxImageCount);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp_10)));
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, mColumnSize);
        setLayoutManager(layoutManager);
        setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setData(null);
        mUploadImageAdapter.notifyDataSetChanged();
        mIsInit = true;
    }

    public void init(Activity activity, @NonNull String uploadUrl, boolean editable,
                     @NonNull TogetherUploadAdapter adapter) {
        init(activity, "", uploadUrl, editable, adapter);
    }

    public void init(Activity activity, String attachId, @NonNull String uploadUrl,
                     boolean editable, @NonNull TogetherUploadAdapter adapter) {
        mTogetherUploadMode = true;
        mActivity = activity;
        mAttachId = attachId;
        mUploadImageUrl = uploadUrl;
        if (TextUtils.isEmpty(mUploadImageUrl)) {
            throw new IllegalStateException("Upload url should not be empty");
        }
        mTogetherUploadAdapter = adapter;
        mUploadImageAdapter = new UploadImageAdapter(
                UploadImageAdapter.UPLOAD_IMAGE_VIEW_HOLDER, editable, mMaxImageCount);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp_10)));
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, mColumnSize);
        setLayoutManager(layoutManager);
        setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setData(null);
        mUploadImageAdapter.notifyDataSetChanged();
        mIsInit = true;
    }

    public void setCropEnable() {
        mNeedCrop = true;
    }

    public void setCropEnable(int cropWidth, int cropHeight) {
        mNeedCrop = true;
        mCropWidth = cropWidth;
        mCropHeight = cropHeight;
    }

    public void setMaxImageCount(int maxImageCount) {
        mMaxImageCount = maxImageCount;
    }

    public void setMaxImageSize(int maxImageSize) {
        mMaxImageSize = maxImageSize;
    }

    /**
     * 打开系统裁剪功能
     */
    private void startCropPhoto(String filePath) {
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
        mActivity.startActivityForResult(intent, REQUEST_CODE_CROP);
    }

    /**
     * 打开相册或者照相机选择图片，最多mMaxImageCount张
     */
    private void selectImages() {
        int validCount = getValidImageList().size();
        if (validCount >= mMaxImageCount) {
            Toast.makeText(mActivity, getResources().getString(R.string.base_upload_image_count_exceeding_msg,
                    mMaxImageCount), Toast.LENGTH_SHORT).show();
            return;
        }
        ImageSelector.create()
                .count(mNeedCrop ? 1 : mMaxImageCount - validCount)
                .start(mActivity, REQUEST_CODE_SELECT_IMAGE);
    }

    private void showImages(int position) {
        ImageViewer.create()
                .origin(getImageShowList())
                .position(position)
                .start(mActivity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> newSelectList = new ArrayList<>();
                newSelectList.add(mCurCropPhotoPath);
                uploadImageOneByOne(newSelectList);
            }
        } else if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> newSelectList = data.getStringArrayListExtra(
                        ImageSelector.INTENT_SELECTED_IMAGE_LIST);
                if (newSelectList.size() < 1) {
                    return;
                }
                if (mNeedCrop) {
                    startCropPhoto(newSelectList.get(0));
                } else {
                    if (mTogetherUploadMode) {
                        uploadImageList(newSelectList);
                    } else {
                        uploadImageOneByOne(newSelectList);
                    }
                }
            }
        }
    }

    private void uploadImageOneByOne(final List<String> list) {
        if (!mIsInit) {
            throw new IllegalStateException("You should call init() method before use this view");
        }
        if (list == null || list.size() < 1 || mOneByOneUploadAdapter == null) {
            return;
        }
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
            fileUploadBean.setFileKey(mOneByOneUploadAdapter.getFileKey(fileUploadBean));
            fileUploadBean.setFileType(FileUploadComponent.TYPE_IMAGE);
            fileUploadBean.setLocalFilePath(filePath);
            fileUploadBean.setFileName(filePath.substring(filePath.lastIndexOf(File.separator) + 1));
            fileUploadBean.setParams(mOneByOneUploadAdapter.getUploadParam(fileUploadBean));
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setRequestUrl(mUploadImageUrl);
            fileUploadBean.setOriginalBean(list.get(i));
            fileUploadBean.setUploadState(FileUploadBean.UPLOAD_STATE_UPLOADING);
            uploadBeanList.add(fileUploadBean);
        }
        if (uploadBeanList.size() < 1) {
            return;
        }
        int startIndex = mUploadImageAdapter.getAdapterData().size();
        mUploadImageAdapter.addData(uploadBeanList);
        mUploadImageAdapter.notifyItemRangeChanged(startIndex, mUploadImageAdapter.getAdapterData().size());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAttachedToWindow()) {
                    return;
                }
                mFileUploadComponent = new FileUploadComponent(mActivity, mMaxImageSize);
                mFileUploadComponent.startOneByOne(uploadBeanList, new FileUploadComponent.FileUploadCallback() {

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
                                mUploadImageAdapter.notifyItemChanged(fileBean.getOrderIndex());
                            }
                        });
                    }

                    @Override
                    public void onError(FileUploadBean fileBean, String message) {

                    }

                    @Override
                    public void onFailed(final FileUploadBean fileBean, String message) {
                        if (mMainHandler == null) {
                            return;
                        }
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                fileBean.setUploadState(FileUploadBean.UPLOAD_STATE_FAIL);
                                mUploadImageAdapter.notifyItemChanged(fileBean.getOrderIndex());
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
                                fileBean.setUploadState(FileUploadBean.UPLOAD_STATE_SUCCESS);
                                mUploadImageAdapter.notifyItemChanged(fileBean.getOrderIndex());
                                mUploadImageAdapter.notifyItemChanged(0);
                            }
                        });
                    }
                });
            }
        });
    }

    private void uploadImageList(final List<String> list) {
        if (!mIsInit) {
            throw new IllegalStateException("You should call init() method before use this view");
        }
        if (list == null || list.size() < 1 || mTogetherUploadAdapter == null) {
            return;
        }
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
            fileUploadBean.setFileType(FileUploadComponent.TYPE_IMAGE);
            fileUploadBean.setLocalFilePath(filePath);
            fileUploadBean.setFileName(filePath.substring(filePath.lastIndexOf(File.separator) + 1));
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setRequestUrl(mUploadImageUrl);
            fileUploadBean.setOriginalBean(list.get(i));
            fileUploadBean.setUploadState(FileUploadBean.UPLOAD_STATE_UPLOADING);
            uploadBeanList.add(fileUploadBean);
        }
        if (uploadBeanList.size() < 1) {
            return;
        }
        int startIndex = mUploadImageAdapter.getAdapterData().size();
        mUploadImageAdapter.addData(uploadBeanList);
        mUploadImageAdapter.notifyItemRangeChanged(startIndex, mUploadImageAdapter.getAdapterData().size());
        mThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isAttachedToWindow()) {
                    return;
                }
                mFileUploadComponent = new FileUploadComponent(mActivity, mMaxImageSize);
                mFileUploadComponent.startTogether(mUploadImageUrl,
                        mTogetherUploadAdapter.getUploadParam(uploadBeanList), mTogetherUploadAdapter.getFilesKey(uploadBeanList),
                        uploadBeanList, new FileUploadComponent.FileUploadListCallback() {

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
                                        mUploadImageAdapter.notifyDataSetChanged();
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
                                            fileBean.setUploadState(FileUploadBean.UPLOAD_STATE_FAIL);
                                        }
                                        mUploadImageAdapter.notifyDataSetChanged();
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
                                            fileBean.setUploadState(FileUploadBean.UPLOAD_STATE_SUCCESS);
                                        }
                                        mUploadImageAdapter.notifyDataSetChanged();
                                        mUploadImageAdapter.notifyItemChanged(0);
                                    }
                                });
                            }
                        });
            }
        });
    }

    public void setRemoteImageList(List<String> remoteImageList) {
        if (remoteImageList == null && remoteImageList.size() < 1) {
            return;
        }
        List<FileUploadBean> uploadImageList = new ArrayList<>();
        FileUploadBean fileUploadBean = null;
        for (int i = 0; i < remoteImageList.size(); i++) {
            fileUploadBean = new FileUploadBean();
            fileUploadBean.setRemoteFilePath(remoteImageList.get(i));
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setUploadState(FileUploadBean.UPLOAD_STATE_SUCCESS);
            uploadImageList.add(fileUploadBean);
        }
        if (uploadImageList.size() < 1) {
            return;
        }
        mUploadImageAdapter.setData(uploadImageList);
        mUploadImageAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> getImageShowList() {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_SUCCESS);
        states.add(FileUploadBean.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageList(states);
    }

    private ArrayList<String> getValidImageList() {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_SUCCESS);
        states.add(FileUploadBean.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageList(states);
    }

    private ArrayList<String> getValidImageLocalList() {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_SUCCESS);
        states.add(FileUploadBean.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageLocalList(states);
    }

    private ArrayList<String> getUploadedImageLocalList() {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_SUCCESS);
        return mUploadImageAdapter.getImageLocalList(states);
    }

    private ArrayList<String> getUploadingImageLocalList() {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageLocalList(states);
    }

    private ArrayList<String> getUploadedImageRemoteList() {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_SUCCESS);
        return mUploadImageAdapter.getImageRemoteList(states);
    }

    private String getUploadedImageRemoteString(String joinStr) {
        List<Integer> states = new ArrayList<>();
        states.add(FileUploadBean.UPLOAD_STATE_SUCCESS);
        return mUploadImageAdapter.getImageRemoteString(states, joinStr);
    }

    public List<String> getNewUploadImageRemoteList() {
        return mUploadImageAdapter.getNewUploadImageRemoteList();
    }

    public String getNewUploadImageRemoteString(String joinStr) {
        return mUploadImageAdapter.getNewUploadImageRemoteString(joinStr);
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

    public class UploadImageAdapter extends BaseNoPaginationListAdapter<FileUploadBean> {
        public static final int UPLOAD_IMAGE_VIEW_HOLDER = 1;
        private boolean mEditable;
        private int mMaxImageCount;

        public UploadImageAdapter(int defaultItemViewType, boolean editable, int maxImageCount) {
            super(defaultItemViewType);
            mEditable = editable;
            mMaxImageCount = maxImageCount;
        }

        @Override
        protected List<BaseListAdapterItemEntity<FileUploadBean>> parseData(List<FileUploadBean> data,
                                                                            boolean reset) {
            List<BaseListAdapterItemEntity<FileUploadBean>> adapterData = new ArrayList<>();
            BaseListAdapterItemEntity adapterEntity;
            if (mEditable && reset) {
                adapterEntity = new BaseListAdapterItemEntity();
                adapterEntity.getPropertyEntity().setItemViewType(getDefaultItemViewType());
                adapterData.add(adapterEntity);
            }
            if (data != null) {
                for (int i = 0; i < data.size(); i++) {
                    adapterEntity = new BaseListAdapterItemEntity();
                    adapterEntity.setData(data.get(i));
                    adapterEntity.getPropertyEntity().setItemViewType(getDefaultItemViewType());
                    adapterData.add(adapterEntity);
                }
            }
            return adapterData;
        }

        public ArrayList<String> getImageList(List<Integer> states) {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && states.contains(bean.getUploadState())) {
                    if (!TextUtils.isEmpty(bean.getLocalFilePath())) {
                        retList.add(bean.getLocalFilePath());
                    } else if (!TextUtils.isEmpty(bean.getRemoteFilePath())) {
                        retList.add(bean.getRemoteFilePath());
                    }
                }
            }
            return retList;
        }

        public ArrayList<String> getImageLocalList(List<Integer> states) {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && !TextUtils.isEmpty(bean.getLocalFilePath()) &&
                        states.contains(bean.getUploadState())) {
                    retList.add(bean.getLocalFilePath());
                }
            }
            return retList;
        }

        public String getImageLocalString(List<Integer> states, String joinStr) {
            List<String> list = getImageLocalList(states);
            return listJoinToString(list, joinStr);
        }

        public ArrayList<String> getImageRemoteList(List<Integer> states) {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && !TextUtils.isEmpty(bean.getRemoteFilePath()) &&
                        states.contains(bean.getUploadState())) {
                    retList.add(bean.getRemoteFilePath());
                }
            }
            return retList;
        }

        public String getImageRemoteString(List<Integer> states, String joinStr) {
            List<String> list = getImageRemoteList(states);
            return listJoinToString(list, joinStr);
        }

        public ArrayList<String> getNewUploadImageRemoteList() {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && bean.getUploadState() == FileUploadBean.UPLOAD_STATE_SUCCESS &&
                        !TextUtils.isEmpty(bean.getRemoteFilePath()) && !TextUtils.isEmpty(bean.getLocalFilePath())) {
                    retList.add(bean.getRemoteFilePath());
                }
            }
            return retList;
        }

        public String getNewUploadImageRemoteString(String joinStr) {
            List<String> list = getNewUploadImageRemoteList();
            return listJoinToString(list, joinStr);
        }

        private String listJoinToString(List<String> list, String joinStr) {
            if (list.size() < 1) {
                return "";
            }
            String reStr = list.get(0);
            for (String str : list) {
                reStr += joinStr + str;
            }
            return reStr;
        }

        @Override
        public BaseListViewHolder getViewHolder(ViewGroup parent, int viewType) {
            BaseListViewHolder viewHolder = null;
            switch (viewType) {
                case UPLOAD_IMAGE_VIEW_HOLDER:
                    viewHolder = new UploadImageViewHolder(parent.getContext(), LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.base_item_upload_image, parent, false));
                    break;
            }
            return viewHolder;
        }

        public class UploadImageViewHolder extends BaseListViewHolder<FileUploadBean> {
            private Context context;
            private RelativeLayout state_rl;
            private ImageView show_iv, delete_iv, loading_iv;
            private TextView num_max_tv, fail_tv, loading_tv;


            public UploadImageViewHolder(Context context, View itemView) {
                super(itemView);
                this.context = context;
                state_rl = itemView.findViewById(R.id.state_rl);
                show_iv = itemView.findViewById(R.id.show_iv);
                delete_iv = itemView.findViewById(R.id.delete_iv);
                loading_iv = itemView.findViewById(R.id.loading_iv);
                loading_tv = itemView.findViewById(R.id.loading_tv);
                num_max_tv = itemView.findViewById(R.id.num_max_tv);
                fail_tv = itemView.findViewById(R.id.fail_tv);
            }

            @Override
            public void updateData(final FileUploadBean imageBean,
                                   BaseListAdapterItemPropertyEntity propertyEntity, final int position) {
                if (position < mData.size() && position > 0) {
                    imageBean.setOrderIndex(position);
                    // 代表+号之前的需要正常显示图片
                    String imageUrl = imageBean.getLocalFilePath();
                    if (!TextUtils.isEmpty(imageUrl)) {
                        imageUrl = "file://" + imageUrl;
                    } else if (!TextUtils.isEmpty(imageBean.getRemoteFilePath())) {
                        imageUrl = imageBean.getRemoteFilePath();
                    }
                    ImageLoaderManager.getInstance().loadImage(context, imageUrl, show_iv);
                    loading_iv.setVisibility(View.GONE);
                    loading_tv.setVisibility(GONE);
                    fail_tv.setVisibility(View.GONE);
                    switch (imageBean.getUploadState()) {
                        case FileUploadBean.UPLOAD_STATE_UPLOADING:
                            state_rl.setVisibility(View.VISIBLE);
                            loading_iv.setVisibility(View.VISIBLE);
                            loading_tv.setVisibility(VISIBLE);
                            loading_tv.setText(imageBean.getUploadProgress() + "%");
                            break;
                        case FileUploadBean.UPLOAD_STATE_FAIL:
                            state_rl.setVisibility(View.VISIBLE);
                            fail_tv.setVisibility(View.VISIBLE);
                            break;
                        case FileUploadBean.UPLOAD_STATE_SUCCESS:
                            state_rl.setVisibility(View.GONE);
                            break;
                        default:
                            state_rl.setVisibility(View.GONE);
                            break;
                    }
                    num_max_tv.setText("");
                    num_max_tv.setVisibility(View.GONE);
                    show_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showImages(mEditable ? position - 1 : position);
                        }
                    });
                    if (mEditable) {
                        delete_iv.setVisibility(View.VISIBLE);
                        delete_iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mData.remove(position);
                                mFileUploadComponent.cancel(imageBean);
                                notifyDataSetChanged();
                            }
                        });
                    } else {
                        delete_iv.setVisibility(View.GONE);
                    }
                } else {
                    show_iv.setImageResource(R.mipmap.base_iv_add_upload_image);// 第一个显示加号图片
                    int successSize = getUploadedImageRemoteList().size();
                    num_max_tv.setText(successSize + "/" + mMaxImageCount);
                    num_max_tv.setVisibility(VISIBLE);
                    show_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectImages();
                        }
                    });
                    state_rl.setVisibility(View.GONE);
                    delete_iv.setVisibility(View.GONE);
                }
            }
        }
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) % mColumnSize == 0) {
                outRect.left = space;
            }
            if (parent.getChildAdapterPosition(view) < mColumnSize) {
                outRect.top = space;
            }
            outRect.right = space;
            outRect.bottom = space;
        }
    }
}
