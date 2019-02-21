package com.pine.router.impl.atlas.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public class AtlasRouterBusinessMvpManager extends AtlasRouterManager {
    private static volatile AtlasRouterBusinessMvpManager mInstance;

    private AtlasRouterBusinessMvpManager() {
    }

    public static AtlasRouterBusinessMvpManager getInstance() {
        if (mInstance == null) {
            synchronized (AtlasRouterBusinessMvpManager.class) {
                if (mInstance == null) {
                    mInstance = new AtlasRouterBusinessMvpManager();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected Intent getRemoteIntent() {
        return new Intent("atlas.transaction.intent.action.business.MvpBundleRemoteAction");
    }

    @Override
    protected void onCommandFail(String commandType, Context context, int failCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
