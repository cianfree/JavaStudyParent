package com.github.search.simple.utils;

/**
 * 编辑距离计算工具类
 *
 * @author Arvin
 * @time 2016/10/11$ 10:58$
 */
public class EditDistanceUtils {

    private EditDistanceUtils() {
    }

    /**
     * 计算最小值
     */
    private static int min(int one, int two, int three) {
        int min = one;
        if (two < min) {
            min = two;
        }
        if (three < min) {
            min = three;
        }
        return min;
    }

    /**
     * 计算Edit distance
     */
    private static int ed(String str1, String str2, int str1Len, int str2Len) {
        int d[][];    //矩阵
        int n = str1Len;
        int m = str2Len;
        int i;    //遍历str1的
        int j;    //遍历str2的
        char ch1;    //str1的
        char ch2;    //str2的
        int temp;    //记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) {
            return m;
        }
        if (m == 0) {
            return n;
        }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++) {    //初始化第一列
            d[i][0] = i;
        }
        for (j = 0; j <= m; j++) {    //初始化第一行
            d[0][j] = j;
        }
        for (i = 1; i <= n; i++) {    //遍历str1
            ch1 = str1.charAt(i - 1);
            //去匹配str2
            for (j = 1; j <= m; j++) {
                ch2 = str2.charAt(j - 1);
                if (ch1 == ch2) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    /**
     * 计算两个字符串的相似度
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 返回 0-1之间（含）的浮点数
     */
    public static float similarity(String str1, String str2) {
        int str1Len = str1.length();
        int str2Len = str2.length();
        return 1 - (float) ed(str1, str2, str1Len, str2Len) / Math.max(str1Len, str2Len);
    }

}
