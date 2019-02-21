package com.pine.router;

import android.os.Bundle;

/**
 * Created by tanghongfeng on 2018/9/12
 */

public interface IRouterCallback {
    void onSuccess(Bundle responseBundle);

    /**
     * @param errorInfo
     * @return true-跳过默认处理，只执行用户的处理；false-不跳过默认处理，先执行默认处理，再执行用户的处理
     */
    boolean onFail(int code, String errorInfo);
}
