package com.pine.main.remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pine.main.ui.activity.MainHomeActivity;
import com.pine.router.IServiceCallback;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterMainCommand;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainUiRemoteService {

    @RouterAnnotation(CommandName = RouterMainCommand.goMainHomeActivity)
    public void goMainHomeActivity(@NonNull Activity activity, Bundle args, @NonNull final IServiceCallback callback) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(activity, MainHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        callback.onResponse(responseBundle);
    }
}
