package com.zff.springboot_demo.dto;

import java.util.List;

/**
 * 分页响应 DTO，统一封装列表数据和总条数。
 * @param <T> 列表元素类型
 */
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
