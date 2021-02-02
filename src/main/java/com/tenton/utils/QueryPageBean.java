package com.tenton.utils;

/**
 * @Date: 2021/1/31
 * @Author: Tenton
 * @Description:
 */
public class QueryPageBean {
    /**
     * 需要显示的页码
     */
    private Integer currentPage;
    /**
     * 每页记录数
     */
    private Integer pageSize;
    /**
     * 查询条件
     */
    private String queryString;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
