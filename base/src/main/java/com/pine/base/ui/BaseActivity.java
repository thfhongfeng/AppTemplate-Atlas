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

        if (beforeInitOnCreate()) {
            return;
        }

        if (initDataOnCreate()) {
            return;
        }
        initViewOnCreate();

        afterInitOnCreate();
    }

    protected void setContentView() {
        setContentView(getActivityLayoutResId());
    }

    /**
     * onCreate中获取当前Activity的内容布局资源id
     *
     * @return Activity的内容布局资源id
     */
    protected abstract int getActivityLayoutResId();

    /**
     * onCreate中前置初始化
     *
     * @return false:没有消耗掉(不中断onCreate后续流程并finish)
     * true:消耗掉了(中断onCreate后续流程并finish)
     */
    protected boolean beforeInitOnCreate() {
        return false;
    }

    /**
     * onCreate中初始化数据
     *
     * @return false:没有消耗掉(不中断onCreate后续流程)
     * true:消耗掉了(中断onCreate后续流程)
     */
    protected abstract boolean initDataOnCreate();

    /**
     * onCreate中初始化View
     */
    protected abstract void initViewOnCreate();

    /**
     * onCreate中结束初始化
     */
    protected abstract void afterInitOnCreate();
}
