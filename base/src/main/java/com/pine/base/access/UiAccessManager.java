package com.pine.base.access;

import android.content.Context;

import java.util.LinkedHashMap;

/**
 * Created by tanghongfeng on 2018/9/16
 */

public class UiAccessManager {
    private static volatile UiAccessManager mInstance;
    private LinkedHashMap<String, IUiAccessExecutor> mAccessExecutorMap = new LinkedHashMap<String, IUiAccessExecutor>();

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

    public void addAccessExecutor(String key, IUiAccessExecutor accessExecutor) {
        mAccessExecutorMap.put(key, accessExecutor);
    }

    public void removeAccessExecutor(IUiAccessExecutor accessExecutor) {
        mAccessExecutorMap.remove(accessExecutor);
    }

    public void clearAccessExecutor() {
        mAccessExecutorMap.clear();
    }

    public boolean checkCanAccess(Context context) {
        if (context == null) {
            return false;
        }
        UiAccessAnnotation annotation = context.getClass().getAnnotation(UiAccessAnnotation.class);
        if (annotation != null) {
            String[] types = annotation.AccessTypes();
            int[] levels = annotation.LevelValues();
            if (types == null || levels == null || types.length != levels.length) {
                return false;
            }
            for (int i = 0; i < types.length; i++) {
                if (mAccessExecutorMap.get(types[i]) != null &&
                        !mAccessExecutorMap.get(types[i]).onExecute(context, levels[i])) {
                    return false;
                }
            }
        }
        return true;
    }
}
