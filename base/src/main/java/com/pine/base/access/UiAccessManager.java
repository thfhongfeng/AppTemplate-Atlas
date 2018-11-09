package com.pine.base.access;

import android.app.Activity;
import android.support.v4.app.Fragment;

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

    public boolean checkCanAccess(Activity activity, boolean resumeUi) {
        if (activity == null) {
            return false;
        }
        UiAccessAnnotation annotation = activity.getClass().getAnnotation(UiAccessAnnotation.class);
        if (annotation != null) {
            String[] types = annotation.AccessTypes();
            String[] args = annotation.Args();
            if (types == null || args == null || types.length != args.length) {
                return false;
            }
            for (int i = 0; i < types.length; i++) {
                if (mAccessExecutorMap.get(types[i]) != null &&
                        !mAccessExecutorMap.get(types[i]).onExecute(activity, args[i], resumeUi)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkCanAccess(Fragment fragment, boolean isResumeUi) {
        if (fragment == null) {
            return false;
        }
        UiAccessAnnotation annotation = fragment.getClass().getAnnotation(UiAccessAnnotation.class);
        if (annotation != null) {
            String[] types = annotation.AccessTypes();
            String[] args = annotation.Args();
            if (types == null || args == null || types.length != args.length) {
                return false;
            }
            for (int i = 0; i < types.length; i++) {
                if (mAccessExecutorMap.get(types[i]) != null &&
                        !mAccessExecutorMap.get(types[i]).onExecute(fragment, args[i], isResumeUi)) {
                    return false;
                }
            }
        }
        return true;
    }
}
