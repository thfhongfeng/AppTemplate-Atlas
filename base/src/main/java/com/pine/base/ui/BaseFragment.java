package com.pine.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pine.base.access.UiAccessManager;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseFragment extends Fragment {
    protected final String TAG = LogUtils.makeLogTag(this.getClass());
    private boolean mUiAccessReady;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        beforeInitOnCreateView();
        View layout = inflater.inflate(getFragmentLayoutResId(), container, false);

        findViewOnCreateView(layout);

        mUiAccessReady = true;
        if (!UiAccessManager.getInstance().checkCanAccess(this, false)) {
            mUiAccessReady = false;
        }

        tryOnAllRestrictionReleased();

        return layout;
    }

    protected abstract int getFragmentLayoutResId();

    /**
     * onCreateView中前置初始化
     */
    protected void beforeInitOnCreateView() {

    }

    /**
     * onCreateView中初始化View
     */
    protected abstract void findViewOnCreateView(View layout);

    private void tryOnAllRestrictionReleased() {
        if (mUiAccessReady) {
            if (!parseArgumentsOnCreateView()) {
                initOnCreateView();
                afterInitOnCreateView();
            }
        }
    }

    protected abstract boolean parseArgumentsOnCreateView();

    /**
     * 所有准入条件(如：登陆限制，权限限制等)全部解除后回调（界面的数据业务初始化动作推荐在此进行）
     */
    protected abstract void initOnCreateView();

    /**
     * onCreate中结束初始化
     */
    protected abstract void afterInitOnCreateView();

    public void startLoadingUi() {
    }

    public void finishLoadingUi() {
    }
}
