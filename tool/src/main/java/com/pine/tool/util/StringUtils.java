package com.pine.tool.util;

import com.vdurmont.emoji.EmojiParser;

/**
 * Created by tanghongfeng on 2018/11/13
 */

public class StringUtils {
    public static String toChineseNumber(String numberStr) {
        String[] s1 = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
        String[] s2 = {"十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千"};
        String result = "";
        int n = numberStr.length();
        for (int i = 0; i < n; i++) {
            int num = numberStr.charAt(i) - '0';
            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                if (num != 0 || !s1[0].equals(result.charAt(result.length() - 1)) && i != n - 1) {
                    result += s1[num];
                }
            }
        }
        return result;
    }

    public static String toChineseNumber(int number) {
        String numberStr = String.valueOf(number);
        return toChineseNumber(numberStr);
    }

    public static boolean hasEmojis(String str) {
        return !EmojiParser.removeAllEmojis(str).equals(str);
    }
}
