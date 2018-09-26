package com.pine.router;

import android.os.Bundle;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public interface IRouterCallback {
    void onSuccess(Bundle returnBundle);
    void onException(Bundle returnBundle);

    void onFail(String errorInfo);
}
