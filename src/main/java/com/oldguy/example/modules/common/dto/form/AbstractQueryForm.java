package com.oldguy.example.modules.common.dto.form;


import com.baomidou.mybatisplus.plugins.Page;

/**
 * @author huangrenhao
 * @date 2018/11/26
 */
public abstract class AbstractQueryForm {

    /**
     * 模糊搜索字段
     */
    private String queryText;

    /**
     * 排序：
     * 1 - > id 倒序
     * 0 - > id 正序
     */
    private Integer sort;

    /**
     *  排序列明
     */
    private String sortColumn;

    /**
     * 状态：
     * 1 - > 正常
     * 0 - > 禁用
     */
    private Integer status;

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
