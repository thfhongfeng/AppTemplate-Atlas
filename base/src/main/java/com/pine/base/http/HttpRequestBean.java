package com.pine.base.http;

import com.pine.base.http.IHttpRequestManager.ActionType;
import com.pine.base.http.IHttpRequestManager.RequestType;
import com.pine.base.http.callback.HttpAbstractBaseCallback;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class HttpRequestBean {
    private String url;
    private HttpRequestMethod requestMethod;
    private Map<String, String> params;
    //模块标识，默认common
    private String moduleTag = "common";
    private int what;
    private Object sign;
    private boolean needLogin;
    private RequestType requestType;
    private HttpAbstractBaseCallback callBack;

    // for download
    private String fileFolder;
    private String fileName;
    private boolean isContinue;
    private boolean isDeleteOld;

    // for upload
    private String fileKey;
    private List<String> fileNameList;
    private List<File> fileList;

    private ActionType actionType = ActionType.COMMON;

    private HttpResponse response;

    public HttpRequestBean(int what, HttpAbstractBaseCallback callBack) {
        this.what = what;
        this.callBack = callBack;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpRequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(HttpRequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getModuleTag() {
        return moduleTag;
    }

    public void setModuleTag(String moduleTag) {
        this.moduleTag = moduleTag;
    }

    public int getWhat() {
        return what;
    }

    public void setWhat(int what) {
        this.what = what;
    }

    public Object getSign() {
        return sign;
    }

    public void setSign(Object sign) {
        this.sign = sign;
    }

    public boolean isNeedLogin() {
        return needLogin;
    }

    public void setNeedLogin(boolean needLogin) {
        this.needLogin = needLogin;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    protected void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public HttpAbstractBaseCallback getCallBack() {
        return callBack;
    }

    public void setCallBack(HttpAbstractBaseCallback callBack) {
        this.callBack = callBack;
    }

    public ActionType getActionType() {
        return actionType;
    }

    protected void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public HttpResponse getResponse() {
        return response;
    }

    protected void setResponse(HttpResponse response) {
        this.response = response;
    }

    public String getFileFolder() {
        return fileFolder;
    }

    public void setFileFolder(String fileFolder) {
        this.fileFolder = fileFolder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isContinue() {
        return isContinue;
    }

    public void setContinue(boolean aContinue) {
        isContinue = aContinue;
    }

    public boolean isDeleteOld() {
        return isDeleteOld;
    }

    public void setDeleteOld(boolean deleteOld) {
        isDeleteOld = deleteOld;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public List<File> getFileList() {
        return fileList;
    }

    public void setFileList(List<File> fileList) {
        this.fileList = fileList;
    }

    public List<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(List<String> fileNameList) {
        this.fileNameList = fileNameList;
    }
}
