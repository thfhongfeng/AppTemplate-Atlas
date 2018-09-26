package com.pine.a.remote.atlas;

import android.content.Intent;
import android.os.Bundle;

import com.pine.a.ui.activity.BusinessAHomeActivity;
import com.pine.router.RouterCommand;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class BusinessABundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.BUSINESS_goBusinessAHomeActivity)
    public Bundle goBusinessAHomeActivity(Bundle args) {
        Bundle returnBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), BusinessAHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return returnBundle;
    }
}
