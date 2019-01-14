package com.pine.router.atlas;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class AtlasRouterLoginManager extends AtlasRouterManager {
    private static volatile AtlasRouterLoginManager mInstance;

    private AtlasRouterLoginManager() {
    }

    public static AtlasRouterLoginManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterLoginManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterLoginManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected Intent getRemoteIntent() {
        return new Intent("atlas.transaction.intent.action.login.LoginBundleRemoteAction");
    }

    @Override
    protected void onCommandFail(String commandType, Context context, int failCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
