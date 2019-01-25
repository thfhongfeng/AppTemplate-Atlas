package com.pine.mvvm.remote.atlas;

import android.os.Bundle;
import android.widget.Toast;

import com.pine.base.BaseApplication;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterCommand;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvvmBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.MVVM_goMvvmHomeActivity)
    public Bundle goBusinessHomeActivity(Bundle args) {
        Bundle responseBundle = new Bundle();
        Toast.makeText(BaseApplication.mCurResumedActivity, "暂无内容", Toast.LENGTH_SHORT);
        return responseBundle;
    }
}
