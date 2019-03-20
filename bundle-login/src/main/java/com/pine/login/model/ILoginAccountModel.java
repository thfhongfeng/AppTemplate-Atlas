package com.pine.login.model;

import android.support.annotation.NonNull;

import com.pine.base.architecture.mvp.model.IModelAsyncResponse;
import com.pine.login.bean.AccountBean;

import java.util.HashMap;

public interface ILoginAccountModel {
    void requestRegister(final HashMap<String, String> params,
                         @NonNull final IModelAsyncResponse<AccountBean> callback);
}
