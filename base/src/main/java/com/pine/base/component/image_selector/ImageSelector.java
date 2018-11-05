package com.pine.base.component.image_selector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.pine.base.component.image_selector.ui.ImageSelectActivity;

import java.util.ArrayList;

/**
 * Created by tanghongfeng on 2018/11/2
 */

public class ImageSelector {
    public static final String INTENT_SELECTED_IMAGE_LIST = "intent_selected_image_list";
    public static final String INTENT_MAX_SELECTED_COUNT = "intent_max_select_count";
    private int mMaxCount = 9;
    private ArrayList<String> mSelectedData;

    private ImageSelector() {
    }

    public static ImageSelector create() {
        return new ImageSelector();
    }

    public ImageSelector count(int count) {
        mMaxCount = count;
        return this;
    }

    public ImageSelector selected(ArrayList<String> images) {
        mSelectedData = images;
        return this;
    }

    public void start(Activity activity, int requestCode) {
        final Context context = activity;
        Intent intent = new Intent(context, ImageSelectActivity.class);
        intent.putExtra(INTENT_MAX_SELECTED_COUNT, mMaxCount);
        if (mSelectedData != null) {
            intent.putStringArrayListExtra(INTENT_SELECTED_IMAGE_LIST, mSelectedData);
        }
        activity.startActivityForResult(intent, requestCode);
    }
}
