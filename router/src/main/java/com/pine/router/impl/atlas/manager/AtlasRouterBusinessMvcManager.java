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

public class AtlasRouterBusinessMvcManager extends AtlasRouterManager {
    private static volatile AtlasRouterBusinessMvcManager mInstance;

    private AtlasRouterBusinessMvcManager() {
    }

    public static AtlasRouterBusinessMvcManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterBusinessMvcManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterBusinessMvcManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    public String getBundleKey() {
        return ConfigBundleKey.BUSINESS_MVC_BUNDLE_KEY;
    }

    @Override
    protected Intent getRemoteIntent(String commandType) {
        switch (commandType) {
            case TYPE_UI_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.MvcUiRemoteAction");
            case TYPE_DATA_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.MvcDataRemoteAction");
            case TYPE_OP_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.MvcOpRemoteAction");
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
