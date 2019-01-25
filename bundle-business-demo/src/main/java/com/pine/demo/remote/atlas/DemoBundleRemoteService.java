package com.pine.demo.remote.atlas;

import android.content.Intent;
import android.os.Bundle;

import com.pine.demo.DemoHomeActivity;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.router.command.RouterCommand;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class DemoBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.DEMO_goDemoHomeActivity)
    public Bundle goBusinessHomeActivity(Bundle args) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), DemoHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return responseBundle;
    }
}
