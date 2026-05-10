package com.zff.springboot_demo.dto;

import java.util.List;

public class PageResult<T> {

    private List<T> list;

    private long total;

    public PageResult(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
