package com.pine.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.pine.base.access.UiAccessManager;
import com.pine.base.permission.AppSettingsDialog;
import com.pine.base.permission.AppSettingsDialogHolderActivity;
import com.pine.base.permission.EasyPermissions;
import com.pine.tool.util.LogUtils;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String[] permissions = data.getStringArrayExtra(AppSettingsDialogHolderActivity.REQUEST_PERMISSIONS_KEY);
            if (!EasyPermissions.hasPermissions(this, permissions)) {
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtils.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogUtils.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        String[] permArr = new String[perms.size()];
        for (int i = 0; i < perms.size(); i++) {
            permArr[i] = perms.get(i);
        }
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build(permArr).show();
        } else {
            finish();
        }
    }

    @Override
    public void onRationaleAccepted(int requestCode) {
        LogUtils.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        LogUtils.d(TAG, "onRationaleDenied:" + requestCode);
        finish();
    }
}
