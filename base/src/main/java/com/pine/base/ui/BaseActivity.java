package com.pine.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.pine.base.R;
import com.pine.base.access.UiAccessManager;
import com.pine.base.permission.AfterPermissionGranted;
import com.pine.base.permission.AppSettingsDialog;
import com.pine.base.permission.AppSettingsDialogHolderActivity;
import com.pine.base.permission.EasyPermissions;
import com.pine.base.permission.PermissionsAnnotation;
import com.pine.tool.util.LogUtils;

import java.util.List;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public final int REQUEST_PERMISSION = 33333;
    protected final String TAG = LogUtils.makeLogTag(this.getClass());
    private boolean mUiAccessReady, mPermissionReady;
    private boolean onAllAccessRestrictionReleasedMethodCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitOnCreate();
        setContentView();

        findViewOnCreate();

        mUiAccessReady = true;
        if (!UiAccessManager.getInstance().checkCanAccess(this)) {
            mUiAccessReady = false;
            finish();
            return;
        }

        mPermissionReady = true;
        PermissionsAnnotation annotation = getClass().getAnnotation(PermissionsAnnotation.class);
        if (annotation != null) {
            String[] permissions = annotation.Permissions();
            if (permissions != null) {
                if (!EasyPermissions.hasPermissions(this, permissions)) {
                    mPermissionReady = false;
                    EasyPermissions.requestPermissions(
                            this,
                            getString(R.string.base_rationale_need),
                            REQUEST_PERMISSION, permissions);
                }
            }
        }

        tryOnAllRestrictionReleased();
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
     */
    protected void beforeInitOnCreate() {

    }

    /**
     * onCreate中初始化View
     */
    protected abstract void findViewOnCreate();

    private void onAllAccessRestrictionReleased() {
        if (!parseIntentDataOnCreate()) {
            initOnCreate();
            afterInitOnCreate();
        }
    }

    protected abstract boolean parseIntentDataOnCreate();

    /**
     * 所有准入条件(如：登陆限制，权限限制等)全部解除后回调（界面的数据业务初始化动作推荐在此进行）
     */
    protected abstract void initOnCreate();

    /**
     * onCreate中结束初始化
     */
    protected abstract void afterInitOnCreate();

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String[] permissions = data.getStringArrayExtra(AppSettingsDialogHolderActivity.REQUEST_PERMISSIONS_KEY);
            if (!EasyPermissions.hasPermissions(this, permissions)) {
                finish();
            } else {
                mPermissionReady = true;
                tryOnAllRestrictionReleased();
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

    @AfterPermissionGranted(REQUEST_PERMISSION)
    public final void afterAllPermissionGranted() {
        mPermissionReady = true;
        tryOnAllRestrictionReleased();
    }

    private void tryOnAllRestrictionReleased() {
        if (!onAllAccessRestrictionReleasedMethodCalled &&
                mUiAccessReady && mPermissionReady) {
            onAllAccessRestrictionReleasedMethodCalled = true;
            onAllAccessRestrictionReleased();
        }
    }
}
