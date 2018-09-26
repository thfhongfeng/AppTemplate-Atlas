package com.pine.base.http.callback;

import com.pine.base.http.HttpResponse;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpAbstractBaseCallback {
    private String urlTag;

    private String moduleTag = "common"; //模块标识，默认common，各自模块只能接自己的请求回调

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getUrlTag() {
        return urlTag;
    }

    public void setUrlTag(String urlTag) {
        this.urlTag = urlTag;
    }
}
