package com.pine.router.impl.atlas.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class AtlasRouterMainManager extends AtlasRouterManager {
    private static volatile AtlasRouterMainManager mInstance;

    private AtlasRouterMainManager() {
    }

    public static AtlasRouterMainManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterMainManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterMainManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected Intent getRemoteIntent() {
        return new Intent("atlas.transaction.intent.action.main.MainBundleRemoteAction");
    }

    @Override
    protected void onCommandFail(String commandType, Context context, int failCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
