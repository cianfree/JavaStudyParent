package 选择排序;

import utils.Utils;

/**
 * 选择排序
 *
 * @author Arvin
 * @time 2017/2/9 20:32
 */
public class SelectSortTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray();

        System.out.println("没有排序之前: ");
        Utils.show(array);

        selectSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);


        selectSort(array, false);
        System.out.println("降序排序之后: ");
        Utils.show(array);

        selectSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);
    }

    /**
     * @param array 要排序的数组
     * @param asc   是否升序排列
     * @return
     */
    public static int[] selectSort(int[] array, boolean asc) {

        int len = array.length;

        // 要换位置的下标
        for (int i = 0; i < len - 1; ++i) {
            // 往后找逻辑最小的元素
            int min = array[i];
            int minIndex = i;
            for (int j = i; j < len; ++j) {
                if (asc) {  // 升序
                    if (array[j] < min) {
                        minIndex = j;
                        min = array[j];
                    }
                } else { // 降序
                    if (array[j] > min) {
                        minIndex = j;
                        min = array[j];
                    }
                }
            }
            // 交换
            if (i != minIndex) {
                Utils.swap(array, i, minIndex);
            }
        }
        return array;
    }
}
