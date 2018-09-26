package com.pine.base.http.callback;

import com.pine.base.http.HttpResponse;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpStringCallback extends HttpAbstractBaseCallback {

    public void onResponse(int what, HttpResponse response) {
        String res = (String) response.getData();
        onResponse(what, res);
    }

    public abstract void onResponse(int what, String res);

    public abstract boolean onError(int what, Exception exception);
}
