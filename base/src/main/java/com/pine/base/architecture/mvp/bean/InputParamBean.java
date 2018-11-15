package com.pine.base.architecture.mvp.bean;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

import com.pine.tool.util.RegexUtils;
import com.pine.tool.util.ViewActionUtils;

/**
 * Created by tanghongfeng on 2018/11/8
 */

public class InputParamBean<T> {
    private Context context;
    private View containerView;
    private String key;
    private T value;
    private View inputView;

    public InputParamBean(@NonNull Context context, @NonNull String key, T value) {
        this.context = context;
        this.key = key;
        this.value = value;
    }

    public InputParamBean(@NonNull Context context, @NonNull String key, T value,
                          View containerView, View inputView) {
        this.context = context;
        this.key = key;
        this.value = value;
        this.containerView = containerView;
        this.inputView = inputView;
    }

    public String getKey() {
        return key;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public View getInputView() {
        return inputView;
    }

    public void setInputView(View inputView) {
        this.inputView = inputView;
    }

    public void toastAndTryScrollTo(String failMessage) {
        if (containerView != null && containerView.isAttachedToWindow() && inputView != null) {
            ViewActionUtils.scrollToTargetView(context, containerView, inputView);
        }
        Toast.makeText(context, failMessage, Toast.LENGTH_SHORT).show();
    }

    public void toastAndTryScrollTo(@StringRes int failMsgResId) {
        if (containerView != null && containerView.isAttachedToWindow() && inputView != null) {
            ViewActionUtils.scrollToTargetView(context, containerView, inputView);
        }
        Toast.makeText(context, failMsgResId, Toast.LENGTH_SHORT).show();
    }

    public boolean checkIsEmpty(String failMessage) {
        if (value == null || "".equals(value.toString())) {
            toastAndTryScrollTo(failMessage);
            return true;
        }
        return false;
    }

    public boolean checkIsEmpty(@StringRes int failMsgResId) {
        return checkIsEmpty(context.getString(failMsgResId));
    }

    public boolean checkNumberRange(String failMessage, int min, int max) {
        try {
            int num = Integer.parseInt(value.toString());
            if (num >= min && num <= max) {
                return true;
            }
        } catch (NumberFormatException e) {
        }
        toastAndTryScrollTo(failMessage);
        return false;
    }

    public boolean checkNumberRange(@StringRes int failMsgResId, int min, int max) {
        return checkNumberRange(context.getString(failMsgResId), min, max);
    }

    public boolean checkIsPhone(String failMessage) {
        if (!RegexUtils.isMobilePhoneNumber(value.toString())) {
            toastAndTryScrollTo(failMessage);
            return false;
        }
        return true;
    }

    public boolean checkIsPhone(@StringRes int failMsgResId) {
        return checkIsPhone(context.getString(failMsgResId));
    }
}
