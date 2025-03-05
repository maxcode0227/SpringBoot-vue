package com.maxcode.buyer.controller.pagination;

import java.util.List;

public class PaginationMultiTypeValuesHelper {

    private Integer count, page;

    private Object results;

    private Long total;

    private Integer currentPage;

    private Integer totalPage;

    public void setCount(Integer name) {
        this.count = name;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
