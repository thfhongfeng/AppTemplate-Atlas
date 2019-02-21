package com.pine.mvvm.remote.atlas;

import android.os.Bundle;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterMvvmCommand;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvvmBundleRemoteService {

    @RouterAnnotation(CommandName = RouterMvvmCommand.goMvvmHomeActivity)
    public Bundle goBusinessHomeActivity(Bundle args) {
        Bundle responseBundle = new Bundle();
        Toast.makeText(BaseApplication.mCurResumedActivity, "暂无内容", Toast.LENGTH_SHORT).show();
        return responseBundle;
    }
}
