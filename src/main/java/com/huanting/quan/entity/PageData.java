package com.huanting.quan.entity;

import java.util.List;

/**
 * @author: xiexinquan520@163.com
 * User: XieXinQuan
 * DATE:2020/1/26
 */
public class PageData<T> {
    private Integer page;
    private Integer size;
    private List<T> data;
    private Integer totalPage;
    private long totalNum;

    public PageData(Integer page, Integer size, List<T> data, Integer totalPage, long totalNum) {
        this.page = page;
        this.size = size;
        this.data = data;
        this.totalPage = totalPage;
        this.totalNum = totalNum;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    public List<T> getData() {
        return data;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public long getTotalNum() {
        return totalNum;
    }

    @Override
    public String toString() {
        return "Page{" +
                "page=" + page +
                ", size=" + size +
                ", data=" + data +
                ", totalPage=" + totalPage +
                ", totalNum=" + totalNum +
                '}';
    }
}
