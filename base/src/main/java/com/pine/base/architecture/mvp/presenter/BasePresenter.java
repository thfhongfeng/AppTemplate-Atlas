package com.pine.base.architecture.mvp.presenter;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.base.architecture.mvp.contract.IBaseContract;
import com.pine.tool.util.LogUtils;

import java.lang.ref.WeakReference;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public abstract class BasePresenter<V extends IBaseContract.Ui> {
    protected final String TAG = LogUtils.makeLogTag(this.getClass());

    protected boolean mIsLoadProcessing;

    /**
     * UI的弱引用
     */
    private WeakReference<V> mUiRef;

    /**
     * 关联UI
     *
     * @param ui
     */
    @CallSuper
    public void attachUi(V ui) {
        mUiRef = new WeakReference<V>(ui);
    }

    /**
     * 解除关联
     */
    @CallSuper
    public void detachUi() {
        if (mUiRef != null) {
            onUiState(BasePresenter.UiState.UI_STATE_ON_DETACH);
            mUiRef.clear();
        }
    }

    /**
     * 得到UI
     *
     * @return
     */
    public V getUi() {
        return mUiRef.get();
    }

    /**
     * 得到Application
     *
     * @return
     */
    public Application getApplication() {
        return BaseApplication.mApplication;
    }

    /**
     * 得到Activity
     *
     * @return
     */
    public Activity getActivity() {
        return getUi().getContextActivity();
    }

    /**
     * 得到Context
     *
     * @return
     */
    public Context getContext() {
        return getUi().getContextActivity();
    }

    /**
     * 得到Intent
     *
     * @return
     */
    public Intent getIntent() {
        if (mUiRef.get() != null) {
            if (mUiRef.get() instanceof Activity) {
                return ((Activity) mUiRef.get()).getIntent();
            } else if (mUiRef.get() instanceof Fragment) {
                return ((Fragment) mUiRef.get()).getActivity().getIntent();
            } else if (mUiRef.get() instanceof android.support.v4.app.Fragment) {
                return ((android.support.v4.app.Fragment) mUiRef.get()).getActivity().getIntent();
            }
        }
        return new Intent();
    }

    public final boolean isUiAlive() {
        if (mUiRef.get() == null) {
            return false;
        }
        if (mUiRef.get() instanceof Activity) {
            return !((Activity) mUiRef.get()).isFinishing();
        }
        return true;
    }

    public final void finishUi() {
        if (mUiRef.get() != null && mUiRef.get() instanceof Activity) {
            ((Activity) mUiRef.get()).finish();
        }
    }

    public void setUiLoading(boolean uiLoading) {
        mIsLoadProcessing = uiLoading;
        if (!isUiAlive()) {
            return;
        }
        getUi().setLoadingUiVisibility(uiLoading);
    }

    /**
     * 用于分析传入参数是否非法
     *
     * @return true表示非法， false表示合法
     */
    public abstract boolean parseInitData(Bundle bundle);

    /**
     * UI状态回调
     *
     * @param state UI_STATE_ON_CREATE,UI_STATE_ON_START,UI_STATE_ON_RESUME,UI_STATE_ON_PAUSE,
     *              UI_STATE_ON_STOP,UI_STATE_ON_DETACH
     */
    public abstract void onUiState(BasePresenter.UiState state);

    public void showShortToast(String message) {
        if (isUiAlive()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showShortToast(@StringRes int resId) {
        if (isUiAlive()) {
            Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
        }
    }

    public void showLongToast(String message) {
        if (isUiAlive()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }
    }

    public void showLongToast(@StringRes int resId) {
        if (isUiAlive()) {
            Toast.makeText(getContext(), resId, Toast.LENGTH_LONG).show();
        }
    }

    public enum UiState {
        UI_STATE_UNDEFINE,
        UI_STATE_ON_CREATE,
        UI_STATE_ON_START,
        UI_STATE_ON_RESUME,
        UI_STATE_ON_PAUSE,
        UI_STATE_ON_STOP,
        UI_STATE_ON_DETACH
    }
}
