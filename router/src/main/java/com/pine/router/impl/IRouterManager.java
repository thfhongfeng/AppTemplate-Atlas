package com.pine.router.impl;

import android.app.Activity;
import android.os.Bundle;

import com.pine.router.IRouterCallback;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public interface IRouterManager {
    int FAIL_CODE_INVALID = 1001; // 无效的请求，比如请求的模块没有权限等。
    int FAIL_CODE_LOST = 1002; // 请求丢失，比如对应模块并没有开放对应的服务接口。
    int FAIL_CODE_INTERRUPT = 1003; // 请求被打断。
    int FAIL_CODE_ERROR = 1004; // 请求返回出错，被请求的服务出错。

    void callUiCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback);

    void callDataCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback);

    void callOpCommand(Activity activity, final String commandName, final Bundle args, final IRouterCallback callback);
}
