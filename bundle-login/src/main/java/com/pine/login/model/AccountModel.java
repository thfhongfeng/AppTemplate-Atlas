package com.pine.login.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.base.http.HttpRequestManager;
import com.pine.base.http.callback.HttpJsonCallback;
import com.pine.login.LoginConstants;
import com.pine.login.LoginUrlConstants;
import com.pine.login.bean.AccountBean;
import com.pine.tool.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AccountModel {
    private final String TAG = LogUtils.makeLogTag(this.getClass());
    private static final int HTTP_REGISTER = 1;

    public void requestRegister(final HashMap<String, String> params,
                                @NonNull final IModelAsyncResponse<AccountBean> callback) {
        String url = LoginUrlConstants.Register_Account;
        HttpRequestManager.setJsonRequest(url, params, TAG, HTTP_REGISTER,
                handleHttpResponse(callback));
    }

    private <T> HttpJsonCallback handleHttpResponse(final IModelAsyncResponse<T> callback) {
        return new HttpJsonCallback() {
            @Override
            public void onResponse(int what, JSONObject jsonObject) {
                if (what == HTTP_REGISTER) {
                    // Test code begin
                    jsonObject = getRegisterAccountData();
                    // Test code end
                    if (jsonObject.optBoolean(LoginConstants.SUCCESS)) {
                        T retData = new Gson().fromJson(jsonObject.optString(LoginConstants.DATA), new TypeToken<AccountBean>() {
                        }.getType());
                        callback.onResponse(retData);
                    } else {
                        callback.onFail(new Exception(jsonObject.optString("message")));
                    }
                }
            }

            @Override
            public boolean onFail(int what, Exception e) {
                return callback.onFail(e);
            }

            @Override
            public void onCancel(int what) {
                callback.onCancel();
            }
        };
    }

    // Test code begin
    private JSONObject getRegisterAccountData() {
        String res = "{success:true,code:200,message:'',data:" +
                "{id:'" + 100 + "',mobile:'测试注册用户', pwd:'111aaa'}}";
        try {
            return new JSONObject(res);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
    // Test code end
}
