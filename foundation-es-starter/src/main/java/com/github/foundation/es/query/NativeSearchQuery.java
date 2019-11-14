package com.github.foundation.es.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;

/**
 *  * @Description: NativeSearchQuery
 */
public class NativeSearchQuery extends AbstractQuery implements SearchQuery {

    private QueryBuilder query;
    private List<SortBuilder<?>> sorts;


    public NativeSearchQuery(QueryBuilder query) {
        this.query = query;
    }

    public NativeSearchQuery(QueryBuilder query, List<SortBuilder<?>> sorts) {
        this.query = query;
        this.sorts = sorts;
    }

    @Override
    public QueryBuilder getQuery() {
        return this.query;
    }

    @Override
    public List<SortBuilder<?>> getElasticsearchSorts() {
        return sorts;
    }
}
