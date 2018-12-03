package com.pine.mvp.ui.activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pine.base.architecture.mvp.bean.InputParamBean;
import com.pine.base.architecture.mvp.ui.activity.BaseMvpActionBarTextMenuActivity;
import com.pine.mvp.R;
import com.pine.mvp.adapter.MvpShopListPaginationAdapter;
import com.pine.mvp.contract.IMvpShopSearchCheckContract;
import com.pine.mvp.presenter.MvpShopSearchCheckPresenter;
import com.pine.tool.util.KeyboardUtils;

/**
 * Created by tanghongfeng on 2018/11/15
 */

public class MvpShopSearchCheckActivity extends
        BaseMvpActionBarTextMenuActivity<IMvpShopSearchCheckContract.Ui, MvpShopSearchCheckPresenter>
        implements IMvpShopSearchCheckContract.Ui,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout swipe_refresh_layout;
    private RecyclerView recycle_view;
    private ImageView search_iv, search_key_clear_iv;
    private EditText search_key_et;
    private TextView clear_check_tv;

    @Override
    protected MvpShopSearchCheckPresenter createPresenter() {
        return new MvpShopSearchCheckPresenter();
    }

    @Override
    protected void initActionBar(ImageView goBackIv, TextView titleTv, TextView menuBtnTv) {
        titleTv.setText(R.string.mvp_shop_check_title);
        menuBtnTv.setText(R.string.mvp_complete);
        menuBtnTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.completeAction();
            }
        });
    }

    @Override
    protected int getActivityLayoutResId() {
        return R.layout.mvp_activity_shop_search_check;
    }

    @Override
    protected void findViewOnCreate() {
        swipe_refresh_layout = findViewById(R.id.swipe_refresh_layout);
        recycle_view = findViewById(R.id.recycle_view);
        search_iv = findViewById(R.id.search_iv);
        search_key_clear_iv = findViewById(R.id.search_key_clear_iv);
        search_key_et = findViewById(R.id.search_key_et);
        clear_check_tv = findViewById(R.id.clear_check_tv);
    }

    @Override
    protected void init() {
        search_iv.setOnClickListener(this);
        search_key_clear_iv.setOnClickListener(this);
        clear_check_tv.setOnClickListener(this);
        search_key_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {
                    KeyboardUtils.closeSoftKeyboard(MvpShopSearchCheckActivity.this);
                    mPresenter.postSearch(true);
                    return true;
                }
                return false;
            }
        });

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
                if (MvpShopListPaginationAdapter.isLastViewMoreView(recyclerView)) {
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
    public void onRefresh() {
        mPresenter.postSearch(true);
    }

    public void onLoadingMore() {
        mPresenter.postSearch(false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_iv) {
            onRefresh();
        } else if (v.getId() == R.id.search_key_clear_iv) {
            search_key_et.setText("");
        } else if (v.getId() == R.id.clear_check_tv) {
            mPresenter.clearCurCheck();
        }
    }

    @Override
    public InputParamBean getSearchKey(String key) {
        return new InputParamBean(this, key, search_key_et.getText().toString());
    }

    @Override
    public void goAllSelectMode() {
        search_key_et.setText("");
        onRefresh();
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
