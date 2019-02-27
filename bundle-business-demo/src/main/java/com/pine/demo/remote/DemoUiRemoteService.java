package com.pine.demo.remote;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.pine.demo.DemoHomeActivity;
import com.pine.router.IServiceCallback;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterDemoCommand;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class DemoUiRemoteService {

    @RouterAnnotation(CommandName = RouterDemoCommand.goDemoHomeActivity)
    public void goBusinessHomeActivity(@NonNull Activity activity, Bundle args, @NonNull final IServiceCallback callback) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(activity, DemoHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        callback.onResponse(responseBundle);
    }
}
