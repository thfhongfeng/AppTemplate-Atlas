package com.pine.base.mvc.fragment;

import android.content.Context;
import android.support.annotation.CallSuper;

import com.pine.base.ui.BaseFragment;

public abstract class BaseMvcFragment extends BaseFragment {

    @CallSuper
    @Override
    protected void beforeInit() {

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
