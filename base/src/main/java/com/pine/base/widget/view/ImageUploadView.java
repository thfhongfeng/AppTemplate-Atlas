package com.pine.base.widget.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
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
import com.pine.tool.util.LogUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by tanghongfeng on 2018/11/1
 */

public class ImageUploadView extends RecyclerView {
    private static final String TAG = LogUtils.makeLogTag(ImageUploadView.class);
    private final int REQUEST_CODE_SELECT_IMAGE = 10000 + new Random().nextInt(10000);
    private Context mContext;
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
    private HashMap<String, String> mUploadParams;
    // 上传地址
    private String mUploadImageUrl;
    private FileUploadComponent mFileUploadComponent;
    private boolean mIsInit;
    private UploadResponseAdapter mUploadResultAdapter;

    public ImageUploadView(Context context) {
        super(context);
        mContext = context;
        setup();
    }

    public ImageUploadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        int defaultColumnSize = getResources().getDisplayMetrics().widthPixels / getResources().getDimensionPixelOffset(R.dimen.dp_106);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseImageUploadView);
        mMaxImageCount = typedArray.getInt(R.styleable.BaseImageUploadView_baseMaxImageCount, 10);
        mColumnSize = typedArray.getInt(R.styleable.BaseImageUploadView_baseColumnSize, defaultColumnSize);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseImageUploadView_baseMaxImageSize, 1024 * 1024);
        setup();
    }

    public ImageUploadView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
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

    public void init(@NonNull String uploadUrl, HashMap<String, String> params, boolean editable,
                     @NonNull UploadResponseAdapter adapter) {
        init("", uploadUrl, params, editable, adapter);
    }

    public void init(String attachId, @NonNull String uploadUrl, HashMap<String, String> params,
                     boolean editable, @NonNull UploadResponseAdapter adapter) {
        mAttachId = attachId;
        mUploadImageUrl = uploadUrl;
        mUploadParams = params;
        mUploadResultAdapter = adapter;
        mUploadImageAdapter = new UploadImageAdapter(
                UploadImageAdapter.UPLOAD_IMAGE_VIEW_HOLDER, editable, mMaxImageCount);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp_10)));
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, mColumnSize);
        setLayoutManager(layoutManager);
        setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setData(null);
        mUploadImageAdapter.notifyDataSetChanged();
        mIsInit = true;
    }

    public void setMaxImageCount(int maxImageCount) {
        mMaxImageCount = maxImageCount;
    }

    public void setMaxImageSize(int maxImageSize) {
        mMaxImageSize = maxImageSize;
    }

    /**
     * 打开相册或者照相机选择图片，最多mMaxImageCount张
     */
    private void selectImages() {
        int existCount = getExistImageCount();
        if (existCount >= mMaxImageCount) {
            Toast.makeText(mContext, getResources().getString(R.string.base_upload_image_count_exceeding_msg,
                    mMaxImageCount), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mContext instanceof Activity) {
            ImageSelector.create()
                    .count(mMaxImageCount - existCount)
                    .start((Activity) mContext, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    private void showImages(int position) {
        ImageViewer.create()
                .origin(getAllUploadImageList())
                .position(position)
                .start((Activity) mContext);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                List<String> selectedPicture = data.getStringArrayListExtra(
                        ImageSelector.INTENT_SELECTED_IMAGE_LIST);
                if (TextUtils.isEmpty(mUploadImageUrl)) {
                    return;
                }
                uploadImageOneByOne(selectedPicture, false);
            }
        }
    }

    private void uploadImageOneByOne(final List<String> list, final boolean hasCropped) {
        if (list == null || !mIsInit) {
            throw new IllegalStateException("You should call init() method before use this view");
        }
        if (mHandlerThread == null) {
            mHandlerThread = new HandlerThread("MallAddPhotoFragment");
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
            fileUploadBean.setFileKey("file");
            fileUploadBean.setFileType(FileUploadComponent.TYPE_IMAGE);
            fileUploadBean.setLocalFilePath(filePath);
            fileUploadBean.setFileName(filePath.substring(filePath.lastIndexOf(File.separator) + 1));
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setRequestUrl(mUploadImageUrl);
            fileUploadBean.setParams(mUploadParams);
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
                mFileUploadComponent = new FileUploadComponent(mContext, mMaxImageSize);
                mFileUploadComponent.start(uploadBeanList, new FileUploadComponent.FileUploadCallback() {

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
                    public void onFinish(FileUploadBean fileBean) {

                    }

                    @Override
                    public void onCancel(FileUploadBean fileBean) {

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
                    public void onError(FileUploadBean fileBean, String message) {

                    }

                    @Override
                    public void onSuccess(final FileUploadBean fileBean, final JSONObject response) {
                        if (mMainHandler == null) {
                            return;
                        }
                        mMainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                String url = mUploadResultAdapter.getRemoteUrl(response);
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

    public int getExistImageCount() {
        return mUploadImageAdapter.getExistImageCount();
    }

    public ArrayList<String> getAllUploadImageList() {
        return mUploadImageAdapter.getAllUploadImageList();
    }

    public String getAllUploadImageJoinString(String joinStr) {
        return mUploadImageAdapter.getAllUploadImageJoinString(joinStr);
    }

    public List<String> getNewUploadImageList() {
        return mUploadImageAdapter.getNewUploadImageList();
    }

    public String getNewUploadImageJoinString(String joinStr) {
        return mUploadImageAdapter.getNewUploadImageJoinString(joinStr);
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
        super.onDetachedFromWindow();
    }

    public interface UploadResponseAdapter {
        String getRemoteUrl(JSONObject response);
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

        public ArrayList<String> getLocalImageList() {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && !TextUtils.isEmpty(bean.getLocalFilePath())) {
                    retList.add(bean.getLocalFilePath());
                }
            }
            return retList;
        }

        public int getExistImageCount() {
            ArrayList<String> retList = new ArrayList<>();
            int count = 0;
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && (bean.getUploadState() == FileUploadBean.UPLOAD_STATE_SUCCESS ||
                        bean.getUploadState() == FileUploadBean.UPLOAD_STATE_UPLOADING)) {
                    count++;
                }
            }
            return count;
        }

        public ArrayList<String> getAllUploadImageList() {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && (bean.getUploadState() == FileUploadBean.UPLOAD_STATE_SUCCESS) &&
                        !TextUtils.isEmpty(bean.getRemoteFilePath())) {
                    retList.add(bean.getRemoteFilePath());
                }
            }
            return retList;
        }

        public String getAllUploadImageJoinString(String joinStr) {
            List<String> list = getAllUploadImageList();
            if (list.size() < 1) {
                return "";
            }
            String reStr = list.get(0);
            for (String str : list) {
                reStr += joinStr + str;
            }
            return reStr;
        }

        public ArrayList<String> getNewUploadImageList() {
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

        public String getNewUploadImageJoinString(String joinStr) {
            List<String> list = getNewUploadImageList();
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
                            showImages(position);
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
                    int successSize = getAllUploadImageList().size();
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
