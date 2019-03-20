package com.pine.base.component.uploader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
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

import com.pine.base.R;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.component.uploader.FileUploadComponent;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.component.uploader.bean.FileUploadState;
import com.pine.base.list.BaseListViewHolder;
import com.pine.base.list.adapter.BaseNoPaginationListAdapter;
import com.pine.base.list.bean.BaseListAdapterItemEntity;
import com.pine.base.list.bean.BaseListAdapterItemProperty;
import com.pine.base.ui.BaseActivity;
import com.pine.tool.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tanghongfeng on 2018/11/1
 */

public class ImageUploadView extends UploadFileRecyclerView {
    private final String TAG = LogUtils.makeLogTag(this.getClass());
    private UploadImageAdapter mUploadImageAdapter;
    // RecyclerView列数（一行可容纳image数量）
    private int mColumnSize = 5;

    public ImageUploadView(Context context) {
        super(context);
    }

    public ImageUploadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int defaultColumnSize = getResources().getDisplayMetrics().widthPixels / getResources().getDimensionPixelOffset(R.dimen.dp_106);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseFileUploadView);
        mMaxFileCount = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileCount, 10);
        mColumnSize = typedArray.getInt(R.styleable.BaseFileUploadView_baseColumnSize, defaultColumnSize);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileSize, 1024 * 1024);
    }

    public ImageUploadView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        int defaultColumnSize = getResources().getDisplayMetrics().widthPixels / getResources().getDimensionPixelOffset(R.dimen.dp_106);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseFileUploadView);
        mMaxFileCount = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileCount, 10);
        mColumnSize = typedArray.getInt(R.styleable.BaseFileUploadView_baseColumnSize, defaultColumnSize);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileSize, 1024 * 1024);
    }

    public void init(@NonNull BaseActivity activity) {
        init(activity, false);
    }

    public void init(@NonNull BaseActivity activity, boolean canDelete) {
        initUpload(activity, FileUploadComponent.TYPE_IMAGE);
        mUploadImageAdapter = new UploadImageAdapter(
                UploadImageAdapter.UPLOAD_IMAGE_VIEW_HOLDER, false, canDelete, mMaxFileCount);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp_10)));
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, mColumnSize);
        setLayoutManager(layoutManager);
        setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setData(null);
        notifyAdapterDataChanged();
    }


    public void init(@NonNull BaseActivity activity, @NonNull String uploadUrl,
                     boolean canDelete, @NonNull OneByOneUploadAdapter adapter, int requestCodeSelectImage) {
        initUpload(activity, uploadUrl, FileUploadComponent.TYPE_IMAGE, adapter, requestCodeSelectImage);
        mUploadImageAdapter = new UploadImageAdapter(
                UploadImageAdapter.UPLOAD_IMAGE_VIEW_HOLDER, true, canDelete, mMaxFileCount);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp_10)));
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, mColumnSize);
        setLayoutManager(layoutManager);
        setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setData(null);
        notifyAdapterDataChanged();
    }

    public void init(@NonNull BaseActivity activity, @NonNull String uploadUrl,
                     boolean canDelete, @NonNull TogetherUploadAdapter adapter, int requestCodeSelectImage) {
        initUpload(activity, uploadUrl, FileUploadComponent.TYPE_IMAGE, adapter, requestCodeSelectImage);
        mUploadImageAdapter = new UploadImageAdapter(
                UploadImageAdapter.UPLOAD_IMAGE_VIEW_HOLDER, true, canDelete, mMaxFileCount);
        addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelOffset(R.dimen.dp_10)));
        GridLayoutManager layoutManager = new GridLayoutManager(mActivity, mColumnSize);
        setLayoutManager(layoutManager);
        setAdapter(mUploadImageAdapter);
        mUploadImageAdapter.setData(null);
        notifyAdapterDataChanged();
    }

    @Override
    public int getValidImageCount() {
        ArrayList<String> list = getValidImageList();
        return list == null ? 0 : list.size();
    }

    @Override
    public void onFileUploadPrepare(List<FileUploadBean> uploadBeanList) {
        int startIndex = mUploadImageAdapter.getAdapterData().size();
        mUploadImageAdapter.addData(uploadBeanList);
        notifyAdapterItemRangeChanged(startIndex, mUploadImageAdapter.getAdapterData().size());
    }

    @Override
    public void onFileUploadProgress(FileUploadBean uploadBean) {
        notifyAdapterItemChanged(uploadBean.getOrderIndex());
    }

    @Override
    public void onFileUploadCancel(FileUploadBean uploadBean) {
        notifyAdapterItemChanged(uploadBean.getOrderIndex());
    }

    @Override
    public void onFileUploadFail(FileUploadBean uploadBean) {
        notifyAdapterItemChanged(uploadBean.getOrderIndex());
    }

    @Override
    public void onFileUploadSuccess(FileUploadBean uploadBean) {
        notifyAdapterItemChanged(uploadBean.getOrderIndex());
        notifyAdapterItemChanged(0);
    }

    @Override
    public void onFileUploadProgress(List<FileUploadBean> uploadBeanList) {
        notifyAdapterDataChanged();
    }

    @Override
    public void onFileUploadFail(List<FileUploadBean> uploadBeanList) {
        notifyAdapterDataChanged();
    }

    @Override
    public void onFileUploadSuccess(List<FileUploadBean> uploadBeanList) {
        notifyAdapterDataChanged();
    }

    public void setRemoteImages(String remoteImages, String joinStr) {
        if (TextUtils.isEmpty(remoteImages)) {
            return;
        }
        setRemoteImages(remoteImages.split(joinStr));
    }

    public void setRemoteImageList(List<String> remoteImageList) {
        if (remoteImageList == null && remoteImageList.size() < 1) {
            return;
        }
        setRemoteImages(remoteImageList.toArray(new String[0]));
    }

    public void setRemoteImages(String[] remoteImageArr) {
        if (remoteImageArr == null && remoteImageArr.length < 1) {
            return;
        }
        List<FileUploadBean> uploadImageList = new ArrayList<>();
        FileUploadBean fileUploadBean = null;
        for (int i = 0; i < remoteImageArr.length; i++) {
            fileUploadBean = new FileUploadBean();
            fileUploadBean.setRemoteFilePath(remoteImageArr[i]);
            fileUploadBean.setOrderIndex(i);
            fileUploadBean.setUploadState(FileUploadState.UPLOAD_STATE_SUCCESS);
            uploadImageList.add(fileUploadBean);
        }
        if (uploadImageList.size() < 1) {
            return;
        }
        mUploadImageAdapter.setData(uploadImageList);
        notifyAdapterDataChanged();
    }

    private ArrayList<String> getImageShowList() {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_SUCCESS);
        states.add(FileUploadState.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageList(states);
    }

    private ArrayList<String> getValidImageList() {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_SUCCESS);
        states.add(FileUploadState.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageList(states);
    }

    private ArrayList<String> getValidImageLocalList() {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_SUCCESS);
        states.add(FileUploadState.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageLocalList(states);
    }

    private ArrayList<String> getUploadedImageLocalList() {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_SUCCESS);
        return mUploadImageAdapter.getImageLocalList(states);
    }

    private ArrayList<String> getUploadingImageLocalList() {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_UPLOADING);
        return mUploadImageAdapter.getImageLocalList(states);
    }

    private ArrayList<String> getUploadedImageRemoteList() {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_SUCCESS);
        return mUploadImageAdapter.getImageRemoteList(states);
    }

    private String getUploadedImageRemoteString(String joinStr) {
        List<FileUploadState> states = new ArrayList<>();
        states.add(FileUploadState.UPLOAD_STATE_SUCCESS);
        return mUploadImageAdapter.getImageRemoteString(states, joinStr);
    }

    public List<String> getNewUploadImageRemoteList() {
        return mUploadImageAdapter.getNewUploadImageRemoteList();
    }

    public String getNewUploadImageRemoteString(String joinStr) {
        return mUploadImageAdapter.getNewUploadImageRemoteString(joinStr);
    }

    public class UploadImageAdapter extends BaseNoPaginationListAdapter<FileUploadBean> {
        public static final int UPLOAD_IMAGE_VIEW_HOLDER = 1;
        private boolean mCanUpload, mCanDelete;
        private int mMaxImageCount;

        public UploadImageAdapter(int defaultItemViewType, boolean canUpload,
                                  boolean canDelete, int maxImageCount) {
            super(defaultItemViewType);
            mCanUpload = canUpload;
            mCanDelete = canDelete;
            mMaxImageCount = maxImageCount;
        }

        @Override
        protected List<BaseListAdapterItemEntity<FileUploadBean>> parseData(List<FileUploadBean> data,
                                                                            boolean reset) {
            List<BaseListAdapterItemEntity<FileUploadBean>> adapterData = new ArrayList<>();
            BaseListAdapterItemEntity adapterEntity;
            if (mCanUpload && reset) {
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

        public ArrayList<String> getImageList(List<FileUploadState> states) {
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

        public ArrayList<String> getImageLocalList(List<FileUploadState> states) {
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

        public String getImageLocalString(List<FileUploadState> states, String joinStr) {
            List<String> list = getImageLocalList(states);
            return listJoinToString(list, joinStr);
        }

        public ArrayList<String> getImageRemoteList(List<FileUploadState> states) {
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

        public String getImageRemoteString(List<FileUploadState> states, String joinStr) {
            List<String> list = getImageRemoteList(states);
            return listJoinToString(list, joinStr);
        }

        public ArrayList<String> getNewUploadImageRemoteList() {
            ArrayList<String> retList = new ArrayList<>();
            for (int i = 0; i < mData.size(); i++) {
                FileUploadBean bean = mData.get(i).getData();
                if (bean != null && bean.getUploadState() == FileUploadState.UPLOAD_STATE_SUCCESS &&
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
            for (int i = 1; i < list.size(); i++) {
                reStr += joinStr + list.get(i);
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
                                   BaseListAdapterItemProperty propertyEntity, final int position) {
                if (position < mData.size() && (mCanUpload && position > 0 || !mCanUpload)) {
                    imageBean.setOrderIndex(position);
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
                        case UPLOAD_STATE_PREPARING:
                        case UPLOAD_STATE_UPLOADING:
                            state_rl.setVisibility(View.VISIBLE);
                            loading_iv.setVisibility(View.VISIBLE);
                            loading_tv.setVisibility(VISIBLE);
                            loading_tv.setText(imageBean.getUploadProgress() + "%");
                            break;
                        case UPLOAD_STATE_CANCEL:
                            state_rl.setVisibility(View.VISIBLE);
                            fail_tv.setVisibility(View.VISIBLE);
                            break;
                        case UPLOAD_STATE_FAIL:
                            state_rl.setVisibility(View.VISIBLE);
                            fail_tv.setVisibility(View.VISIBLE);
                            break;
                        case UPLOAD_STATE_SUCCESS:
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
                            displayUploadObject(getImageShowList(), mCanUpload ? position - 1 : position);
                        }
                    });
                    if (mCanDelete) {
                        delete_iv.setVisibility(View.VISIBLE);
                        delete_iv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mData.remove(position);
                                mFileUploadComponent.cancel(imageBean);
                                notifyAdapterDataChanged();
                            }
                        });
                    } else {
                        delete_iv.setVisibility(View.GONE);
                    }
                } else if (mCanUpload) {
                    show_iv.setImageResource(R.mipmap.base_iv_add_upload_image);// 第一个显示加号图片
                    int successSize = getUploadedImageRemoteList().size();
                    num_max_tv.setText(successSize + "/" + mMaxImageCount);
                    num_max_tv.setVisibility(VISIBLE);
                    show_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectUploadObjects();
                        }
                    });
                    state_rl.setVisibility(View.GONE);
                    delete_iv.setVisibility(View.GONE);
                }
            }
        }
    }

    protected void notifyAdapterDataChanged() {
        mUploadImageAdapter.notifyDataSetChanged();
    }

    protected void notifyAdapterItemChanged(int index) {
        mUploadImageAdapter.notifyItemChanged(index);
    }

    protected void notifyAdapterItemRangeChanged(int start, int end) {
        mUploadImageAdapter.notifyItemRangeChanged(start, end);
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
