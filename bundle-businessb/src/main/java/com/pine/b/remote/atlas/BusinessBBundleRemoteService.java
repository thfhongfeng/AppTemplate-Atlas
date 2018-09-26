package com.pine.b.remote.atlas;

import android.content.Intent;
import android.os.Bundle;

import com.pine.b.ui.activity.BusinessBHomeActivity;
import com.pine.router.RouterCommand;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class BusinessBBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.BUSINESS_goBusinessBHomeActivity)
    public Bundle goBusinessBHomeActivity(Bundle args) {
        Bundle returnBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), BusinessBHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return returnBundle;
    }
}
