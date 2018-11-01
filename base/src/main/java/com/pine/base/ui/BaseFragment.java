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

        initDataOnCreateView();

        initViewOnCreateView(layout);

        mUiAccessReady = true;
        if (!UiAccessManager.getInstance().checkCanAccess(this)) {
            mUiAccessReady = false;
        }

        afterInitOnCreateView();

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
     * onCreateView中初始化数据
     */
    protected abstract void initDataOnCreateView();

    /**
     * onCreateView中初始化View
     */
    protected abstract void initViewOnCreateView(View layout);

    /**
     * onCreate中结束初始化
     */
    protected abstract void afterInitOnCreateView();

    private void tryOnAllRestrictionReleased() {
        if (mUiAccessReady) {
            onAllAccessRestrictionReleased();
        }
    }

    /**
     * 所有准入条件(如：登陆限制，权限限制等)全部解除后回调（界面的数据业务初始化动作推荐在此进行）
     */
    protected abstract void onAllAccessRestrictionReleased();
}
