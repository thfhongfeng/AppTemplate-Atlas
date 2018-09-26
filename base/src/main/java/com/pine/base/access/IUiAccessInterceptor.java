package com.pine.base.access;

import android.content.Context;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public interface IUiAccessInterceptor {
    boolean onInterceptor(Context context, int level);
}
