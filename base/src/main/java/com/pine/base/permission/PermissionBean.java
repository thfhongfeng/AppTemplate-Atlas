package com.pine.base.permission;

import android.support.annotation.NonNull;
import android.support.annotation.Size;

/**
 * Created by tanghongfeng on 2019/2/27
 */

public class PermissionBean {
    private String[] perms;
    private int requestCode;
    private IPermissionCallback callback;
    private String rationaleTitle = null;
    private String rationaleContent = null;
    private String rationalePositiveBtnText = null;
    private String rationaleNegativeBtnText = null;
    private int rationaleTheme = -1;
    private String goSettingTitle = null;
    private String goSettingContent = null;
    private String goSettingPositiveBtnText = null;
    private String goSettingNegativeBtnText = null;
    private int goSettingTheme = -1;

    public PermissionBean(int requestCode, @Size(min = 1) @NonNull String... perms) {
        this.requestCode = requestCode;
        this.perms = perms;
    }

    public String[] getPerms() {
        return perms;
    }

    public void setPerms(String[] perms) {
        this.perms = perms;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public IPermissionCallback getCallback() {
        return callback;
    }

    public void setCallback(IPermissionCallback callback) {
        this.callback = callback;
    }

    public String getRationaleTitle() {
        return rationaleTitle;
    }

    public void setRationaleTitle(String rationaleTitle) {
        this.rationaleTitle = rationaleTitle;
    }

    public String getRationaleContent() {
        return rationaleContent;
    }

    public void setRationaleContent(String rationaleContent) {
        this.rationaleContent = rationaleContent;
    }

    public String getRationalePositiveBtnText() {
        return rationalePositiveBtnText;
    }

    public void setRationalePositiveBtnText(String rationalePositiveBtnText) {
        this.rationalePositiveBtnText = rationalePositiveBtnText;
    }

    public String getRationaleNegativeBtnText() {
        return rationaleNegativeBtnText;
    }

    public void setRationaleNegativeBtnText(String rationaleNegativeBtnText) {
        this.rationaleNegativeBtnText = rationaleNegativeBtnText;
    }

    public int getRationaleTheme() {
        return rationaleTheme;
    }

    public void setRationaleTheme(int rationaleTheme) {
        this.rationaleTheme = rationaleTheme;
    }

    public String getGoSettingTitle() {
        return goSettingTitle;
    }

    public void setGoSettingTitle(String goSettingTitle) {
        this.goSettingTitle = goSettingTitle;
    }

    public String getGoSettingContent() {
        return goSettingContent;
    }

    public void setGoSettingContent(String goSettingContent) {
        this.goSettingContent = goSettingContent;
    }

    public String getGoSettingPositiveBtnText() {
        return goSettingPositiveBtnText;
    }

    public void setGoSettingPositiveBtnText(String goSettingPositiveBtnText) {
        this.goSettingPositiveBtnText = goSettingPositiveBtnText;
    }

    public String getGoSettingNegativeBtnText() {
        return goSettingNegativeBtnText;
    }

    public void setGoSettingNegativeBtnText(String goSettingNegativeBtnText) {
        this.goSettingNegativeBtnText = goSettingNegativeBtnText;
    }

    public int getGoSettingTheme() {
        return goSettingTheme;
    }

    public void setGoSettingTheme(int goSettingTheme) {
        this.goSettingTheme = goSettingTheme;
    }
}
