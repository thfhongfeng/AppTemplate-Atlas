package com.pine.base.http.callback;

import android.app.Application;
import android.widget.Toast;

import com.pine.base.R;
import com.pine.base.http.HttpResponse;
import com.pine.tool.util.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public abstract class HttpJsonCallback extends HttpAbstractBaseCallback {

    public void onResponse(int what, HttpResponse response) {
        String res = (String) response.getData();
        try {
            JSONObject jsonObject = new JSONObject(res);
            onResponse(what, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            if (!onError(what, e)) {
                Application application = AppUtils.getApplicationByReflect();
                Toast.makeText(application, application.getString(R.string.base_json_data_err),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public abstract void onResponse(int what, JSONObject jsonObject);

    public abstract boolean onError(int what, Exception e);
}
