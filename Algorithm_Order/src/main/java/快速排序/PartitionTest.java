package 快速排序;

import utils.Utils;

/**
 * 划分测试
 *
 * @author Arvin
 * @time 2017/2/10 10:46
 */
public class PartitionTest {

    public static void main(String[] args) {
        int[] array = Utils.getArray2();
        System.out.println("没有划分之前： ");
        Utils.show(array);

        int pivot = 6; // 中间值
        int left = partition(array, 0, array.length - 1, pivot);

        System.out.println("划分之后： ");
        System.out.println("划分下标：" + left);
        Utils.show(array);
    }

    /**
     * 划分，返回划分的节点下标
     *
     * @param array
     * @param left
     * @param right
     * @param pivot
     * @return
     */
    private static int partition(int[] array, int left, int right, int pivot) {
        int leftIdx = left;
        int rightIdx = right;
        while (true) {
            // 从左边开始找到第一个比pivot大的元素，记录下标
            while (leftIdx < rightIdx && array[leftIdx] < pivot) {
                leftIdx++;
            }
            // 从右边第一个开始找比pivot小的元素，记录下标
            while (rightIdx > leftIdx && array[rightIdx] > pivot) {
                --rightIdx;
            }
            if (leftIdx >= rightIdx) {
                break;
            } else {
                // 交换位置
                Utils.swap(array, leftIdx, rightIdx);
            }
        }
        // 注意返回的是比 pivot 大的下标
        return leftIdx;
    }
}
