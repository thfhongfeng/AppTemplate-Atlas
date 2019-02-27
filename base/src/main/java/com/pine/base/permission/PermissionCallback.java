package com.pine.base.permission;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by tanghongfeng on 2019/2/27
 */

public abstract class PermissionCallback implements IPermissionCallback {

    @Override
    public final void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public final void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        onDenied();
    }

    @Override
    public final void onRationaleAccepted(int requestCode) {

    }

    @Override
    public final void onRationaleDenied(int requestCode) {

    }

    @Override
    public final void onAllPermissionGranted(int requestCode) {
        onGranted();
    }

    public abstract void onDenied();

    public abstract void onGranted();
}
