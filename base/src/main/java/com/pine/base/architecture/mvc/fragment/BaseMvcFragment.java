package com.pine.base.architecture.mvc.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.pine.base.ui.BaseFragment;

public abstract class BaseMvcFragment extends BaseFragment {

    @CallSuper
    @Override
    protected void onCreateViewBeforeInit() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
