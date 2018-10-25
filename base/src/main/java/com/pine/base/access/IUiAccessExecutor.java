package com.pine.base.access;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public interface IUiAccessExecutor {
    boolean onExecute(Activity activity, String args);

    boolean onExecute(Fragment fragment, String args);
}
