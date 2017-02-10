package 希尔排序;

import utils.Utils;

/**
 * @author Arvin
 * @time 2017/2/10 15:36
 */
public class ShellSortTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray();

        System.out.println("没有排序之前: ");
        Utils.show(array);

        shellSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);

        shellSort(array, false);
        System.out.println("降序排序之后: ");
        Utils.show(array);

        shellSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);
    }

    /**
     * @param array 要排序的数组
     * @param asc   是否升序排列
     * @return
     */
    public static int[] shellSort(int[] array, boolean asc) {

        int len = array.length;

        if (len < 2) {
            return array;
        }

        // 确定步长
        int step = len / 3; // 默认用3作为基数
        int start = 0; // 从0开始

        while (step >= 1) {
            while (start < step) {
                for (int markIndex = start + step; markIndex < array.length; markIndex += step) {
                    int back = array[markIndex]; // 记录标记位，其实是从数组中取出标记位
                    int cursor = markIndex; // 游标
                    if (asc) {
                        while (cursor >= step && array[cursor - step] > back) {
                            // 向右移位
                            array[cursor] = array[cursor - step];
                            cursor -= step;
                        }
                    } else {
                        while (cursor >= step && array[cursor - step] < back) {
                            // 向右移位
                            array[cursor] = array[cursor - step];
                            cursor -= step;
                        }
                    }
                    // 最后把标记位设置到最后一次移位的地方
                    array[cursor] = back;
                }
                start++;
            }
            start = 0;
            step--;
        }
        return array;

    }
}
