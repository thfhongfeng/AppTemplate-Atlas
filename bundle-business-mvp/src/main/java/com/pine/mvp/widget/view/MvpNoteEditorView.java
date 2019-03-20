package com.pine.mvp.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.pine.base.component.editor.bean.TextImageEditorItemData;
import com.pine.base.component.editor.ui.TextImageEditorView;
import com.pine.base.component.uploader.ui.UploadFileLinearLayout;
import com.pine.base.ui.BaseActivity;
import com.pine.mvp.MvpUrlConstants;
import com.pine.mvp.R;
import com.pine.tool.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class MvpNoteEditorView extends LinearLayout {
    public MvpNoteEditorView(Context context) {
        super(context);
        setOrientation(VERTICAL);
    }

    public MvpNoteEditorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
    }

    public MvpNoteEditorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
    }

    public List<List<TextImageEditorItemData>> getNoteDayList() {
        List<List<TextImageEditorItemData>> list = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            list.add(((TextImageEditorView) getChildAt(i)).getData());
        }
        return list;
    }

    public void setDayCount(BaseActivity activity, int dayCount, List<List<TextImageEditorItemData>> dayList,
                            UploadFileLinearLayout.OneByOneUploadAdapter adapter) {
        int childCount = getChildCount();
        if (dayCount > childCount) {
            for (int i = childCount; i < dayCount; i++) {
                addDayView(activity, dayList == null ? null : dayList.get(i + 1), i + 1, adapter);
            }
        } else if (dayCount < childCount) {
            removeViews(dayCount, childCount - dayCount);
        }
        if (dayCount == 1) {
            ((TextImageEditorView) getChildAt(0)).setTitle("");
        } else if (dayCount > 1) {
            ((TextImageEditorView) getChildAt(0))
                    .setTitle(getContext().getString(R.string.mvp_note_release_day_note_title,
                            StringUtils.toChineseNumber(1)));
        }
        invalidate();
    }

    private void addDayView(BaseActivity activity, List<TextImageEditorItemData> data, int day,
                            UploadFileLinearLayout.OneByOneUploadAdapter adapter) {
        TextImageEditorView view = new TextImageEditorView(getContext());
        String title = getContext().getString(R.string.mvp_note_release_day_note_title, StringUtils.toChineseNumber(day));
        view.init(activity, MvpUrlConstants.Upload_File, day, title,
                adapter, 100 + day);
        if (data != null) {
            view.setData(data);
        }
        addView(view);
    }
}
