package 快速排序;

import utils.Utils;

/**
 * 快速排序测试
 *
 * @author Arvin
 * @time 2017/2/10 15:04
 */
public class QuickSortTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray();

        System.out.println("没有排序之前: ");
        Utils.show(array);

        quickSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);


        quickSort(array, false);
        System.out.println("降序排序之后: ");
        Utils.show(array);

        quickSort(array, true);
        System.out.println("升序排序之后: ");
        Utils.show(array);
    }

    /**
     * @param array 要排序的数组
     * @param asc   是否升序排列
     * @return
     */
    public static int[] quickSort(int[] array, boolean asc) {

        int len = array.length;
        if (len < 2) {
            return array;
        }
        recQuickSort(array, 0, array.length - 1, asc);
        return array;
    }

    /**
     * 进行快速排序
     *
     * @param array
     * @param left
     * @param right
     * @param asc
     */
    private static int[] recQuickSort(int[] array, int left, int right, boolean asc) {
        if (right - left <= 0) {
            return array;
        } else {
            // 确定划分
            int partition = partition(array, left, right, asc);

            // 对左边分区进行排序
            recQuickSort(array, left, partition - 1, asc);
            // 对右边边分区进行排序, 经过划分后，中心元素实际上已经是在最终的有序位置上面了，所以使用 partition + 1
            recQuickSort(array, partition + 1, right, asc);
            return array;
        }
    }

    /**
     * 返回分区点的下标
     *
     * @param array
     * @param left
     * @param right
     * @param asc
     * @return
     */
    private static int partition(int[] array, int left, int right, boolean asc) {
        // 此处选择中心节点直接使用最后一个元素
        int pivot = array[right];
        int leftIdx = left;
        int rightIdx = right - 1; // 排除中心节点，中心节点在最后只需要做个交换即可
        while (true) {
            if (asc) {
                // 找到第一个大于中心节点的丢到后面
                while (leftIdx < rightIdx && array[leftIdx] > pivot) {
                    leftIdx++;
                }
                // 找到右边分区第一个小于中心节点的元素位置
                while (leftIdx < rightIdx && array[rightIdx] < pivot) {
                    rightIdx--;
                }
            } else {
                // 找到第一个小于中心节点的丢到后面
                while (leftIdx < rightIdx && array[leftIdx] < pivot) {
                    leftIdx++;
                }
                // 找到右边分区第一个大于中心节点的元素位置
                while (leftIdx < rightIdx && array[rightIdx] > pivot) {
                    rightIdx--;
                }
            }
            if (leftIdx >= rightIdx) {
                break;
            } else {
                // 交换
                Utils.swap(array, leftIdx, rightIdx);
            }
        }
        // 把中心元素调整到分区中间
        Utils.swap(array, leftIdx, right);
        return leftIdx;
    }
}
