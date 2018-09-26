package com.pine.welcome.bean;

/**
 * Created by tanghongfeng on 2018/9/25
 */

public class VersionEntity {

    /**
     * packageName : com.pine.template
     * versionCode : 2
     * versionName : 1.0.2
     * minSupportedVersion : 1
     * force : true
     * fileName : template.apk
     * path : https://www.baidu.com
     */

    private String packageName;
    private int versionCode;
    private String versionName;
    private int minSupportedVersion;
    private boolean force;
    private String fileName;
    private String path;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getMinSupportedVersion() {
        return minSupportedVersion;
    }

    public void setMinSupportedVersion(int minSupportedVersion) {
        this.minSupportedVersion = minSupportedVersion;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
