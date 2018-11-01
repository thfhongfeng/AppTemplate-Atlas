package com.pine.tool.util;

import java.math.BigDecimal;

/**
 * Created by tanghongfeng on 2018/9/5.
 */

public class DecimalUtils {
    // 默认除法运算精度
    private static final int DEFAULT_DIVIDE_SCALE = 10;

    private DecimalUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 提供精确的加法运算
     *
     * @param f1 被加数
     * @param f2 加数
     * @return 两个参数的和
     */
    public static float add(float f1, float f2) {
        return new BigDecimal(String.valueOf(f1)).add(new BigDecimal(String.valueOf(f2))).floatValue();
    }

    /**
     * 提供精确的加法运算
     *
     * @param f1    被加数
     * @param f2    加数
     * @param scale 保留scale 位小数
     * @return 两个参数的和
     */
    public static float add(float f1, float f2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "保留的小数位数必须大于零");
        }
        return new BigDecimal(String.valueOf(f1)).add(new BigDecimal(String.valueOf(f2))).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param f1 被减数
     * @param f2 减数
     * @return 两个参数的差
     */
    public static float subtract(float f1, float f2) {
        return new BigDecimal(String.valueOf(f1)).subtract(new BigDecimal(String.valueOf(f2))).floatValue();
    }

    /**
     * 提供精确的减法运算
     *
     * @param f1    被减数
     * @param f2    减数
     * @param scale 保留scale 位小数
     * @return 两个参数的差
     */
    public static float subtract(float f1, float f2, int scale) {
        return new BigDecimal(String.valueOf(f1)).subtract(new BigDecimal(String.valueOf(f2))).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param f1 被乘数
     * @param f2 乘数
     * @return 两个参数的积
     */
    public static float multiply(float f1, float f2) {
        return new BigDecimal(String.valueOf(f1)).multiply(new BigDecimal(String.valueOf(f2))).floatValue();
    }

    /**
     * 提供精确的乘法运算
     *
     * @param f1    被乘数
     * @param f2    乘数
     * @param scale 保留scale 位小数
     * @return 两个参数的积
     */
    public static float multiply(float f1, float f2, int scale) {
        return new BigDecimal(String.valueOf(f1)).multiply(new BigDecimal(String.valueOf(f2))).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入
     *
     * @param f1 被除数
     * @param f2 除数
     * @return 两个参数的商
     */
    public static float divide(float f1, float f2) {
        return divide(f1, f2, DEFAULT_DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入
     *
     * @param f1    被除数
     * @param f2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static float divide(float f1, float f2, int scale) {
        return divide(f1, f2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字遵循mode指定的规则
     *
     * @param mode 表示舍弃规则模式
     * @param f1   被除数
     * @param f2   除数
     * @return
     */
    public static float divide(int mode, float f1, float f2) {
        return divide(f1, f2, DEFAULT_DIVIDE_SCALE, mode);
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后scale位，以后的数字遵循mode指定的规则
     *
     * @param f1    被除数
     * @param f2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @param mode  表示舍弃规则模式
     * @return
     */
    public static float divide(float f1, float f2, int scale, int mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (mode < 0 || mode > 7) {
            throw new IllegalArgumentException("The mode must be valid");
        }
        return new BigDecimal(String.valueOf(f1)).divide(new BigDecimal(String.valueOf(f2)), scale, mode).floatValue();
    }

    public static double add(double d1, double d2) {
        return new BigDecimal(String.valueOf(d1)).add(new BigDecimal(String.valueOf(d2))).doubleValue();
    }

    public static double add(double d1, double d2, int scale) {
        return new BigDecimal(String.valueOf(d1)).add(new BigDecimal(String.valueOf(d2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double subtract(double d1, double d2) {
        return new BigDecimal(String.valueOf(d1)).subtract(new BigDecimal(String.valueOf(d2))).doubleValue();
    }

    public static double subtract(double d1, double d2, int scale) {
        return new BigDecimal(String.valueOf(d1)).subtract(new BigDecimal(String.valueOf(d2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double multiply(double d1, double d2) {
        return new BigDecimal(String.valueOf(d1)).multiply(new BigDecimal(String.valueOf(d2))).doubleValue();
    }

    public static double multiply(double d1, double d2, int scale) {
        return new BigDecimal(String.valueOf(d1)).multiply(new BigDecimal(String.valueOf(d2))).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double divide(double d1, double d2) {
        return divide(d1, d2, DEFAULT_DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static double divide(double d1, double d2, int scale) {
        return divide(d1, d2, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static double divide(int mode, double d1, double d2) {
        return divide(d1, d2, DEFAULT_DIVIDE_SCALE, mode);
    }

    public static double divide(double d1, double d2, int scale, int mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (mode < 0 || mode > 7) {
            throw new IllegalArgumentException("The mode must be valid");
        }
        return new BigDecimal(String.valueOf(d1)).divide(new BigDecimal(String.valueOf(d2)), scale, mode).doubleValue();
    }

    public static String add(String s1, String s2) {
        return new BigDecimal(s1).add(new BigDecimal(s2)).toString();
    }

    public static String add(String s1, String s2, int scale) {
        return new BigDecimal(s1).add(new BigDecimal(s2)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String subtract(String s1, String s2) {
        return new BigDecimal(s1).subtract(new BigDecimal(s2)).toString();
    }

    public static String subtract(String s1, String s2, int scale) {
        return new BigDecimal(s1).subtract(new BigDecimal(s2)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String multiply(String s1, String s2) {
        return new BigDecimal(s1).multiply(new BigDecimal(s2)).toString();
    }

    public static String multiply(String s1, String s2, int scale) {
        return new BigDecimal(s1).multiply(new BigDecimal(s2)).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String divide(String s1, String s2) {
        return divide(s1, s2, DEFAULT_DIVIDE_SCALE, BigDecimal.ROUND_HALF_UP);
    }

    public static String divide(String s1, String s2, int scale) {
        return divide(s1, s2, scale, BigDecimal.ROUND_HALF_UP);
    }

    public static String divide(int mode, String s1, String s2) {
        return divide(s1, s2, DEFAULT_DIVIDE_SCALE, mode);
    }

    public static String divide(String s1, String s2, int scale, int mode) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        if (mode < 0 || mode > 7) {
            throw new IllegalArgumentException("The mode must be valid");
        }
        return new BigDecimal(s1).divide(new BigDecimal(s2), scale, mode).toString();
    }

    public static boolean isEqual(float f1, float f2) {
        return Math.abs(f1 - f2) <= 0;
    }

    public static boolean isEqual(double d1, double d2) {
        return Math.abs(d1 - d2) <= 0;
    }

    public static float format(float f, int scale) {
        return new BigDecimal(f).setScale(scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    public static float format(float f, int scale, int mode) {
        return new BigDecimal(f).setScale(scale, mode).floatValue();
    }

    public static double format(double d, int scale) {
        return new BigDecimal(d).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double format(double d, int scale, int mode) {
        return new BigDecimal(d).setScale(scale, mode).doubleValue();
    }

    public static double format(String s, int scale) {
        return new BigDecimal(s).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double format(String s, int scale, int mode) {
        return new BigDecimal(s).setScale(scale, mode).doubleValue();
    }
}
