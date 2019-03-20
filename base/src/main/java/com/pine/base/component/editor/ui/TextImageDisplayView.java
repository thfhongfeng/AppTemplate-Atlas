package com.pine.base.component.editor.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pine.base.R;
import com.pine.base.component.editor.bean.TextImageItemEntity;
import com.pine.base.component.image_loader.ImageLoaderManager;
import com.pine.tool.util.LogUtils;

import java.util.List;

import static com.pine.base.component.editor.bean.TextImageItemEntity.TYPE_IMAGE;
import static com.pine.base.component.editor.bean.TextImageItemEntity.TYPE_TEXT;

/**
 * Created by tanghongfeng on 2018/11/13
 */

public class TextImageDisplayView extends LinearLayout {
    private final String TAG = LogUtils.makeLogTag(this.getClass());

    // View标题
    private String mTitle;
    private List<TextImageItemEntity> mContent;

    public TextImageDisplayView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public TextImageDisplayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public TextImageDisplayView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(VERTICAL);
    }

    public void setTitle(String title) {
        mTitle = title;
        View topTitleView = LayoutInflater.from(getContext()).inflate(R.layout.base_text_image_display_top, null);
        ((TextView) topTitleView.findViewById(R.id.title_tv)).setText(mTitle);
        topTitleView.setVisibility(TextUtils.isEmpty(mTitle) ? GONE : VISIBLE);
        addView(topTitleView);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setContent(List<TextImageItemEntity> dataList) {
        removeAllViews();
        mContent = dataList;
        if (dataList != null) {
            for (TextImageItemEntity entity : dataList) {
                if (TYPE_TEXT.equals(entity.getType())) {
                    addText(entity);
                } else if (TYPE_IMAGE.equals(entity.getType())) {
                    addImage(entity);
                }
            }
        }
    }

    public List<TextImageItemEntity> getContent() {
        return mContent;
    }

    private void addText(@NonNull TextImageItemEntity data) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.base_text_image_display_item_text, null);
        addView(view);
        ((TextView) view.findViewById(R.id.text_tv)).setText(data.getText());
    }

    private void addImage(@NonNull TextImageItemEntity data) {
        final View view = LayoutInflater.from(getContext()).inflate(R.layout.base_text_image_display_item_image, null);
        addView(view);

        ((TextView) view.findViewById(R.id.text_tv)).setText(data.getText());
        ImageView image_iv = view.findViewById(R.id.image_iv);
        String imageUrl = "";
        if (data != null) {
            if (!TextUtils.isEmpty(data.getLocalFilePath())) {
                imageUrl = "file://" + data.getLocalFilePath();
            } else if (!TextUtils.isEmpty(data.getRemoteFilePath())) {
                imageUrl = data.getRemoteFilePath();
            }
        }
        ImageLoaderManager.getInstance().loadImage(getContext(), imageUrl, image_iv);
    }
}
