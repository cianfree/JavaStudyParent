package com.bxtpw.common.web.page;

import com.bxtpw.common.web.view.ViewBase;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.util.List;

/**
 * 查询结果，返回到页面供bootstrap-table调用
 *
 * @author 夏集球
 * @version 0.1
 * @time 2015年7月20日 下午2:44:10
 * @since 0.1
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -3253838381359287414L;

    /**
     * 总数
     */
    @JsonView(ViewBase.class)
    private int total;

    /**
     * 记录
     */
    @JsonView(ViewBase.class)
    private List<T> rows;

    public PageResult() {
        super();
    }

    public PageResult(int total, List<T> rows) {
        super();
        this.total = total;
        this.rows = rows;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
