package com.pine.config.switcher;

import org.json.JSONObject;

/**
 * Created by tanghongfeng on 2019/2/18
 */

public abstract class ConfigAbsFuncSwitcher {
    protected JSONObject mConfigData;

    public void setConfig(JSONObject configData) {
        mConfigData = configData;
    }

    public abstract boolean canAddProduct();

    public boolean canAddTravelNote() {
        return true;
    }
}
