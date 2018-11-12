package com.pine.base.architecture.mvp.bean;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.pine.tool.util.RegexUtils;
import com.pine.tool.util.ViewActionUtils;

/**
 * Created by tanghongfeng on 2018/11/8
 */

public class InputParamBean {
    private Context context;
    private View parentScrollView;
    private String key;
    private String value;
    private View inputView;

    public InputParamBean(@NonNull Context context, View parentScrollView,
                          @NonNull String key, String value, @NonNull View inputView) {
        this.context = context;
        this.parentScrollView = parentScrollView;
        this.key = key;
        this.value = value;
        this.inputView = inputView;
    }

    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public View getInputView() {
        return inputView;
    }

    public void setInputView(View inputView) {
        this.inputView = inputView;
    }

    public boolean checkIsEmpty(String failMessage) {
        if (TextUtils.isEmpty(value)) {
            if (parentScrollView != null && parentScrollView.isAttachedToWindow()) {
                ViewActionUtils.scrollToTargetView(context, parentScrollView, inputView);
            }
            Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public boolean checkIsEmpty(@StringRes int failMsgResId) {
        return checkIsEmpty(context.getString(failMsgResId));
    }

    public boolean checkNumberRange(String failMessage, int min, int max) {
        try {
            int num = Integer.parseInt(value);
            if (num >= min && num <= max) {
                return true;
            }
        } catch (NumberFormatException e) {
        }
        if (parentScrollView != null && parentScrollView.isAttachedToWindow()) {
            ViewActionUtils.scrollToTargetView(context, parentScrollView, inputView);
        }
        Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show();
        return false;
    }

    public boolean checkNumberRange(@StringRes int failMsgResId, int min, int max) {
        return checkNumberRange(context.getString(failMsgResId), min, max);
    }

    public boolean checkIsPhone(String failMessage) {
        if (!RegexUtils.isMobilePhoneNumber(value)) {
            if (parentScrollView != null && parentScrollView.isAttachedToWindow()) {
                ViewActionUtils.scrollToTargetView(context, parentScrollView, inputView);
            }
            Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean checkIsPhone(@StringRes int failMsgResId) {
        return checkIsPhone(context.getString(failMsgResId));
    }
}
