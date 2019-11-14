package com.github.foundation.es.query;

import com.github.foundation.es.domain.Pageable;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;

/**
 *  * @Description: StringQuery
 */
public class StringQuery {

    private String source;

    private List<SortBuilder<?>> sortList;

    private Pageable pageable;

    public StringQuery(String source) {
        this.source = source;
    }

    public StringQuery(String source, Pageable pageable) {
        this.source = source;
        this.pageable = pageable;
    }

    public StringQuery(String source, Pageable pageable, List<SortBuilder<?>> sortList) {
        this.pageable = pageable;
        this.sortList = sortList;
        this.source = source;
    }

    public List<SortBuilder<?>> getSortList() {
        return sortList;
    }

    public void setSortList(List<SortBuilder<?>> sortList) {
        this.sortList = sortList;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}

