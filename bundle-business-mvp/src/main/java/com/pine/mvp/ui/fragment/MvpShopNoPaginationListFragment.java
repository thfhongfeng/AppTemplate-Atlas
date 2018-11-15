package com.pine.mvp.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.pine.base.architecture.mvp.ui.fragment.BaseMvpFragment;
import com.pine.mvp.R;
import com.pine.mvp.contract.IMvpShopNoPaginationListContract;
import com.pine.mvp.presenter.MvpShopNoPaginationListPresenter;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public class MvpShopNoPaginationListFragment extends BaseMvpFragment<IMvpShopNoPaginationListContract.Ui, MvpShopNoPaginationListPresenter>
        implements IMvpShopNoPaginationListContract.Ui, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView recycle_view;

    @Override
    protected MvpShopNoPaginationListPresenter createPresenter() {
        return new MvpShopNoPaginationListPresenter();
    }

    @Override
    protected int getFragmentLayoutResId() {
        return R.layout.mvp_fragment_shop_no_pagination_list;
    }

    @Override
    protected void findViewOnCreateView(View layout) {
        swipe_refresh_layout = layout.findViewById(R.id.swipe_refresh_layout);
        recycle_view = layout.findViewById(R.id.recycle_view);
    }

    @Override
    protected void initOnCreateView() {
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
        swipe_refresh_layout.setEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view.setLayoutManager(linearLayoutManager);
        recycle_view.setHasFixedSize(true);
        recycle_view.setAdapter(mPresenter.getListAdapter());

        swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    public void onRefresh() {
        mPresenter.loadShopNoPaginationListData();
    }

    @Override
    public void setSwipeRefreshLayoutRefresh(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        if (processing) {
            if (!swipe_refresh_layout.isRefreshing()) {
                swipe_refresh_layout.setRefreshing(processing);
            }
        } else {
            swipe_refresh_layout.setRefreshing(processing);
        }
    }
}
