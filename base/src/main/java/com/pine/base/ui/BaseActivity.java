package com.pine.base.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.pine.base.access.UiAccessManager;
import com.pine.tool.util.LogUtils;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = LogUtils.makeLogTag(this.getClass());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        if (!UiAccessManager.getInstance().checkCanAccess(this)) {
            finish();
            return;
        }

        if (beforeInit()) {
            return;
        }

        if (initData()) {
            return;
        }
        initView();

        afterInit();
    }

    /**
     * 前置初始化
     *
     * @return false:没有消耗掉
     * true:消耗掉了
     */
    protected boolean beforeInit() {
        return false;
    }

    protected void setContentView() {
        setContentView(getActivityLayoutResId());
    }

    protected abstract int getActivityLayoutResId();

    /**
     * 初始化数据
     *
     * @return false:没有消耗掉
     * true:消耗掉了
     */
    protected abstract boolean initData();

    protected abstract void initView();

    protected abstract void afterInit();
}
