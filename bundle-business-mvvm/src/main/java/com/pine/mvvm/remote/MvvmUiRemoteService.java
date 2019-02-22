package com.pine.mvvm.remote;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.router.IServiceCallback;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterMvvmCommand;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvvmUiRemoteService {

    @RouterAnnotation(CommandName = RouterMvvmCommand.goMvvmHomeActivity)
    public void goBusinessHomeActivity(Bundle args, @NonNull final IServiceCallback callback) {
        Bundle responseBundle = new Bundle();
        Toast.makeText(BaseApplication.mCurResumedActivity, "暂无内容", Toast.LENGTH_SHORT).show();
        callback.onResponse(responseBundle);
    }
}
