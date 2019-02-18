package com.pine.config.switcher;

/**
 * Created by tanghongfeng on 2019/2/18
 */

public class ConfigFuncSwitcher extends ConfigAbsFuncSwitcher {
    private static ConfigFuncSwitcher mInstance;

    private ConfigFuncSwitcher() {

    }

    public synchronized static ConfigFuncSwitcher getInstance() {
        if (mInstance == null) {
            mInstance = new ConfigFuncSwitcher();
        }
        return mInstance;
    }

    @Override
    public boolean canAddProduct() {
        return true;
    }
}
