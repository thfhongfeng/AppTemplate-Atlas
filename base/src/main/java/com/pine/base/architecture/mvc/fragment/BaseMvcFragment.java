package com.pine.base.architecture.mvc.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;

import com.pine.base.ui.BaseFragment;

public abstract class BaseMvcFragment extends BaseFragment {

    @CallSuper
    @Override
    protected void beforeInitOnCreateView(@Nullable Bundle savedInstanceState) {

    }

    @CallSuper
    @Override
    protected void afterInit() {

    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
