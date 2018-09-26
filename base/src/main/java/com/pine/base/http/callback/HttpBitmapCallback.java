package com.pine.base.http.callback;

import android.graphics.Bitmap;

import com.pine.base.http.HttpResponse;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpBitmapCallback extends HttpAbstractBaseCallback {

    public void onResponse(int what, HttpResponse response) {
        Bitmap bitmap = (Bitmap) response.getData();
        onResponse(what, bitmap);
    }

    public abstract void onResponse(int what, Bitmap bitmap);

    public abstract boolean onError(int what, Exception exception);

}
