package 插入排序;

import utils.Utils;

/**
 * @author Arvin
 * @time 2017/2/9 20:32
 */
public class InsertSortTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray();

        System.out.println("没有排序之前: ");
        Utils.show(array);

        insertSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);


        insertSort(array, false);
        System.out.println("降序排序之后: ");
        Utils.show(array);

        insertSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);
    }

    /**
     * @param array 要排序的数组
     * @param asc   是否升序排列
     * @return
     */
    public static int[] insertSort(int[] array, boolean asc) {
        if (array.length < 2) {
            return array;
        }
        for (int markIndex = 1; markIndex < array.length; ++markIndex) {
            int back = array[markIndex]; // 记录标记位，其实是从数组中取出标记位
            int cursor = markIndex; // 游标
            if (asc) {
                while (cursor > 0 && array[cursor - 1] > back) {
                    // 向右移位
                    array[cursor] = array[cursor - 1];
                    cursor--;
                }
            } else {
                while (cursor > 0 && array[cursor - 1] < back) {
                    // 向右移位
                    array[cursor] = array[cursor - 1];
                    cursor--;
                }
            }
            // 最后把标记位设置到最后一次移位的地方
            array[cursor] = back;
        }
        return array;
    }
}
