package 归并排序;

import utils.Utils;

/**
 * @author Arvin
 * @time 2017/2/9 20:32
 */
public class MergSortTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray();

        System.out.println("没有排序之前: ");
        Utils.show(array);

        System.out.println("升序排序之后: ");
        Utils.show(mergeSort(array, true));


        System.out.println("降序排序之后: ");
        Utils.show(mergeSort(array, false));

        System.out.println("升序排序之后: ");
        Utils.show(mergeSort(array, true));
    }

    /**
     * 递归算法
     *
     * @param array 要排序的数组
     * @param asc   是否升序排列
     * @return
     */
    public static int[] mergeSort(int[] array, boolean asc) {
        if (array.length < 2) {
            return array;
        }

        // 拆分为两个数组
        int len = array.length;
        int[] temp = Utils.copy(array);

        // 左边第一个数组的范围是 leftStart - leftEnd
        int leftStart = 0;
        int leftEnd = len % 2 == 0 ? len / 2 - 1 : len / 2;

        // 右边第一个数组的范围是 rightStart - rightEnd
        int rightStart = leftEnd + 1;
        int rightEnd = len - 1;

        // 进行合并排序
        mergeSort(array, temp, leftStart, leftEnd, rightStart, rightEnd, asc);
        return temp;
    }

    /**
     * 递归合并排序
     *
     * @param array
     * @param temp
     * @param leftStart
     * @param leftEnd
     * @param rightStart
     * @param rightEnd
     * @param asc
     */
    private static void mergeSort(int[] array, int[] temp, int leftStart, int leftEnd, int rightStart, int rightEnd, boolean asc) {
        // 左边数组大小
        int leftSize = leftEnd - leftStart + 1;
        // 右边数组大小
        int rightSize = rightEnd - rightStart + 1;

        // 如果leftSize>1那么需要继续拆分
        if (leftSize > 1) {
            int subLeftStart = leftStart;
            int subLeftEnd = (leftEnd - leftStart) / 2 + subLeftStart;
            int subRightStart = subLeftEnd + 1;
            int subRightEnd = leftEnd;
            // 递归归并排序
            mergeSort(array, temp, subLeftStart, subLeftEnd, subRightStart, subRightEnd, asc);
        } // 经过if语句后，左侧变成已经是有序的了
        if (rightSize > 1) {
            int subLeftStart = rightStart;
            int subLeftEnd = (rightEnd - rightStart) / 2 + subLeftStart;
            int subRightStart = subLeftEnd + 1;
            int subRightEnd = rightEnd;
            // 递归归并排序
            mergeSort(array, temp, subLeftStart, subLeftEnd, subRightStart, subRightEnd, asc);
        } // 经过if语句后，右侧变成已经是有序的了

        // 进行合并， 赋值的范围就是leftStart 到rightEnd
        int copyStartIndex = leftStart;
        int leftStartIndex = leftStart;
        int rightStartIndex = rightStart;

        // 进行合并
        while (leftStartIndex <= leftEnd && rightStartIndex <= rightEnd) {
            int leftVal = array[leftStartIndex];
            int rightVal = array[rightStartIndex];

            if (asc) {
                if (leftVal < rightVal) {
                    temp[copyStartIndex++] = leftVal;
                    leftStartIndex++;
                } else {
                    temp[copyStartIndex++] = rightVal;
                    rightStartIndex++;
                }
            } else {
                if (leftVal > rightVal) {
                    temp[copyStartIndex++] = leftVal;
                    leftStartIndex++;
                } else {
                    temp[copyStartIndex++] = rightVal;
                    rightStartIndex++;
                }
            }
        }
        // 左侧的不为空
        while (leftStartIndex <= leftEnd) {
            temp[copyStartIndex++] = array[leftStartIndex++];
        }
        // 右侧的不为空
        while (rightStartIndex <= rightEnd) {
            temp[copyStartIndex++] = array[rightStartIndex++];
        }

        // 复制
        Utils.copy(temp, array, leftStart, rightEnd);
    }
}
