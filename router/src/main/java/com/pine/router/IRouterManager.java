package com.pine.router;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public interface IRouterManager {

    void callUiCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback);

    void callDataCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback);

    void callOpCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback);
}
