package com.pine.router.impl.atlas.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import static com.pine.router.RouterConstants.TYPE_DATA_COMMAND;
import static com.pine.router.RouterConstants.TYPE_OP_COMMAND;
import static com.pine.router.RouterConstants.TYPE_UI_COMMAND;

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
    protected Intent getRemoteIntent(String commandType) {
        switch (commandType) {
            case TYPE_UI_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.MvvmUiRemoteAction");
            case TYPE_DATA_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.MvvmDataRemoteAction");
            case TYPE_OP_COMMAND:
                return new Intent("atlas.transaction.intent.action.business.MvvmOpRemoteAction");
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
