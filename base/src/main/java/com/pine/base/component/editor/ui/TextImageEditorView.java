package com.pine.base.component.editor.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.R;
import com.pine.base.component.editor.bean.TextImageEditorItemData;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.base.component.uploader.FileUploadComponent;
import com.pine.base.component.uploader.bean.FileUploadBean;
import com.pine.base.component.uploader.bean.FileUploadState;
import com.pine.base.component.uploader.ui.UploadFileLinearLayout;
import com.pine.base.ui.BaseActivity;
import com.pine.base.util.DialogUtils;
import com.pine.tool.util.KeyboardUtils;
import com.pine.tool.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import static com.pine.base.component.editor.bean.TextImageItemEntity.TYPE_IMAGE;
import static com.pine.base.component.editor.bean.TextImageItemEntity.TYPE_TEXT;

/**
 * Created by tanghongfeng on 2018/11/13
 */

public class TextImageEditorView extends UploadFileLinearLayout {
    private final String TAG = LogUtils.makeLogTag(this.getClass());

    // 编辑器索引
    private int mIndex;
    // 编辑器标题
    private String mTitle;
    // 子编辑条目之前的子View的个数（用于显示顶部标题等的非编辑型View，获取的编辑数据从这些View之后开始）
    private int mInitChildViewCount;
    // 顶部标题View
    private View mTopTitleView;
    // 当前子编辑条目View
    private View mCurAddNoteView;

    public TextImageEditorView(Context context) {
        super(context);
    }

    public TextImageEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseFileUploadView);
        mMaxFileCount = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileCount, 10);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileSize, 1024 * 1024);
    }

    public TextImageEditorView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseFileUploadView);
        mMaxFileCount = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileCount, 10);
        mMaxImageSize = typedArray.getInt(R.styleable.BaseFileUploadView_baseMaxFileSize, 1024 * 1024);
    }

    public void init(@NonNull BaseActivity activity, @NonNull String uploadUrl, int index,
                     String title, OneByOneUploadAdapter adapter, int requestCodeSelectImage) {
        initUpload(activity, uploadUrl, FileUploadComponent.TYPE_IMAGE, adapter, requestCodeSelectImage);
        initView(index, title);
    }

    public void init(@NonNull BaseActivity activity, @NonNull String uploadUrl, int index,
                     String title, TogetherUploadAdapter adapter, int requestCodeSelectImage) {
        initUpload(activity, uploadUrl, FileUploadComponent.TYPE_IMAGE, adapter, requestCodeSelectImage);
        initView(index, title);
    }

    public void initView(int index, String title) {
        setOrientation(VERTICAL);
        mIndex = index;
        mTitle = title;

        mInitChildViewCount = 0;
        mTopTitleView = LayoutInflater.from(mActivity).inflate(R.layout.base_text_image_editor_top, null);
        ((TextView) mTopTitleView.findViewById(R.id.title_tv)).setText(mTitle);
        mTopTitleView.setVisibility(TextUtils.isEmpty(mTitle) ? GONE : VISIBLE);
        addView(mTopTitleView);
        mInitChildViewCount++;

        mInitChildViewCount++;
        addText(mInitChildViewCount - 1, new TextImageEditorItemData(TYPE_TEXT), false);
    }

    private void addText(int position, @NonNull TextImageEditorItemData data, boolean needFocus) {
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.base_text_image_editor_item_text, null);

        setupEditorView(position, view);
        if (position == mInitChildViewCount - 1) {
            view.findViewById(R.id.text_rl).setVisibility(GONE);
        }

        addView(view, position);
        view.setTag(data);

        EditText text_et = view.findViewById(R.id.text_et);
        text_et.setText(data.getText());
        if (needFocus) {
            text_et.requestFocus();
            KeyboardUtils.openSoftKeyboard(mActivity, text_et);
        }
    }

    private void addImage(final int position, @NonNull TextImageEditorItemData data) {
        final View view = LayoutInflater.from(mActivity).inflate(R.layout.base_text_image_editor_item_image, null);

        setupEditorView(position, view);

        addView(view, position);
        view.setTag(data);

        ImageView image_iv = view.findViewById(R.id.image_iv);
        EditText text_et = view.findViewById(R.id.text_et);
        String imageUrl = "";
        if (data != null) {
            if (!TextUtils.isEmpty(data.getLocalFilePath())) {
                imageUrl = "file://" + data.getLocalFilePath();
            } else if (!TextUtils.isEmpty(data.getRemoteFilePath())) {
                imageUrl = data.getRemoteFilePath();
            }
        }
        final String url = imageUrl;
        ImageLoaderManager.getInstance().loadImage(mActivity, imageUrl, image_iv);
        image_iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> urlList = new ArrayList<>();
                urlList.add(url);
                displayUploadObject(urlList, 0);
            }
        });
        text_et.setText(data.getText());
        refreshImageState(view, data.getUploadState(), 0);
    }

    private void setupEditorView(int position, final View view) {
        if (position > mInitChildViewCount - 1) {
            view.findViewById(R.id.delete_iv).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showConfirmDialog(mActivity,
                            mActivity.getString(R.string.base_delete_content_confirm_msg),
                            new DialogUtils.IActionListener() {
                                @Override
                                public void onLeftBtnClick() {

                                }

                                @Override
                                public void onRightBtnClick() {
                                    removeView(view);
                                }
                            });
                }
            });
        }
        view.findViewById(R.id.add_text_btn_tv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurAddNoteView = view;
                addText(indexOfChild(view) + 1, new TextImageEditorItemData(TYPE_TEXT), true);
            }
        });
        view.findViewById(R.id.add_image_btn_tv).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurAddNoteView = view;
                selectUploadObjects();
            }
        });
    }

    private void refreshImageState(View view, FileUploadState state, int progress) {
        TextView loading_tv = view.findViewById(R.id.loading_tv);
        View result_tv = view.findViewById(R.id.result_tv);
        View state_rl = view.findViewById(R.id.state_rl);
        switch (state) {
            case UPLOAD_STATE_PREPARING:
            case UPLOAD_STATE_UPLOADING:
                loading_tv.setText(progress + "%");
                loading_tv.setVisibility(VISIBLE);
                result_tv.setVisibility(GONE);
                state_rl.setVisibility(VISIBLE);
                break;
            case UPLOAD_STATE_CANCEL:
                loading_tv.setVisibility(GONE);
                result_tv.setVisibility(VISIBLE);
                state_rl.setVisibility(VISIBLE);
                break;
            case UPLOAD_STATE_FAIL:
                loading_tv.setVisibility(GONE);
                result_tv.setVisibility(VISIBLE);
                state_rl.setVisibility(VISIBLE);
                break;
            case UPLOAD_STATE_SUCCESS:
                state_rl.setVisibility(GONE);
                break;
            default:
                state_rl.setVisibility(GONE);
                break;
        }
    }

    public void setTitle(String title) {
        mTitle = title;
        ((TextView) mTopTitleView.findViewById(R.id.title_tv)).setText(mTitle);
        mTopTitleView.setVisibility(TextUtils.isEmpty(mTitle) ? GONE : VISIBLE);
    }

    public List<TextImageEditorItemData> getValidImageDataList() {
        List<TextImageEditorItemData> imageItemUrlList = new ArrayList<>();
        if (getChildCount() > mInitChildViewCount) {
            for (int i = mInitChildViewCount; i < getChildCount(); i++) {
                TextImageEditorItemData data = (TextImageEditorItemData) getChildAt(i).getTag();
                if (data != null && TYPE_IMAGE.equals(data.getType()) &&
                        (data.getUploadState() != FileUploadState.UPLOAD_STATE_UPLOADING ||
                                data.getUploadState() == FileUploadState.UPLOAD_STATE_SUCCESS)) {
                    imageItemUrlList.add(data);
                }
            }
        }
        return imageItemUrlList;
    }

    public List<TextImageEditorItemData> getData() {
        List<TextImageEditorItemData> data = new ArrayList<>();
        int childCount = getChildCount();
        for (int i = mInitChildViewCount; i < childCount; i++) {
            View view = getChildAt(i);
            TextImageEditorItemData itemData = (TextImageEditorItemData) view.getTag();
            if (itemData != null) {
                EditText editText = view.findViewById(R.id.text_et);
                itemData.setText(editText.getText().toString());
                data.add(itemData);
            }
        }
        return data;
    }

    public void setData(List<TextImageEditorItemData> data) {
        for (int i = 0; i < data.size(); i++) {
            TextImageEditorItemData itemData = data.get(i);
            if (itemData == null) {
                continue;
            }
            if (TYPE_TEXT.equals(itemData.getType())) {
                addText(i + mInitChildViewCount, itemData, false);
            } else if (TYPE_IMAGE.equals(itemData.getType())) {
                addImage(i + mInitChildViewCount, itemData);
            }
        }
        invalidate();
    }

    @Override
    public int getValidImageCount() {
        List<TextImageEditorItemData> imageItemUrlList = getValidImageDataList();
        return imageItemUrlList == null ? 0 : imageItemUrlList.size();
    }

    @Override
    public void onFileUploadPrepare(List<FileUploadBean> uploadBeanList) {
        for (int i = 0; i < uploadBeanList.size(); i++) {
            FileUploadBean bean = uploadBeanList.get(i);
            TextImageEditorItemData data = new TextImageEditorItemData(TYPE_IMAGE);
            data.setUploadState(bean.getUploadState());
            data.setUploadProgress(bean.getUploadProgress());
            data.setLocalFilePath(bean.getLocalFilePath());
            data.setRemoteFilePath(bean.getRemoteFilePath());
            int viewIndex = indexOfChild(mCurAddNoteView) + 1 + i;
            addImage(viewIndex, data);
            bean.setAttachView(getChildAt(viewIndex));
        }
    }

    @Override
    public void onFileUploadProgress(FileUploadBean uploadBean) {
        if (uploadBean != null && uploadBean.getAttachView() != null) {
            copyUploadData(uploadBean);
            refreshImageState(uploadBean.getAttachView(), uploadBean.getUploadState(),
                    uploadBean.getUploadProgress());
        }
    }

    @Override
    public void onFileUploadCancel(FileUploadBean uploadBean) {
        if (uploadBean != null && uploadBean.getAttachView() != null) {
            copyUploadData(uploadBean);
            refreshImageState(uploadBean.getAttachView(), uploadBean.getUploadState(),
                    uploadBean.getUploadProgress());
        }
    }

    @Override
    public void onFileUploadFail(FileUploadBean uploadBean) {
        if (uploadBean != null && uploadBean.getAttachView() != null) {
            copyUploadData(uploadBean);
            refreshImageState(uploadBean.getAttachView(), uploadBean.getUploadState(),
                    uploadBean.getUploadProgress());
        }
    }

    @Override
    public void onFileUploadSuccess(FileUploadBean uploadBean) {
        if (uploadBean != null && uploadBean.getAttachView() != null) {
            copyUploadData(uploadBean);
            refreshImageState(uploadBean.getAttachView(), uploadBean.getUploadState(),
                    uploadBean.getUploadProgress());
        }
    }

    @Override
    public void onFileUploadProgress(List<FileUploadBean> uploadBeanList) {
        for (FileUploadBean bean : uploadBeanList) {
            if (bean != null && bean.getAttachView() != null) {
                copyUploadData(bean);
                refreshImageState(bean.getAttachView(), bean.getUploadState(),
                        bean.getUploadProgress());
            }
        }
    }

    @Override
    public void onFileUploadFail(List<FileUploadBean> uploadBeanList) {
        for (FileUploadBean bean : uploadBeanList) {
            if (bean != null && bean.getAttachView() != null) {
                copyUploadData(bean);
                refreshImageState(bean.getAttachView(), bean.getUploadState(),
                        bean.getUploadProgress());
            }
        }
    }

    @Override
    public void onFileUploadSuccess(List<FileUploadBean> uploadBeanList) {
        for (FileUploadBean bean : uploadBeanList) {
            if (bean != null && bean.getAttachView() != null) {
                copyUploadData(bean);
                refreshImageState(bean.getAttachView(), bean.getUploadState(),
                        bean.getUploadProgress());
            }
        }
    }

    private void copyUploadData(FileUploadBean uploadBean) {
        Object obj = uploadBean.getAttachView().getTag();
        if (obj != null && obj instanceof TextImageEditorItemData) {
            TextImageEditorItemData data = (TextImageEditorItemData) obj;
            data.setUploadState(uploadBean.getUploadState());
            data.setUploadProgress(uploadBean.getUploadProgress());
            data.setLocalFilePath(uploadBean.getLocalFilePath());
            data.setRemoteFilePath(uploadBean.getRemoteFilePath());
        }
    }
}
