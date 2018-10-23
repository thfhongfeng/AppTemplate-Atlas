package com.pine.base.architecture.mvc.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.pine.base.ui.BaseFragment;

public abstract class BaseMvcFragment extends BaseFragment {

    @CallSuper
    @Override
    protected void beforeInitOnCreateView() {

    }

    @CallSuper
    @Override
    protected void afterInitOnCreateView() {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
