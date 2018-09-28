package com.pine.mvp.ui.fragment;

import android.view.View;

import com.pine.base.mvp.ui.fragment.BaseMvpFragment;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpHomePartAContract;
import com.pine.mvp.presenter.MvpHomePartAPresenter;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartBFragment extends BaseMvpFragment<IMvpHomePartAContract.Ui, MvpHomePartAPresenter>
        implements IMvpHomePartAContract.Ui {
    @Override
    protected MvpHomePartAPresenter createPresenter() {
        return new MvpHomePartAPresenter();
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.mvp_fragment_home_part_a;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View layout) {

    }
}
