package com.pine.base;

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
        beforeInit();

        initData();

        View layout = inflater.inflate(getFragmentLayoutResId(), container, false);
        initView(layout);

        return layout;
    }

    protected void beforeInit() {

    }

    protected abstract int getFragmentLayoutResId();

    protected abstract void initData();

    protected abstract void initView(View layout);
}
