package com.github.foundation.pagination.model;

import java.util.List;

/**
 * 分页模型数据
 */
public class Pagination<T> {
    /**
     * 当前页
     */
    private final int page;
    /**
     * 每页显示行数
     */
    private final int pageSize;

    /**
     * 排序对象
     */
    private final Sort sort;

    /**
     * 总记录数，用于查询结果返回
     */
    private long total;

    /**
     * 查询结果数据，用于查询结果返回
     */
    private List<T> dataset;

    /**
     * 查询参数
     */
    private SearchParams params;

    /**
     * 创建新的分页数据model {@link Pagination}。<br>
     * 页数从0开始计数，因此如果指定为0，则返回第一页数据。
     * @param page     当前页号
     * @param pageSize 每页记录数
     */
    public Pagination(int page, int pageSize) {
        this(page, pageSize, null);
    }

    /**
     * 依据给定的排序信息创建分页数据model {@link Pagination}.
     * @param page       当前页号
     * @param pageSize   每页行数
     * @param direction  排序方向，{@link }}
     * @param properties 排序的字段
     */
    public Pagination(int page, int pageSize, Direction direction, String... properties) {
        this(page, pageSize, new Sort(direction, properties));
    }

    /**
     * 依据给定的排序信息创建分页数据model {@link Pagination}..
     * @param page     当前页号
     * @param pageSize 每页行数
     * @param sort     排序信息
     */
    public Pagination(int page, int pageSize, Sort sort) {
        if (0 > page) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        if (0 >= pageSize) {
            throw new IllegalArgumentException("Page pageSize must not be less than or equal to zero!");
        }

        this.page = page;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    /**
     * 设置查询结果信息
     * @param dataset
     * @param total
     */
    public void setResultInfo(List<T> dataset, long total) {
        this.dataset = dataset;
        this.total = total;
    }

    /**
     * 获取记录偏移量
     * @return 记录偏移量
     */
    public int getOffset() {
        return (page - 1) * pageSize;
    }

    /**
     * @return the total
     */
    public long getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * @return the dataset
     */
    public List<T> getDataset() {
        return dataset;
    }

    /**
     * @param dataset the dataset to set
     */
    public void setDataset(List<T> dataset) {
        this.dataset = dataset;
    }

    /**
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * @return the pageSize
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * @return the sort
     */
    public Sort getSort() {
        return sort;
    }

    public SearchParams getParams() {
        return params;
    }

    public void setParams(SearchParams params) {
        this.params = params;
    }

    public void addParam(SearchParams.Param param) {
        if (params == null) {
            params = new SearchParams();
        }
        params.addParam(param);
    }
}
