package com.pine.mvp.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pine.base.mvp.ui.fragment.BaseMvpFragment;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpHomeItemNoPaginationAdapter;
import com.pine.mvp.bean.MvpHomePartCEntity;
import com.pine.mvp.contract.IMvpHomePartCContract;
import com.pine.mvp.presenter.MvpHomePartCPresenter;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpHomePartCFragment extends BaseMvpFragment<IMvpHomePartCContract.Ui, MvpHomePartCPresenter>
        implements IMvpHomePartCContract.Ui, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView recycle_view;
    private MvpHomeItemNoPaginationAdapter mMvpHomeItemAdapter;

    @Override
    protected MvpHomePartCPresenter createPresenter() {
        return new MvpHomePartCPresenter();
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.mvp_fragment_home_part_c;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView(View layout) {
        swipe_refresh_layout = layout.findViewById(R.id.swipe_refresh_layout);
        recycle_view = layout.findViewById(R.id.recycle_view);
        swipe_refresh_layout.setOnRefreshListener(this);
        swipe_refresh_layout.setColorSchemeResources(
                R.color.red,
                R.color.yellow,
                R.color.green
        );
        swipe_refresh_layout.setDistanceToTriggerSync(250);
        if (swipe_refresh_layout != null) {
            swipe_refresh_layout.setRefreshing(true);
        }
        swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefresh();
            }
        });
        swipe_refresh_layout.setEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view.setLayoutManager(linearLayoutManager);
        recycle_view.setHasFixedSize(true);
        mMvpHomeItemAdapter = new MvpHomeItemNoPaginationAdapter(MvpHomeItemNoPaginationAdapter.HOME_PART_C_VIEW_HOLDER);
        recycle_view.setAdapter(mMvpHomeItemAdapter);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadHomePartCListData();
    }

    @Override
    public void setHomePartCListAdapter(List<MvpHomePartCEntity> data) {
        mMvpHomeItemAdapter.setData(data);
        mMvpHomeItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSwipeRefreshLayoutRefresh(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        if (processing) {
            if (swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(processing);
            }
        } else {
            swipe_refresh_layout.setRefreshing(processing);
        }
    }
}
