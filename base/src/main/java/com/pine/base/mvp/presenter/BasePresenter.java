package com.pine.base.mvp.presenter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CallSuper;

import com.pine.base.mvp.contract.IBaseContract;
import com.pine.tool.util.LogUtils;

import java.lang.ref.WeakReference;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public abstract class BasePresenter<V extends IBaseContract.Ui> {
    public final static int UI_STATE_ON_ATTACH = 1;
    public final static int UI_STATE_ON_SHOW = 2;
    public final static int UI_STATE_ON_PAUSE = 3;
    public final static int UI_STATE_ON_HIDE = 4;
    public final static int UI_STATE_ON_DETACH = 5;
    protected final String TAG = LogUtils.makeLogTag(this.getClass());
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
        onUiState(BasePresenter.UI_STATE_ON_ATTACH);
    }

    /**
     * 解除关联
     */
    @CallSuper
    public void detachUi() {
        if (mUiRef != null) {
            onUiState(BasePresenter.UI_STATE_ON_DETACH);
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
     * 得到Context
     *
     * @return
     */
    public Context getContext() {
        return getUi().getContext();
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

    public abstract void onUiState(int state);
}
