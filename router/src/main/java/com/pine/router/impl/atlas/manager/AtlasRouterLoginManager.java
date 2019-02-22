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
    protected Intent getRemoteIntent(String commandType) {
        switch (commandType) {
            case TYPE_UI_COMMAND:
                return new Intent("atlas.transaction.intent.action.login.LoginUiRemoteAction");
            case TYPE_DATA_COMMAND:
                return new Intent("atlas.transaction.intent.action.login.LoginDataRemoteAction");
            case TYPE_OP_COMMAND:
                return new Intent("atlas.transaction.intent.action.login.LoginOpRemoteAction");
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
