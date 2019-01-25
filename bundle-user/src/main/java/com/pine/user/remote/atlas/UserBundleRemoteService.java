package com.pine.user.remote.atlas;

import android.content.Intent;
import android.os.Bundle;

import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterCommand;
import com.pine.tool.util.AppUtils;
import com.pine.user.ui.activity.UserHomeActivity;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class UserBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.USER_goUserHomeActivity)
    public Bundle goUserCenterActivity(Bundle args) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), UserHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return responseBundle;
    }
}
