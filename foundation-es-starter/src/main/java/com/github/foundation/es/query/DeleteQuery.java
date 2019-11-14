package com.github.foundation.es.query;

import org.elasticsearch.index.query.QueryBuilder;

/**
 *  * @Description: DeleteQuery
 */
public class DeleteQuery {

    private QueryBuilder query;
    private String index;
    private String type;
    private Integer pageSize;
    private Long scrollTimeInMillis;

    public QueryBuilder getQuery() {
        return query;
    }

    public void setQuery(QueryBuilder query) {
        this.query = query;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getScrollTimeInMillis() {
        return scrollTimeInMillis;
    }

    public void setScrollTimeInMillis(Long scrollTimeInMillis) {
        this.scrollTimeInMillis = scrollTimeInMillis;
    }
}
