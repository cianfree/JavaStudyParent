package com.github.search.simple.model;

import java.util.List;

/**
 * @author Arvin
 * @time 2016/10/11$ 10:20$
 */
public class SearchResult {

    /** 第几页数据 */
    private final int pageNo;

    /** 每一页返回的数据 */
    private final int pageSize;

    /** 总数目 */
    private final int total;

    /** 总页数 */
    private final int totalPage;

    /** 最后一页差多少才满一页 */
    private final int lastRemain;

    /** 结果列表 */
    private List<ResultItem> resultItemList;

    public SearchResult(int pageNo, int pageSize, int total, List<ResultItem> resultItemList) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.total = total;
        this.resultItemList = resultItemList;
        this.totalPage = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        this.lastRemain = this.total % pageSize == 0 ? 0 : this.pageSize - this.total % pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public List<ResultItem> getResultItemList() {
        return resultItemList;
    }

    public int getLastRemain() {
        return lastRemain;
    }
}
