package com.pine.base.permission;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Size;

import com.pine.base.R;
import com.pine.base.permission.easy.AppSettingsDialog;
import com.pine.base.permission.easy.EasyPermissions;
import com.pine.base.permission.easy.PermissionRequest;
import com.pine.base.ui.BaseActivity;
import com.pine.base.ui.BaseFragment;

import java.util.Arrays;

/**
 * Created by tanghongfeng on 2019/2/28
 */

public class PermissionManager {
    public static boolean hasPermissions(@NonNull Context context,
                                         @Size(min = 1) @NonNull String... perms) {
        return EasyPermissions.hasPermissions(context, perms);
    }

    public static void requestPermission(@NonNull BaseActivity activity, int requestCode,
                                         IPermissionCallback callback,
                                         @Size(min = 1) @NonNull String... perms) {
        PermissionBean bean = new PermissionBean(requestCode, perms);
        bean.setRationaleContent(activity.getString(R.string.base_rationale_need));
        bean.setCallback(callback);
        requestPermission(activity, bean);
    }

    public static void requestPermission(@NonNull BaseActivity activity, PermissionBean bean) {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(activity, bean.getRequestCode(), bean.getPerms())
                        .setRationale(bean.getRationaleContent())
                        .setPositiveButtonText(bean.getRationalePositiveBtnText())
                        .setNegativeButtonText(bean.getRationaleNegativeBtnText())
                        .setTheme(bean.getRationaleTheme())
                        .build());
        activity.getPermissionRequestMap().put(bean.getRequestCode(), bean);
    }

    public static void requestPermission(@NonNull BaseFragment fragment, int requestCode,
                                         IPermissionCallback callback,
                                         @Size(min = 1) @NonNull String... perms) {
        PermissionBean bean = new PermissionBean(requestCode, perms);
        bean.setRationaleContent(fragment.getString(R.string.base_rationale_need));
        bean.setCallback(callback);
        requestPermission(fragment, bean);
    }

    public static void requestPermission(@NonNull BaseFragment fragment, PermissionBean bean) {
        EasyPermissions.requestPermissions(
                new PermissionRequest.Builder(fragment, bean.getRequestCode(), bean.getPerms())
                        .setRationale(bean.getRationaleContent())
                        .setPositiveButtonText(bean.getRationalePositiveBtnText())
                        .setNegativeButtonText(bean.getRationaleNegativeBtnText())
                        .setTheme(bean.getRationaleTheme())
                        .build());
        fragment.getPermissionRequestMap().put(bean.getRequestCode(), bean);
    }

    public static boolean showGoAppSettingsDialog(@NonNull BaseActivity activity,
                                                  int requestCode,
                                                  PermissionBean bean,
                                                  @NonNull String... perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(activity, Arrays.asList(perms))) {
            new AppSettingsDialog.Builder(activity)
                    .setRationale(bean != null && bean.getGoSettingContent() != null ?
                            bean.getGoSettingContent() : getDefaultGoSettingContent(perms))
                    .setPermRequestCode(requestCode)
                    .build(perms).show();
            return true;
        }
        return false;
    }

    public static boolean showGoAppSettingsDialog(@NonNull BaseFragment fragment,
                                                  int requestCode,
                                                  PermissionBean bean,
                                                  @NonNull String... perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(fragment, Arrays.asList(perms))) {
            new AppSettingsDialog.Builder(fragment)
                    .setRationale(bean != null && bean.getGoSettingContent() != null ?
                            bean.getGoSettingContent() : getDefaultGoSettingContent(perms))
                    .setPermRequestCode(requestCode)
                    .build(perms).show();
            return true;
        }
        return false;
    }

    private static String getDefaultGoSettingContent(@NonNull String... perms) {
        String rational = "没有授予" + PermissionTranslate.translate(perms) +
                "等权限，应用可能无法正常运行。请打开应用设置允许相应权限。";
        return rational;
    }
}
