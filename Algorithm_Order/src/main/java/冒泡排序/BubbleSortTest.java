package 冒泡排序;

import utils.Utils;

/**
 * 冒泡排序测试
 *
 * @author Arvin
 * @time 2017/2/9 18:59
 */
public class BubbleSortTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray();

        System.out.println("没有排序之前: ");
        Utils.show(array);

        bubbleSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);


        bubbleSort(array, false);
        System.out.println("降序排序之后: ");
        Utils.show(array);

        bubbleSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);
    }

    /**
     * <pre>
     * 冒泡排序:
     * 1. 第n个元素和第n+1个元素比较，确定是否要交换，如果需要交互就交换
     * 2. 第 n+1 和 第 n+2 个元素比较，同样操作
     *
     * </pre>
     *
     * @param array 要排序的数组
     * @param asc   是否升序排列
     * @return
     */
    public static int[] bubbleSort(int[] array, boolean asc) {

        int len = array.length;
        // 每次处理一个数据，将一个数字移动到上一次移动最后一位的前一位
        for (int i = len - 1; i >= 0; --i) {
            for (int j = 0; j < i; ++j) {
                int a = array[j];
                int b = array[j + 1];
                if (asc) {
                    if (a > b) { // 升序，左边大的进行交换
                        Utils.swap(array, j, j + 1);
                    }
                } else {
                    if (a < b) { // 降序，左边小的才进行交换
                        Utils.swap(array, j, j + 1);
                    }
                }
            }

        }

        return array;

    }
}
