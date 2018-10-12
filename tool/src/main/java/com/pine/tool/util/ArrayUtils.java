package com.pine.tool.util;

import android.text.TextUtils;

/**
 * Created by tanghongfeng on 2018/9/5.
 */

public class ArrayUtils {

    private ArrayUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 在数组中查询第一个element元素
     *
     * @param objects 待操作的数组
     * @param element 待匹配的元素
     * @return 索引，如不存在，-1
     */
    public static int searchFirst(Object[] objects, Object element) {
        int index = -1;
        for (int i = 0; i < objects.length; i++) {
            if (element.equals(objects[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 在数组中查询最后一个element元素
     *
     * @param objects 待操作的数组
     * @param element 待匹配的元素
     * @return 索引，如不存在，-1
     */
    public static int searchLast(Object[] objects, Object element) {
        int index = -1;
        for (int i = objects.length - 1; i >= 0; i++) {
            if (element.equals(objects[i])) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 将数组颠倒
     */
    public static Object[] upsideDown(Object[] objects) {
        int length = objects.length;
        Object temp;
        for (int i = 0; i < length / 2; i++) {
            temp = objects[i];
            objects[i] = objects[length - 1 - i];
            objects[length - 1 - i] = temp;
            temp = null;
        }
        return objects;
    }

    /**
     * 将给定的数组转换成字符串
     *
     * @param integers     给定的数组
     * @param startSymbols 开始符号
     * @param separator    分隔符
     * @param endSymbols   结束符号
     * @return 例如开始符号为"{"，分隔符为", "，结束符号为"}"，那么结果为：{1, 2, 3}
     */
    public static String toString(int[] integers, String startSymbols, String separator, String endSymbols) {
        boolean addSeparator = false;
        StringBuffer sb = new StringBuffer();
        //如果开始符号不为null且不空
        if (!TextUtils.isEmpty(startSymbols)) {
            sb.append(startSymbols);
        }

        //循环所有的对象
        for (int object : integers) {
            //如果需要添加分隔符
            if (addSeparator) {
                sb.append(separator);
                addSeparator = false;
            }
            sb.append(object);
            addSeparator = true;
        }

        //如果结束符号不为null且不空
        if (!TextUtils.isEmpty(endSymbols)) {
            sb.append(endSymbols);
        }
        return sb.toString();
    }

    /**
     * 将给定的数组转换成字符串
     *
     * @param integers  给定的数组
     * @param separator 分隔符
     * @return 例如分隔符为", "那么结果为：1, 2, 3
     */
    public static String toString(int[] integers, String separator) {
        return toString(integers, null, separator, null);
    }

    /**
     * 将给定的数组转换成字符串，默认分隔符为", "
     *
     * @param integers 给定的数组
     * @return 例如：1, 2, 3
     */
    public static String toString(int[] integers) {
        return toString(integers, null, ", ", null);
    }

    /**
     * 将给定的数组转换成字符串
     *
     * @param objects      给定的数组
     * @param startSymbols 开始符号
     * @param separator    分隔符
     * @param endSymbols   结束符号
     * @return 例如开始符号为"{"，分隔符为", "，结束符号为"}"，那么结果为：{1, 2, 3}
     */
    public static String toString(Object[] objects, String startSymbols, String separator, String endSymbols) {
        boolean addSeparator = false;
        StringBuffer sb = new StringBuffer();
        //如果开始符号不为null且不空
        if (!TextUtils.isEmpty(startSymbols)) {
            sb.append(startSymbols);
        }

        //循环所有的对象
        for (Object object : objects) {
            //如果需要添加分隔符
            if (addSeparator) {
                sb.append(separator);
                addSeparator = false;
            }
            sb.append(object);
            addSeparator = true;
        }

        //如果结束符号不为null且不空
        if (!TextUtils.isEmpty(endSymbols)) {
            sb.append(endSymbols);
        }
        return sb.toString();
    }

    /**
     * 将给定的数组转换成字符串
     *
     * @param objects   给定的数组
     * @param separator 分隔符
     * @return 例如分隔符为", "那么结果为：1, 2, 3
     */
    public static String toString(Object[] objects, String separator) {
        return toString(objects, null, separator, null);
    }

    /**
     * 将给定的数组转换成字符串，默认分隔符为", "
     *
     * @param objects 给定的数组
     * @return 例如：1, 2, 3
     */
    public static String toString(Object[] objects) {
        return toString(objects, null, ", ", null);
    }
}
