package com.bxtpw.common.web.query;

import java.io.Serializable;

/**
 * <pre>
 * 查询分页基类
 * </pre>
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015/12/18 17:23
 * @since 0.1
 */
public abstract class PageBaseQuery implements Serializable {

    /**
     * 第几页
     */
    private int pageNumber;

    /**
     * 每一页多少记录
     */
    private int pageSize;

    public void setPageNumber(int pageNumber) {
        if (pageNumber > 0) {
            this.pageNumber = pageNumber;
        }
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageSize(int pageSize) {
        if (pageSize > 0) {
            this.pageSize = pageSize;
        }
    }

    public int getPageSize() {
        return pageSize;
    }
}
