package com.pine.router.impl.atlas.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.pine.config.ConfigBundleKey;

import static com.pine.router.RouterConstants.TYPE_DATA_COMMAND;
import static com.pine.router.RouterConstants.TYPE_OP_COMMAND;
import static com.pine.router.RouterConstants.TYPE_UI_COMMAND;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class AtlasRouterBusinessDemoManager extends AtlasRouterManager {
    private static volatile AtlasRouterBusinessDemoManager mInstance;

    private AtlasRouterBusinessDemoManager() {
    }

    public static AtlasRouterBusinessDemoManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterBusinessDemoManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterBusinessDemoManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String getBundleKey() {
        return ConfigBundleKey.BUSINESS_DEMO_BUNDLE_KEY;
    }

    @Override
    protected Intent getRemoteIntent(String commandType) {
        switch (commandType) {
            case TYPE_UI_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.DemoUiRemoteAction");
            case TYPE_DATA_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.DemoDataRemoteAction");
            case TYPE_OP_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.DemoOpRemoteAction");
        }
        return null;
    }

    @Override
    protected void onCommandFail(String commandType, Context context, int failCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
