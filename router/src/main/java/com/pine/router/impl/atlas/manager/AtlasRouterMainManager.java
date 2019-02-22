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
    protected Intent getRemoteIntent(String commandType) {
        switch (commandType) {
            case TYPE_UI_COMMAND:
                return new Intent("atlas.transaction.intent.action.main.MainUiRemoteAction");
            case TYPE_DATA_COMMAND:
                return new Intent("atlas.transaction.intent.action.main.MainDataRemoteAction");
            case TYPE_OP_COMMAND:
                return new Intent("atlas.transaction.intent.action.main.MainOpRemoteAction");
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
