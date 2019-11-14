package com.github.foundation.es.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;

/**
 *  * @Description: SearchQuery
 */
public interface SearchQuery extends Query {

    QueryBuilder getQuery();

    List<SortBuilder<?>> getElasticsearchSorts();

}
