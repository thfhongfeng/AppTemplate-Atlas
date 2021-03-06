package com.pine.mvp.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarActivity;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpTravelNoteDetailComplexAdapter;
import com.pine.mvp.contract.IMvpTravelNoteDetailContract;
import com.pine.mvp.presenter.MvpTravelNoteDetailPresenter;

/**
 * Created by tanghongfeng on 2018/10/9
 */

public class MvpTravelNoteDetailActivity extends BaseMvpActionBarActivity<IMvpTravelNoteDetailContract.Ui, MvpTravelNoteDetailPresenter>
        implements IMvpTravelNoteDetailContract.Ui, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView recycle_view;

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_travel_note_detail;
    }

    @Override
    protected void findViewOnCreate() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        recycle_view = findViewById(R.id.recycle_view);
    }

    @Override
    protected void init() {
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
                if (MvpTravelNoteDetailComplexAdapter.isLastViewMoreView(recyclerView)) {
                    onLoadingMore();
                }
            }
        });
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
    protected void setupActionBar(ImageView goBackIv, TextView titleTv) {
        titleTv.setText(R.string.mvp_travel_note_detail_title);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        mPresenter.loadTravelNoteDetailData();
    }

    public void onLoadingMore() {
        mPresenter.loadTravelNoteCommentData(false);
    }

    @Override
    public void setLoadingUiVisibility(boolean processing) {
        if (swipe_refresh_layout == null) {
            return;
        }
        swipe_refresh_layout.setRefreshing(processing);
    }
}
