package com.pine.tool.util;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

/**
 * Created by tanghongfeng on 2018/9/10.
 */

public class SharePreferenceUtils {
    public static final String TAG = LogUtils.makeLogTag(SharePreferenceUtils.class);

    // 应用缓存SharePreference，生命周期随用户指定
    public static final String CACHE_SP = "cache_share_preference";
    // 应用配置SharePreference，生命周期与APP安装周期相同（APP卸载才会被清理）
    public static final String CONFIG_SP = "config_share_preference";
    // 应用本次启动SharePreference，生命周期为本次APP启动周期（APP每次重新启动时清理）
    public static final String THIS_STARTUP_SP = "this_startup_share_preference";

    private static Application mApplication = AppUtils.getApplicationByReflect();

    private SharePreferenceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 删除cache数据库
     */
    public static void cleanCache() {
        cleanData(mApplication, CACHE_SP);
    }

    /**
     * 删除cache数据库
     *
     * @param context
     */
    public static void cleanCache(Context context) {
        cleanData(context, CACHE_SP);
    }

    /**
     * 删除config数据库
     */
    public static void cleanConfig() {
        cleanData(mApplication, CONFIG_SP);
    }

    /**
     * 删除config数据库
     *
     * @param context
     */
    public static void cleanConfig(Context context) {
        cleanData(context, CONFIG_SP);
    }


    /**
     * 删除this_startup数据库
     */
    public static void cleanThisStartup() {
        cleanData(mApplication, THIS_STARTUP_SP);
    }

    /**
     * 删除this_startup数据库
     *
     * @param context
     */
    public static void cleanThisStartup(Context context) {
        cleanData(context, THIS_STARTUP_SP);
    }

    /**
     * 清除cache数据
     *
     * @param key
     */
    public static void cleanCacheKey(String key) {
        cleanDataKey(mApplication, CACHE_SP, key);
    }

    /**
     * 清除cache数据
     *
     * @param context
     * @param key
     */
    public static void cleanCacheKey(Context context, String key) {
        cleanDataKey(context, CACHE_SP, key);
    }

    /**
     * 清除config数据
     *
     * @param key
     */
    public static void cleanConfigKey(String key) {
        cleanDataKey(mApplication, CONFIG_SP, key);
    }

    /**
     * 清除config数据
     *
     * @param context
     * @param key
     */
    public static void cleanConfigKey(Context context, String key) {
        cleanDataKey(context, CONFIG_SP, key);
    }

    /**
     * 删除this_startup数据
     *
     * @param key
     */
    public static void cleanThisStartupKey(String key) {
        cleanDataKey(mApplication, THIS_STARTUP_SP, key);
    }

    /**
     * 删除this_startup数据
     *
     * @param context
     * @param key
     */
    public static void cleanThisStartupKey(Context context, String key) {
        cleanDataKey(context, THIS_STARTUP_SP, key);
    }

    /**
     * 保存数据到cache中
     *
     * @param key
     * @param value
     */
    public static void saveToCache(String key, Object value) {
        save(mApplication, CACHE_SP, key, value);
    }

    /**
     * 保存数据到cache中
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveToCache(Context context, String key, Object value) {
        save(context, CACHE_SP, key, value);
    }

    /**
     * 保存数据到config中
     *
     * @param key
     * @param value
     */
    public static void saveToConfig(String key, Object value) {
        save(mApplication, CONFIG_SP, key, value);
    }

    /**
     * 保存数据到config中
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveToConfig(Context context, String key, Object value) {
        save(context, CONFIG_SP, key, value);
    }


    /**
     * 保存数据到this_startup中
     *
     * @param key
     * @param value
     */
    public static void saveToThisStartup(String key, Object value) {
        save(mApplication, THIS_STARTUP_SP, key, value);
    }

    /**
     * 保存数据到this_startup中
     *
     * @param context
     * @param key
     * @param value
     */
    public static void saveToThisStartup(Context context, String key, Object value) {
        save(context, THIS_STARTUP_SP, key, value);
    }

    /**
     * 判断cache中是否包含某一个key
     *
     * @param key
     * @return
     */
    public static boolean isCacheContainsKey(String key) {
        return isContainsKey(CACHE_SP, key);
    }

    public static String readStringFromCache(String key, String def) {
        return readString(mApplication, CACHE_SP, key, def);
    }

    public static String readStringFromCache(Context context, String key, String def) {
        return readString(context, CACHE_SP, key, def);
    }

    public static boolean readBooleanFromCache(String key, boolean def) {
        return readBoolean(mApplication, CACHE_SP, key, def);
    }

    public static boolean readBooleanFromCache(Context context, String key, boolean def) {
        return readBoolean(context, CACHE_SP, key, def);
    }

    public static int readIntFromCache(String key, int def) {
        return readInt(mApplication, CACHE_SP, key, def);
    }

    public static int readIntFromCache(Context context, String key, int def) {
        return readInt(context, CACHE_SP, key, def);
    }

    public static float readFloatFromCache(String key, float def) {
        return readFloat(mApplication, CACHE_SP, key, def);
    }

    public static float readFloatFromCache(Context context, String key, float def) {
        return readFloat(context, CACHE_SP, key, def);
    }

    public static long readLongFromCache(String key, long def) {
        return readLong(mApplication, CACHE_SP, key, def);
    }

    public static long readLongFromCache(Context context, String key, long def) {
        return readLong(context, CACHE_SP, key, def);
    }

    public static Set<String> readSetStringLongFromCache(String key, Set<String> def) {
        return readStringSet(mApplication, CACHE_SP, key, def);
    }

    public static Set<String> readSetStringLongFromCache(Context context, String key, Set<String> def) {
        return readStringSet(context, CACHE_SP, key, def);
    }

    public static String readStringFromConfig(String key, String def) {
        return readString(mApplication, CONFIG_SP, key, def);
    }

    public static String readStringFromConfig(Context context, String key, String def) {
        return readString(context, CONFIG_SP, key, def);
    }

    public static boolean readBooleanFromConfig(String key, boolean def) {
        return readBoolean(mApplication, CONFIG_SP, key, def);
    }

    public static boolean readBooleanFromConfig(Context context, String key, boolean def) {
        return readBoolean(context, CONFIG_SP, key, def);
    }

    public static int readIntFromConfig(String key, int def) {
        return readInt(mApplication, CONFIG_SP, key, def);
    }

    public static int readIntFromConfig(Context context, String key, int def) {
        return readInt(context, CONFIG_SP, key, def);
    }

    public static float readFloatFromConfig(String key, float def) {
        return readFloat(mApplication, CONFIG_SP, key, def);
    }

    public static float readFloatFromConfig(Context context, String key, float def) {
        return readFloat(context, CONFIG_SP, key, def);
    }

    public static long readLongFromConfig(String key, long def) {
        return readLong(mApplication, CONFIG_SP, key, def);
    }

    public static long readLongFromConfig(Context context, String key, long def) {
        return readLong(context, CONFIG_SP, key, def);
    }

    public static Set<String> readSetStringLongFromConfig(String key, Set<String> def) {
        return readStringSet(mApplication, CONFIG_SP, key, def);
    }

    public static Set<String> readSetStringLongFromConfig(Context context, String key, Set<String> def) {
        return readStringSet(context, CONFIG_SP, key, def);
    }

    public static String readStringFromThisStartup(String key, String def) {
        return readString(mApplication, THIS_STARTUP_SP, key, def);
    }

    public static String readStringFromThisStartup(Context context, String key, String def) {
        return readString(context, THIS_STARTUP_SP, key, def);
    }

    public static boolean readBooleanFromThisStartup(String key, boolean def) {
        return readBoolean(mApplication, THIS_STARTUP_SP, key, def);
    }

    public static boolean readBooleanFromThisStartup(Context context, String key, boolean def) {
        return readBoolean(context, THIS_STARTUP_SP, key, def);
    }

    public static int readIntFromThisStartup(String key, int def) {
        return readInt(mApplication, THIS_STARTUP_SP, key, def);
    }

    public static int readIntFromThisStartup(Context context, String key, int def) {
        return readInt(context, THIS_STARTUP_SP, key, def);
    }

    public static float readFloatFromThisStartup(String key, float def) {
        return readFloat(mApplication, THIS_STARTUP_SP, key, def);
    }

    public static float readFloatFromThisStartup(Context context, String key, float def) {
        return readFloat(context, THIS_STARTUP_SP, key, def);
    }

    public static long readLongFromThisStartup(String key, long def) {
        return readLong(mApplication, THIS_STARTUP_SP, key, def);
    }

    public static long readLongFromThisStartup(Context context, String key, long def) {
        return readLong(context, THIS_STARTUP_SP, key, def);
    }

    public static Set<String> readSetStringLongFromThisStartup(String key, Set<String> def) {
        return readStringSet(mApplication, THIS_STARTUP_SP, key, def);
    }

    public static Set<String> readSetStringLongFromThisStartup(Context context, String key, Set<String> def) {
        return readStringSet(context, THIS_STARTUP_SP, key, def);
    }

    /**
     * 判断cache中是否包含某一个key
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isCacheContainsKey(Context context, String key) {
        return isContainsKey(context, CACHE_SP, key);
    }

    /**
     * 判断config中是否包含某一个key
     *
     * @param key
     * @return
     */
    public static boolean isConfigContainsKey(String key) {
        return isContainsKey(CONFIG_SP, key);
    }

    /**
     * 判断config中是否包含某一个key
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isConfigContainsKey(Context context, String key) {
        return isContainsKey(context, CONFIG_SP, key);
    }

    /**
     * 判断this_startup中是否包含某一个key
     *
     * @param key
     * @return
     */
    public static boolean isThisStartupContainsKey(String key) {
        return isContainsKey(THIS_STARTUP_SP, key);
    }

    /**
     * 判断this_startup中是否包含某一个key
     *
     * @param context
     * @param key
     * @return
     */
    public static boolean isThisStartupContainsKey(Context context, String key) {
        return isContainsKey(context, THIS_STARTUP_SP, key);
    }

    /**
     * 清除指定库
     *
     * @param context
     * @param db
     */
    public static void cleanData(Context context, String db) {
        if (context == null) {
            context = mApplication;
        }
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 清除指定库中的某一个key
     *
     * @param context
     * @param db
     * @param key
     */
    public static void cleanDataKey(Context context, String db, String key) {
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putString(key, "");
        editor.commit();
    }

    public static void save(String db, String key, Object value) {
        save(mApplication, db, key, value);
    }

    public static void save(Context context, String db, String key, Object value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (value instanceof String) {
            saveString(context, db, key, (String) value);
        } else if (value instanceof Integer) {
            saveInt(context, db, key, (int) value);
        } else if (value instanceof Boolean) {
            saveBoolean(context, db, key, (boolean) value);
        } else if (value instanceof Float) {
            saveFloat(context, db, key, (float) value);
        } else if (value instanceof Long) {
            saveLong(context, db, key, (long) value);
        } else if (value instanceof Set<?>) {
            saveStringSet(context, db, key, (Set<String>) value);
        }
    }

    /**
     * 保存String到库中
     *
     * @param context
     * @param db
     * @param key
     * @param value
     */
    public static void saveString(Context context, String db, String key, String value) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * 保存boolean到库中
     *
     * @param context
     * @param db
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String db, String key, boolean value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * 保存int到库中
     *
     * @param context
     * @param db
     * @param key
     * @param value
     */
    public static void saveInt(Context context, String db, String key, int value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    /**
     * 保存float到库中
     *
     * @param context
     * @param db
     * @param key
     * @param value
     */
    public static void saveFloat(Context context, String db, String key, float value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    /**
     * 保存long到库中
     *
     * @param context
     * @param db
     * @param key
     * @param value
     */
    public static void saveLong(Context context, String db, String key, long value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.commit();
    }


    /**
     * 保存Set<String>到库中
     *
     * @param context
     * @param db
     * @param key
     * @param value
     */
    public static void saveStringSet(Context context, String db, String key, Set<String> value) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        SharedPreferences.Editor editor = context.getSharedPreferences(db, Context.MODE_PRIVATE).edit();
        editor.putStringSet(key, value);
        editor.commit();
    }

    /**
     * 读取String数据
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
     * 读取String数据
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
     * 读取int数据
     *
     * @param context
     * @param db
     * @param key
     * @param def
     * @return
     */
    public static int readInt(Context context, String db, String key, int def) {
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.getInt(key, def);
    }

    /**
     * 读取float数据
     *
     * @param context
     * @param db
     * @param key
     * @param def
     * @return
     */
    public static float readFloat(Context context, String db, String key, float def) {
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.getFloat(key, def);
    }

    /**
     * 读取long数据
     *
     * @param context
     * @param db
     * @param key
     * @param def
     * @return
     */
    public static long readLong(Context context, String db, String key, long def) {
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.getLong(key, def);
    }

    /**
     * 读取Set<String>数据
     *
     * @param context
     * @param db
     * @param key
     * @param def
     * @return
     */
    public static Set<String> readStringSet(Context context, String db, String key, Set<String> def) {
        SharedPreferences pref = context.getSharedPreferences(db, Context.MODE_PRIVATE);
        return pref.getStringSet(key, def);
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
