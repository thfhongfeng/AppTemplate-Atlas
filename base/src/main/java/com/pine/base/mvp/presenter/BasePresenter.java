package com.pine.base.mvp.presenter;

import android.app.Activity;
import android.content.Context;

import com.pine.base.mvp.contract.IBaseContract;
import com.pine.tool.util.LogUtils;

import java.lang.ref.WeakReference;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public abstract class BasePresenter<V extends IBaseContract.Ui> {
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
    public void attachUi(V ui) {
        mUiRef = new WeakReference<V>(ui);
    }

    /**
     * 解除关联
     */
    public void detachUi() {
        if (mUiRef != null) {
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
}
