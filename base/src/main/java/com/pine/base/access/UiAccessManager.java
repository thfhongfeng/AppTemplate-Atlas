package com.pine.base.access;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessManager {
    private static volatile UiAccessManager mInstance;
    private HashMap<String, IUiAccessInterceptor> mAccessInterceptorMap = new HashMap<String, IUiAccessInterceptor>();

    private UiAccessManager() {
    }

    public static UiAccessManager getInstance() {
        if (mInstance == null) {
            synchronized (UiAccessManager.class) {
                if (mInstance == null) {
                    mInstance = new UiAccessManager();
                }
            }
        }
        return mInstance;
    }

    public void addAccessInterceptor(String key, IUiAccessInterceptor accessInterceptor) {
        mAccessInterceptorMap.put(key, accessInterceptor);
    }

    public void removeAccessInterceptor(IUiAccessInterceptor accessInterceptor) {
        mAccessInterceptorMap.remove(accessInterceptor);
    }

    public void clearAccessInterceptor() {
        mAccessInterceptorMap.clear();
    }

    public boolean checkAccess(Context context) {
        UiAccessAnnotation annotation = context.getClass().getAnnotation(UiAccessAnnotation.class);
        if (annotation != null) {
            String[] types = annotation.AccessTypes();
            int[] levels = annotation.LevelValues();
            if (types == null || levels == null || types.length != levels.length) {
                return false;
            }
            for (int i = 0; i < types.length; i++) {
                if (mAccessInterceptorMap.get(types[i]) != null && mAccessInterceptorMap.get(types[i]).onInterceptor(context, levels[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
