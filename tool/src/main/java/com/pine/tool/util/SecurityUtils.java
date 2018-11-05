package com.pine.tool.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by tanghongfeng on 2018/11/5
 */

public class SecurityUtils {
    public static final String TAG = LogUtils.makeLogTag(SecurityUtils.class);

    public static String generateMD5(String input) {
        String ret = null;

        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            ret = hex.toString();
        } catch (NoSuchAlgorithmException e) {
            LogUtils.d(TAG, "Failed to encrypt the password!");
        } catch (UnsupportedEncodingException e) {
            LogUtils.d(TAG, "No UTF-8 encoding support!");
        }
        return ret;
    }
}
