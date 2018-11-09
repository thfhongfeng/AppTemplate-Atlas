package com.pine.tool.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.pine.tool.Constants;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class SharePreferenceUtils {
    public static final String TAG = LogUtils.makeLogTag(SharePreferenceUtils.class);

    private static Application mApplication = AppUtils.getApplicationByReflect();

    private SharePreferenceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 删除cache数据库
     */
    public static void cleanCache() {
        SharedPreferences pref = mApplication.getSharedPreferences(Constants.CACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 删除cache数据库
     *
     * @param context
     */
    public static void cleanCache(Context context) {
        if (context == null) {
            context = mApplication;
        }
        SharedPreferences pref = context.getSharedPreferences(Constants.CACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清除cache数据
     *
     * @param key
     */
    public static void cleanCacheKey(String key) {
        cleanData(AppUtils.getApplicationByReflect(), Constants.CACHE, key);
    }

    /**
     * 清除cache数据
     *
     * @param context
     * @param key
     */
    public static void cleanCacheKey(Context context, String key) {
        cleanData(context, Constants.CACHE, key);
    }

    /**
     * 删除config数据库
     */
    public static void cleanConfig() {
        SharedPreferences pref = mApplication.getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 删除config数据库
     *
     * @param context
     */
    public static void cleanConfig(Context context) {
        if (context == null) {
            context = mApplication;
        }
        SharedPreferences pref = context.getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清除config数据
     *
     * @param key
     */
    public static void cleanConfigKey(String key) {
        cleanData(AppUtils.getApplicationByReflect(), Constants.CONFIG, key);
    }

    /**
     * 清除config数据
     *
     * @param context
     * @param key
     */
    public static void cleanConfigKey(Context context, String key) {
        cleanData(context, Constants.CONFIG, key);
    }

    /**
     * 清除指定库中的某一个key
     *
     * @param context
     * @param db
     * @param key
     */
    public static void cleanData(Context context, String db, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putString(key, "");
        editor.commit();
    }

    /**
     * 保存数据
     *
     * @param key
     * @param content
     */
    public static void saveStringToCache(String key, String content) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return;
        }
        saveString(mApplication, Constants.CACHE, key, content);
    }

    /**
     * 保存数据到cache中
     *
     * @param context
     * @param key
     * @param content
     */
    public static void saveStringToCache(Context context, String key, String content) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return;
        }
        saveString(context, Constants.CACHE, key, content);
    }

    /**
     * 保存数据到cache中
     *
     * @param key
     * @param content
     */
    public static void saveBooleanToCache(String key, boolean content) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        saveBoolean(mApplication, Constants.CACHE, key, content);
    }

    /**
     * 保存数据到cache中
     *
     * @param context
     * @param key
     * @param content
     */
    public static void saveBooleanToCache(Context context, String key, boolean content) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        saveBoolean(context, Constants.CACHE, key, content);
    }

    /**
     * 保存数据到config中
     *
     * @param key
     * @param content
     */
    public static void saveStringToConfig(String key, String content) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return;
        }
        saveString(mApplication, Constants.CONFIG, key, content);
    }

    /**
     * 保存数据到config中
     *
     * @param context
     * @param key
     * @param content
     */
    public static void saveStringToConfig(Context context, String key, String content) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return;
        }
        saveString(mApplication, Constants.CONFIG, key, content);
    }

    /**
     * 保存数据到config中
     *
     * @param key
     * @param content
     */
    public static void saveBooleanToConfig(String key, boolean content) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        saveBoolean(mApplication, Constants.CONFIG, key, content);
    }

    /**
     * 保存数据到config中
     *
     * @param context
     * @param key
     * @param content
     */
    public static void saveBooleanToConfig(Context context, String key, boolean content) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        saveBoolean(context, Constants.CONFIG, key, content);
    }

    /**
     * 保存数据到库中
     *
     * @param context
     * @param db
     * @param key
     * @param content
     */
    public static void saveString(Context context, String db, String key, String content) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putString(key, content);
        editor.commit();
    }

    /**
     * 保存数据到库中
     *
     * @param context
     * @param db
     * @param key
     * @param content
     */
    public static void saveBoolean(Context context, String db, String key, boolean content) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, content);
        editor.commit();
    }


    /**
     * 读取cache中的数据
     *
     * @param key
     * @return
     */
    public static String readStringFromCache(String key) {
        return readString(mApplication, Constants.CACHE, key);
    }

    /**
     * 读取cache中的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String readStringFromCache(Context context, String key) {
        return readString(context, Constants.CACHE, key);
    }

    /**
     * 读取cache中的数据
     *
     * @param key
     * @return
     */
    public static boolean readBooleanFromCache(String key) {
        return readBoolean(mApplication, Constants.CACHE, key, false);
    }

    /**
     * 读取cache中的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean readBooleanFromCache(Context context, String key) {
        return readBoolean(context, Constants.CACHE, key, false);
    }

    /**
     * 读取cache中的数据 携带默认值
     *
     * @param key
     * @param def
     * @return
     */
    public static String readStringFromCache(String key, String def) {
        return readString(mApplication, Constants.CACHE, key, def);
    }

    /**
     * 读取cache中的数据 携带默认值
     *
     * @param context
     * @param key
     * @param def
     * @return
     */
    public static String readStringFromCache(Context context, String key, String def) {
        return readString(context, Constants.CACHE, key, def);
    }

    /**
     * 读取cache中的数据 携带默认值
     *
     * @param key
     * @param def
     * @return
     */
    public static boolean readBooleanFromCache(String key, boolean def) {
        return readBoolean(mApplication, Constants.CACHE, key, def);
    }

    /**
     * 读取config数据库中的数据
     *
     * @param key
     * @return
     */
    public static String readStringFromConfig(String key) {
        return readString(mApplication, Constants.CONFIG, key);
    }

    /**
     * 读取config数据库中的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static String readStringFromConfig(Context context, String key) {
        return readString(context, Constants.CONFIG, key);
    }

    /**
     * 读取config数据库中的数据 携带默认值
     *
     * @param key
     * @param def
     * @return
     */
    public static String readStringFromConfig(String key, String def) {
        return readString(mApplication, Constants.CONFIG, key, def);
    }

    /**
     * 读取Config数据库中的数据 携带默认值
     *
     * @param context
     * @param key
     * @param def
     * @return
     */
    public static String readStringFromConfig(Context context, String key, String def) {
        return readString(context, Constants.CONFIG, key, def);
    }

    /**
     * 读取config数据库中的数据
     *
     * @param key
     * @return
     */
    public static boolean readBooleanFromConfig(String key) {
        return readBoolean(mApplication, Constants.CONFIG, key, false);
    }

    /**
     * 读取config数据库中的数据
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean readBooleanFromConfig(Context context, String key) {
        return readBoolean(context, Constants.CONFIG, key, false);
    }

    /**
     * 读取config数据库中的数据 携带默认值
     *
     * @param key
     * @param def
     * @return
     */
    public static boolean readBooleanFromConfig(String key, boolean def) {
        return readBoolean(mApplication, Constants.CONFIG, key, def);
    }

    /**
     * 读取Config数据库中的数据 携带默认值
     *
     * @param context
     * @param key
     * @param def
     * @return
     */
    public static boolean readBooleanFromConfig(Context context, String key, boolean def) {
        return readBoolean(context, Constants.CONFIG, key, def);
    }

    /**
     * 读取string数据
     *
     * @param context
     * @param db
     * @param key
     * @return
     */
    public static String readString(Context context, String db, String key) {
        return readString(context, db, key, "");
    }

    /**
     * 读取string数据
     *
     * @param context
     * @param db
     * @param key
     * @param def
     * @return
     */
    public static String readString(Context context, String db, String key, String def) {
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.getString(key, def);
    }

    /**
     * 读取boolean数据
     *
     * @param context
     * @param db
     * @param key
     * @param def
     * @return
     */
    public static boolean readBoolean(Context context, String db, String key, boolean def) {
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.getBoolean(key, def);
    }

    /**
     * 判断是否包含某一个key  包含cache和config
     *
     * @param key
     * @return
     */
    public static boolean isContainsKey(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return isContainsKey(Constants.CACHE, key) || isContainsKey(Constants.CONFIG, key);
    }

    /**
     * 判断是否包含某一个key  包含Cache 和config
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isContainsKey(Context context, String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return isContainsKey(context, Constants.CACHE, key) || isContainsKey(context, Constants.CONFIG, key);
    }

    /**
     * 查询数据库是否包含key
     *
     * @param db
     * @param key
     * @return
     */
    public static boolean isContainsKey(String db, String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        SharedPreferences pref = mApplication.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.contains(key);
    }

    /**
     * 查询数据库是否包含key
     *
     * @param context
     * @param db
     * @param key
     * @return
     */
    public static boolean isContainsKey(Context context, String db, String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.contains(key);
    }
}
