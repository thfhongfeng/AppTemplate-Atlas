package com.pine.mvc.remote.atlas;

import android.os.Bundle;

import com.pine.router.RouterCommand;
import com.pine.router.annotation.RouterAnnotation;

/**
 * Created by tanghongfeng on 2018/9/13
 */

public class MvcBundleRemoteService {

    @RouterAnnotation(CommandName = RouterCommand.BUSINESS_goBusinessMvcHomeActivity)
    public Bundle goBusinessMvcHomeActivity(Bundle args) {
        Bundle returnBundle = new Bundle();
        return returnBundle;
    }
}
