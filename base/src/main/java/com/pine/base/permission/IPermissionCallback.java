package com.pine.base.permission;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by tanghongfeng on 2019/2/27
 */

public interface IPermissionCallback {
    void onPermissionsGranted(int requestCode, @NonNull List<String> perms);

    void onPermissionsDenied(int requestCode, @NonNull List<String> perms);

    void onRationaleAccepted(int requestCode);

    void onRationaleDenied(int requestCode);

    void onAllPermissionGranted(int requestCode);
}
