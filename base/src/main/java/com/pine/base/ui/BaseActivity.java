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
import com.pine.base.widget.ILifeCircleView;
import com.pine.tool.util.LogUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/28
 */

public abstract class BaseActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks {
    public final int REQUEST_PERMISSION = 33333;
    protected final String TAG = LogUtils.makeLogTag(this.getClass());
    private boolean mUiAccessReady, mPermissionReady;
    private boolean onAllAccessRestrictionReleasedMethodCalled;
    private boolean mPrePause;
    private Map<Integer, ILifeCircleView> mLifeCircleViewMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInitOnCreate();
        setContentView();

        findViewOnCreate();

        mUiAccessReady = true;
        if (!UiAccessManager.getInstance().checkCanAccess(this, false)) {
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
        if (!parseIntentData()) {
            init();
            afterInit();
        }
    }

    /**
     * 用于分析传入参数是否非法
     *
     * @return true表示非法， false表示合法
     */
    protected abstract boolean parseIntentData();

    /**
     * 所有准入条件(如：登陆限制，权限限制等)全部解除后回调（界面的数据业务初始化动作推荐在此进行）
     */
    protected abstract void init();

    /**
     * onCreate中结束初始化
     */
    protected abstract void afterInit();

    @CallSuper
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mPrePause) {
            mPrePause = false;
            if (!UiAccessManager.getInstance().checkCanAccess(this, false)) {
                return;
            } else {
                tryOnAllRestrictionReleased();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPrePause) {
            mPrePause = false;
            if (!UiAccessManager.getInstance().checkCanAccess(this, true)) {
                return;
            } else {
                tryOnAllRestrictionReleased();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPrePause = true;
    }

    @Override
    protected void onDestroy() {
        if (mLifeCircleViewMap != null && mLifeCircleViewMap.size() > 0) {
            mLifeCircleViewMap.clear();
        }
        super.onDestroy();
    }

    @CallSuper
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult requestCode:" + requestCode +
                ", resultCode:" + resultCode);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            String[] permissions = data.getStringArrayExtra(AppSettingsDialogHolderActivity.REQUEST_PERMISSIONS_KEY);
            if (!EasyPermissions.hasPermissions(this, permissions)) {
                finish();
            } else {
                mPermissionReady = true;
                tryOnAllRestrictionReleased();
            }
        }

        if (mLifeCircleViewMap != null) {
            Iterator<Map.Entry<Integer, ILifeCircleView>> iterator = mLifeCircleViewMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, ILifeCircleView> entry = iterator.next();
                ILifeCircleView view = entry.getValue();
                if (view != null) {
                    view.onActivityResult(requestCode, resultCode, data);
                }
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

    public void attachCircleView(ILifeCircleView view) {
        mLifeCircleViewMap.put(view.hashCode(), view);
    }
}
