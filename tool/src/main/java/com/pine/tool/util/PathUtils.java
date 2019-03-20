package com.pine.tool.util;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * Created by tanghongfeng on 2018/10/12
 */

public class PathUtils {
    private static final String TAG = LogUtils.makeLogTag(PathUtils.class);

    private PathUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * Return the path of /system.
     *
     * @return the path of /system
     */
    public static String getRootPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * Return the path of /data.
     *
     * @return the path of /data
     */
    public static String getDataPath() {
        return Environment.getDataDirectory().getAbsolutePath();
    }

    /**
     * Return the path of /cache.
     *
     * @return the path of /cache
     */
    public static String getCachePath() {
        return Environment.getDownloadCacheDirectory().getAbsolutePath();
    }

    /**
     * 获取SD卡文件目录
     *
     * @param type 文件夹类型
     *             如果为空则返回 /storage/emulated/0
     *             否则返回对应类型的文件夹:
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, /storage/emulated/0/Pictures
     *             {@link android.os.Environment#DIRECTORY_MUSIC}, /storage/emulated/0/Music
     *             {@link android.os.Environment#DIRECTORY_DCIM}, /storage/emulated/0/DCIM
     *             {@link android.os.Environment#DIRECTORY_PODCASTS}, /storage/emulated/0/Podcasts
     *             {@link android.os.Environment#DIRECTORY_RINGTONES}, /storage/emulated/0/Ringtones
     *             {@link android.os.Environment#DIRECTORY_ALARMS}, /storage/emulated/0/Alarms
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS}, /storage/emulated/0/Notifications
     *             {@link android.os.Environment#DIRECTORY_MOVIES}. /storage/emulated/0/Movies
     *             or 自定义文件夹名称"type"  /storage/emulated/0/{type}
     * @return 缓存目录文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalPublicDirectory(String type) {
        File fileDir = null;
        if (!isExternalStorageDisable()) {
            if (TextUtils.isEmpty(type)) {
                fileDir = Environment.getExternalStorageDirectory();
            } else {
                fileDir = Environment.getExternalStoragePublicDirectory(type);
            }
            if (fileDir == null) {
                LogUtils.d(TAG, "getExternalDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!fileDir.exists() && !fileDir.mkdirs()) {
                    LogUtils.d(TAG, "getExternalDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            LogUtils.d(TAG, "getExternalDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return fileDir;
    }

    public static String getExternalPublicPath(String type) {
        File file = getExternalPublicDirectory(type);
        if (file == null) {
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * Return the path of /data/app_package_name/package.
     *
     * @return the path of /data/app_package_name/package
     */
    public static String getInternalAppPath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return AppUtils.getApplication().getApplicationInfo().dataDir;
        }
        return AppUtils.getApplication().getDataDir().getAbsolutePath();
    }

    /**
     * Return the path of /data/data/app_package_name/code_cache.
     *
     * @return the path of /data/data/app_package_name/code_cache
     */
    public static String getInternalAppCodeCachePath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return AppUtils.getApplication().getApplicationInfo().dataDir + "/code_cache";
        }
        return AppUtils.getApplication().getCodeCacheDir().getAbsolutePath();
    }

    /**
     * Return the path of /data/data/app_package_name/cache.
     *
     * @return the path of /data/data/app_package_name/cache
     */
    public static String getInternalAppCachePath() {
        return AppUtils.getApplication().getCacheDir().getAbsolutePath();
    }

    /**
     * Return the path of /data/data/app_package_name/databases.
     *
     * @return the path of /data/data/app_package_name/databases
     */
    public static String getInternalAppDbsPath() {
        return AppUtils.getApplication().getApplicationInfo().dataDir + "/databases";
    }

    /**
     * Return the path of /data/data/app_package_name/files.
     *
     * @return the path of /data/data/app_package_name/files
     */
    public static String getInternalAppFilesPath() {
        return AppUtils.getApplication().getFilesDir().getAbsolutePath();
    }

    /**
     * Return the path of /data/data/app_package_name/shared_prefs.
     *
     * @return the path of /data/data/app_package_name/shared_prefs
     */
    public static String getInternalAppSpPath() {
        return AppUtils.getApplication().getApplicationInfo().dataDir + "shared_prefs";
    }

    /**
     * Return the path of /data/data/app_package_name/no_backup.
     *
     * @return the path of /data/data/app_package_name/no_backup
     */
    public static String getInternalAppNoBackupFilesPath() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return AppUtils.getApplication().getApplicationInfo().dataDir + "no_backup";
        }
        return AppUtils.getApplication().getNoBackupFilesDir().getAbsolutePath();
    }

    /**
     * Return the path of /storage/emulated/0/Android/data/app_package_name/cache
     *
     * @return the path of /storage/emulated/0/Android/data/app_package_name/cache
     */
    public static String getExternalAppCachePath() {
        return AppUtils.getApplication().getExternalCacheDir().getAbsolutePath();
    }

    /**
     * 获取应用专属的type对应的文件夹目录
     * android 4.4及以上系统不需要申请SD卡读写权限, 因此也不用考虑6.0系统动态申请SD卡读写权限问题，
     * 且应用被卸载后自动清空, 不会污染用户存储空间
     *
     * @param type 文件夹类型 可以为空，
     *             为空则返回API得到的files目录
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files
     *             无SD卡: data/data/app_package_name/files
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_PICTURES},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Pictures
     *             无SD卡: data/data/app_package_name/files/Pictures
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_MUSIC},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Music
     *             无SD卡: data/data/app_package_name/files/Music
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_DCIM},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Music
     *             无SD卡: data/data/app_package_name/files/Music
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_PODCASTS},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Podcasts
     *             无SD卡: data/data/app_package_name/files/Podcasts
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_RINGTONES},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Ringtones
     *             无SD卡: data/data/app_package_name/files/Ringtones
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_ALARMS},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Alarms
     *             无SD卡: data/data/app_package_name/files/Alarms
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS},
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Notifications
     *             无SD卡: data/data/app_package_name/files/Notifications
     *             <p>
     *             {@link android.os.Environment#DIRECTORY_MOVIES}.
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/Movies
     *             无SD卡: data/data/app_package_name/files/Movies
     *             <p>
     *             自定义文件夹名称"type"
     *             有SD卡: /storage/emulated/0/Android/data/app_package_name/files/{type}
     *             无SD卡: data/data/app_package_name/files/{type}
     * @return type对应的文件夹 如果没有SD卡或SD卡有问题则返回内存的type对应的文件夹，否则优先返回SD卡type对应的文件夹
     */
    public static File getAppFileDirectory(String type) {
        File appCacheDir = getExternalAppFileDirectory(type);
        if (appCacheDir == null) {
            appCacheDir = getInternalAppFileDirectory(type);
        }

        if (appCacheDir == null) {
            LogUtils.d(TAG, "getAppFileDirectory fail ,the reason is mobile phone unknown exception !");
        } else {
            if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                LogUtils.d(TAG, "getAppFileDirectory fail ,the reason is make directory fail !");
            }
        }
        return appCacheDir;
    }

    public static String getAppFilePath(String type) {
        File file = getAppFileDirectory(type);
        if (file == null) {
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取SD卡下的应用专属type对应的文件夹目录
     *
     * @param type 文件夹类型
     *             如果为空则返回 /storage/emulated/0/Android/data/app_package_name/files
     *             否则返回对应类型的文件夹:
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, /storage/emulated/0/Android/data/app_package_name/files/Pictures
     *             {@link android.os.Environment#DIRECTORY_MUSIC}, /storage/emulated/0/Android/data/app_package_name/files/Music
     *             {@link android.os.Environment#DIRECTORY_DCIM}, /storage/emulated/0/Android/data/app_package_name/files/DCIM
     *             {@link android.os.Environment#DIRECTORY_PODCASTS}, /storage/emulated/0/Android/data/app_package_name/files/Podcasts
     *             {@link android.os.Environment#DIRECTORY_RINGTONES}, /storage/emulated/0/Android/data/app_package_name/files/Ringtones
     *             {@link android.os.Environment#DIRECTORY_ALARMS}, /storage/emulated/0/Android/data/app_package_name/files/Alarms
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS}, /storage/emulated/0/Android/data/app_package_name/files/Notifications
     *             {@link android.os.Environment#DIRECTORY_MOVIES}. /storage/emulated/0/Android/data/app_package_name/files/Movies
     *             or 自定义文件夹名称"type"  /storage/emulated/0/Android/data/app_package_name/files/{type}
     * @return type对应的文件夹 或 null（无SD卡或SD卡挂载失败）
     */
    public static File getExternalAppFileDirectory(String type) {
        File appCacheDir = null;
        if (!isExternalStorageDisable()) {
            if (TextUtils.isEmpty(type)) {
                appCacheDir = AppUtils.getApplication().getExternalFilesDir(null);
            } else {
                appCacheDir = AppUtils.getApplication().getExternalFilesDir(type);
            }

            if (appCacheDir == null) {// 有些手机需要通过自定义目录
                appCacheDir = new File(Environment.getExternalStorageDirectory(),
                        "Android/data/" + AppUtils.getApplication().getPackageName() + "/files/" + type);
            }

            if (appCacheDir == null) {
                LogUtils.d(TAG, "getExternalAppFileDirectory fail ,the reason is sdCard unknown exception !");
            } else {
                if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
                    LogUtils.d(TAG, "getExternalAppFileDirectory fail ,the reason is make directory fail !");
                }
            }
        } else {
            LogUtils.d(TAG, "getExternalAppFileDirectory fail ,the reason is sdCard nonexistence or sdCard mount fail !");
        }
        return appCacheDir;
    }

    public static String getExternalAppFilePath(String type) {
        File file = getExternalAppFileDirectory(type);
        if (file == null) {
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取内存下的应用专属type对应的文件夹目录
     *
     * @param type 文件夹类型
     *             如果为空则返回 /data/data/app_package_name/files
     *             否则返回对应类型的文件夹:
     *             {@link android.os.Environment#DIRECTORY_PICTURES}, /data/data/app_package_name/files/Pictures
     *             {@link android.os.Environment#DIRECTORY_MUSIC}, /data/data/app_package_name/files/Music
     *             {@link android.os.Environment#DIRECTORY_DCIM}, /data/data/app_package_name/files/DCIM
     *             {@link android.os.Environment#DIRECTORY_PODCASTS}, /data/data/app_package_name/files/Podcasts
     *             {@link android.os.Environment#DIRECTORY_RINGTONES}, /data/data/app_package_name/files/Ringtones
     *             {@link android.os.Environment#DIRECTORY_ALARMS}, /data/data/app_package_name/files/Alarms
     *             {@link android.os.Environment#DIRECTORY_NOTIFICATIONS}, /data/data/app_package_name/files/Notifications
     *             {@link android.os.Environment#DIRECTORY_MOVIES}. /data/data/app_package_name/files/Movies
     *             or 自定义文件夹名称"type"  /data/data/app_package_name/files/{type}
     * @return type对应的文件夹 或 null（创建目录文件失败）
     * 注：该方法获取的目录是能供当前应用自己使用，外部应用没有读写权限，如 系统相机应用
     */
    public static File getInternalAppFileDirectory(String type) {
        File appCacheDir = null;
        if (TextUtils.isEmpty(type)) {
            appCacheDir = AppUtils.getApplication().getFilesDir();// /data/data/app_package_name/files
        } else {
            appCacheDir = new File(AppUtils.getApplication().getFilesDir(), type);// /data/data/app_package_name/files/type
        }

        if (!appCacheDir.exists() && !appCacheDir.mkdirs()) {
            LogUtils.d(TAG, "getInternalAppFileDirectory fail ,the reason is make directory fail !");
        }
        return appCacheDir;
    }

    public static String getInternalAppFilePath(String type) {
        File file = getInternalAppFileDirectory(type);
        if (file == null) {
            return "";
        }
        return file.getAbsolutePath();
    }

    /**
     * 获取obb下的应用的目录
     *
     * @return 有sd卡: /storage/emulated/0/Android/obb/app_package_name
     * 无sd卡: ""
     */
    public static String getExternalAppObbPath() {
        if (isExternalStorageDisable()) {
            return "";
        }
        return AppUtils.getApplication().getObbDir().getAbsolutePath();
    }

    private static boolean isExternalStorageDisable() {
        return !Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
