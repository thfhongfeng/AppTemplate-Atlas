package com.pine.mvp.remote.atlas;

import android.content.Intent;
import android.os.Bundle;

import com.pine.mvp.ui.activity.MvpHomeActivity;
import com.pine.router.RouterCommand;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvpBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.BUSINESS_goBusinessMvpHomeActivity)
    public Bundle goBusinessMvpHomeActivity(Bundle args) {
        Bundle returnBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), MvpHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return returnBundle;
    }
}
