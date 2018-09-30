package com.pine.base.mvc.fragment;

import android.content.Context;

import com.pine.base.ui.BaseFragment;

public abstract class BaseMvcFragment extends BaseFragment {

    protected final void beforeInit() {

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
