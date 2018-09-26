package com.pine.welcome.bean;

/**
 * Created by tanghongfeng on 2018/9/14
 */

public class BundleSwitcherEntity {

    /**
     * bundleKey : login_bundle
     * open : true
     */

    private String bundleKey;
    private boolean open;

    public String getBundleKey() {
        return bundleKey;
    }

    public void setBundleKey(String bundleKey) {
        this.bundleKey = bundleKey;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
