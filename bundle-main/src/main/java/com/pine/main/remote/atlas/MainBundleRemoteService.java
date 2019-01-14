package com.pine.main.remote.atlas;

import android.content.Intent;
import android.os.Bundle;

import com.pine.main.ui.activity.MainHomeActivity;
import com.pine.router.RouterCommand;
import com.pine.router.annotation.RouterAnnotation;
import com.pine.tool.util.AppUtils;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MainBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.MAIN_goMainHomeActivity)
    public Bundle goMainHomeActivity(Bundle args) {
        Bundle responseBundle = new Bundle();
        Intent intent = new Intent(AppUtils.getApplicationByReflect(), MainHomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppUtils.getApplicationByReflect().startActivity(intent);
        return responseBundle;
    }
}
