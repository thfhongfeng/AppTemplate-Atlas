package com.pine.router.impl.atlas.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

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
    protected Intent getRemoteIntent() {
        return new Intent("atlas.transaction.intent.action.business.MvcBundleRemoteAction");
    }

    @Override
    protected void onCommandFail(String commandType, Context context, int failCode, String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
