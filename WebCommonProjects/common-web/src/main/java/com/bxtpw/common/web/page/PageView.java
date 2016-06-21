package com.bxtpw.common.web.page;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 * 分页对象，包含页码
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 18:10
 * @since 0.1
 */
public class PageView<T> implements Serializable {

    /**
     * 指定当前页
     */
    private long pageNumber;
    /**
     * 指定每一页显示多少条记录
     */
    private long pageSize;

    /**
     * 分页条显示多少个
     */
    private long showSize = 10;

    // ----------------------------------------

    /**
     * 查询结果的总记录数目
     */
    private long total;
    /**
     * 查询结果，保存了本页的查询列表
     */
    private List<T> rows;

    // -------------计算-------------------
    /**
     * 总页数，通过总记录数目和每页显示的记录数计算
     */
    private long pageCount;
    /**
     * 页码列表的开始索引以便产生下面的效果：<br/>
     * <font color=red>首页 4 5 6 <a href="#">7</a> 8 9 10 尾页</font> <br/>
     * 其中这里的4就是开始的索引
     */
    private long begIndex;
    /**
     * 结束索引，和开始索引对应
     */
    private long endIndex;

    /**
     * 只接受前4个必要的属性，会自动的计算出其他3个属性的值
     *
     * @param pageNumber 当前页
     * @param pageSize   每页的大小
     * @param total      总记录数目
     * @param rows       查询的结果
     */
    public PageView(int pageNumber, int pageSize, int total, List<T> rows) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.total = total;
        this.rows = rows;

        // 计算总页码
        pageCount = (total + pageSize - 1) / pageSize;

        // 计算 beginPageIndex 和 endPageIndex
        // >> 总页数不多于showSize页，则全部显示
        if (pageCount <= showSize) {
            begIndex = 1;
            endIndex = pageCount;
        }
        // >> 总页数多于 showSize 页，则显示当前页附近的共 showSize 个页码
        else {
            // 计算 左边显示数量和右边显示数量
            long leftSize = 0;
            long rightSize = 0;

            if (showSize % 2 == 0) {
                leftSize = showSize / 2 - 1;
                rightSize = leftSize + 1;
            } else {
                leftSize = (showSize - 1) / 2;
                rightSize = leftSize;
            }

            if (this.pageNumber - leftSize <= 0) {
                this.begIndex = 1;
                rightSize += (leftSize - this.pageNumber + 1);
            } else {
                begIndex = this.pageNumber - leftSize;
            }

            if (this.pageNumber + rightSize > this.pageCount) {
                endIndex = pageCount;
                begIndex -= (this.pageNumber + rightSize - this.pageCount);
            } else {
                endIndex = this.pageNumber + rightSize;
            }
            if (begIndex <= 0) begIndex = 1;
            if (endIndex > pageCount) endIndex = pageCount;
        }
    }

    public long getPageNumber() {
        return pageNumber;
    }

    public PageView setPageNumber(long pageNumber) {
        this.pageNumber = pageNumber;
        return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public PageView setPageSize(long pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public long getShowSize() {
        return showSize;
    }

    public PageView setShowSize(long showSize) {
        this.showSize = showSize;
        return this;
    }

    public long getTotal() {
        return total;
    }

    public PageView setTotal(long total) {
        this.total = total;
        return this;
    }

    public List<T> getRows() {
        return rows;
    }

    public PageView setRows(List<T> rows) {
        this.rows = rows;
        return this;
    }

    public long getPageCount() {
        return pageCount;
    }

    public PageView setPageCount(long pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    public long getBegIndex() {
        return begIndex;
    }

    public PageView setBegIndex(long begIndex) {
        this.begIndex = begIndex;
        return this;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public PageView setEndIndex(long endIndex) {
        this.endIndex = endIndex;
        return this;
    }

    /**
     * 获取上一页
     *
     * @return
     */
    public long getPrePage() {
        return this.pageNumber > 1 ? this.pageNumber - 1 : this.pageNumber;
    }

    /**
     * 获取下一页
     *
     * @return
     */
    public long getNextPage() {
        return this.pageNumber == pageCount ? pageCount : this.pageNumber + 1;
    }
}
