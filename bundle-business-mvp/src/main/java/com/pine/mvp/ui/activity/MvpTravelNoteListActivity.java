package com.pine.mvp.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarCustomMenuActivity;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpShopItemPaginationAdapter;
import com.pine.mvp.contract.IMvpTravelNoteListContract;
import com.pine.mvp.presenter.MvpTravelNoteListPresenter;

/**
 * Created by tanghongfeng on 2018/10/22
 */

public class MvpTravelNoteListActivity extends BaseMvpActionBarCustomMenuActivity<IMvpTravelNoteListContract.Ui, MvpTravelNoteListPresenter>
        implements IMvpTravelNoteListContract.Ui, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView recycle_view;

    @Override
    protected MvpTravelNoteListPresenter createPresenter() {
        return new MvpTravelNoteListPresenter();
    }

    @Override
    protected int getMenuBarLayoutResId() {
        return R.layout.mvp_travel_note_list_menu;
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_travel_note_list;
    }

    @Override
    protected void findViewOnCreate() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        recycle_view = findViewById(R.id.recycle_view);
    }

    @Override
    protected void initOnCreate() {
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycle_view.setLayoutManager(linearLayoutManager);
        recycle_view.setHasFixedSize(true);
        recycle_view.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (MvpShopItemPaginationAdapter.isLastVisibleViewFooter(recyclerView)) {
                    onLoadingMore();
                }
            }
        });
        recycle_view.setAdapter(mPresenter.getRecycleViewAdapter());

        swipe_refresh_layout.post(new Runnable() {
            @Override
            public void run() {
                swipe_refresh_layout.setRefreshing(true);
                onRefresh();
            }
        });
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, View menuContainer) {
        titleTv.setText(R.string.mvp_travel_note_list_title);
        goBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                return;
            }
        });
    }

    @Override
    public void onRefresh() {
        mPresenter.loadTravelNoteListData(true);
    }

    public void onLoadingMore() {
        mPresenter.loadTravelNoteListData(false);
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
