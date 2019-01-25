package com.pine.router.manager.atlas;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class AtlasRouterBusinessMvvmManager extends AtlasRouterManager {
    private static volatile AtlasRouterBusinessMvvmManager mInstance;

    private AtlasRouterBusinessMvvmManager() {
    }

    public static AtlasRouterBusinessMvvmManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterBusinessMvvmManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterBusinessMvvmManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected Intent getRemoteIntent() {
        return new Intent("atlas.transaction.intent.action.business.MvvmBundleRemoteAction");
    }

    @Override
    protected void onCommandFail(String commandType, Context context, int failCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
