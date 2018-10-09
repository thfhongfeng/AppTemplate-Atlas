package com.pine.base.http.callback;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpAbstractBaseCallback {
    // 该callback对应的http请求的key
    private String key;
    // 该callback对应的http请求标识code
    private int what;
    // 该callback对应的http请求的url
    private String url;
    //模块标识，默认common
    private String moduleTag = "common";

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public String getKey() {
        return key;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
        this.key = hashCode() + "_ " + what;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
