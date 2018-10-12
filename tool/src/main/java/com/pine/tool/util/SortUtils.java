package com.pine.tool.util;

import java.util.Stack;

/**
 * Created by tanghongfeng on 2018/9/6.
 */

public class SortUtils {
    private SortUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 使用选择排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 是否升序
     */
    public static void sortByChoose(int[] intArray, boolean ascending) {
        for (int i = 0; i < intArray.length - 1; i++) {
            int minStub = intArray[i];
            int findIndex = 0;
            for (int j = i + 1; j <= intArray.length - 1; j++) {
                boolean needResetStub = true;
                if (ascending) {
                    needResetStub = minStub > intArray[j];
                } else {
                    needResetStub = minStub < intArray[j];
                }
                if (needResetStub) {
                    minStub = intArray[j];
                    findIndex = j;
                }
            }

            if (findIndex != 0) {
                int f = intArray[findIndex];
                intArray[findIndex] = intArray[i];
                intArray[i] = f;
            }
        }
    }

    /**
     * 使用插入排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortByInsert(int[] intArray, boolean ascending) {
        for (int i = 1; i < intArray.length; i++) {
            int stub = intArray[i];
            int insertIndex = -1;
            for (int j = i - 1; j >= 0; j--) {
                boolean found = true;
                if (ascending) {
                    found = stub < intArray[j];
                } else {
                    found = stub > intArray[j];
                }
                if (!found) break;
                intArray[j + 1] = intArray[j];
                insertIndex = j;
            }

            if (insertIndex > -1) intArray[insertIndex] = stub;
        }
    }

    /**
     * 使用冒泡排序法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortByBubbling(int[] intArray, boolean ascending) {
        for (int i = 0; i < intArray.length - 1; i++) {
            for (int j = 0; j < intArray.length - 1; j++) {
                boolean found = true;
                if (ascending) {
                    found = intArray[j] > intArray[j + 1];
                } else {
                    found = intArray[j] < intArray[j + 1];
                }
                if (found) {
                    int t = intArray[j];
                    intArray[j] = intArray[j + 1];
                    intArray[j + 1] = t;
                }
            }
        }
    }


    /**
     * 使用递归快排法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 排序的方式，用本类中的静态字段指定
     */
    public static void sortByFastRecursion(int[] intArray, int start, int end, boolean ascending) {
        int tmp = intArray[start];
        int i = start;

        if (ascending) {
            for (int j = end; i < j; ) {
                while (intArray[j] > tmp && i < j) {
                    j--;
                }
                if (i < j) {
                    intArray[i] = intArray[j];
                    i++;
                }
                for (; intArray[i] < tmp && i < j; i++) {
                    ;
                }
                if (i < j) {
                    intArray[j] = intArray[i];
                    j--;
                }
            }
        } else {
            for (int j = end; i < j; ) {
                while (intArray[j] < tmp && i < j) {
                    j--;
                }
                if (i < j) {
                    intArray[i] = intArray[j];
                    i++;
                }
                for (; intArray[i] > tmp && i < j; i++) {
                    ;
                }
                if (i < j) {
                    intArray[j] = intArray[i];
                    j--;
                }
            }
        }

        intArray[i] = tmp;
        if (start < i - 1) {
            sortByFastRecursion(intArray, start, i - 1, ascending);
        }
        if (end > i + 1) {
            sortByFastRecursion(intArray, i + 1, end, ascending);
        }
    }


    /**
     * 使用栈快排法，对数组intArray进行排序
     *
     * @param intArray  待排序的数组
     * @param ascending 升序
     */
    public static void sortByFastStack(int[] intArray, boolean ascending) {
        Stack<Integer> sa = new Stack<Integer>();
        sa.push(0);
        sa.push(intArray.length - 1);
        while (!sa.isEmpty()) {
            int end = ((Integer) sa.pop()).intValue();
            int start = ((Integer) sa.pop()).intValue();
            int i = start;
            int j = end;
            int tmp = intArray[i];
            if (ascending) {
                while (i < j) {
                    while (intArray[j] > tmp && i < j) {
                        j--;
                    }
                    if (i < j) {
                        intArray[i] = intArray[j];
                        i++;
                    }
                    for (; intArray[i] < tmp && i < j; i++) {
                        ;
                    }
                    if (i < j) {
                        intArray[j] = intArray[i];
                        j--;
                    }
                }
            } else {
                while (i < j) {
                    while (intArray[j] < tmp && i < j) {
                        j--;
                    }
                    if (i < j) {
                        intArray[i] = intArray[j];
                        i++;
                    }
                    for (; intArray[i] > tmp && i < j; i++) {
                        ;
                    }
                    if (i < j) {
                        intArray[j] = intArray[i];
                        j--;
                    }
                }
            }

            intArray[i] = tmp;
            if (start < i - 1) {
                sa.push(Integer.valueOf(start));
                sa.push(Integer.valueOf(i - 1));
            }
            if (end > i + 1) {
                sa.push(Integer.valueOf(i + 1));
                sa.push(Integer.valueOf(end));
            }
        }
    }
}
