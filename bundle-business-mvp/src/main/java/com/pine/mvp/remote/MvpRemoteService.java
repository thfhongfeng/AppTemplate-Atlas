package com.pine.mvp.remote;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pine.mvp.ui.activity.MvpHomeActivity;
import com.pine.router.IServiceCallback;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterMvpCommand;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpRemoteService {

    @RouterAnnotation(CommandName = RouterMvpCommand.goMvpHomeActivity)
    public void goBusinessHomeActivity(Bundle args, @NonNull final IServiceCallback callback) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), MvpHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        callback.onResponse(responseBundle);
    }
}
