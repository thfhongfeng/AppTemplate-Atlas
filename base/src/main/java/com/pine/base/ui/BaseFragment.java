package com.pine.base.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseFragment extends Fragment {
    protected final String TAG = LogUtils.makeLogTag(this.getClass());

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onCreateViewBeforeInit();

        onCreateViewInitData();

        View layout = inflater.inflate(getFragmentLayoutResId(), container, false);
        onCreateViewInitView(layout);

        return layout;
    }

    protected abstract int getFragmentLayoutResId();

    /**
     * onCreateView中前置初始化
     */
    protected void onCreateViewBeforeInit() {

    }

    /**
     * onCreateView中初始化数据
     */
    protected abstract void onCreateViewInitData();

    /**
     * onCreateView中初始化View
     */
    protected abstract void onCreateViewInitView(View layout);
}
