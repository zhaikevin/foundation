package com.github.foundation.es.query;

import com.github.foundation.es.domain.Pageable;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.support.IndicesOptions;

import java.util.Collection;
import java.util.List;


/**
 *  * @Description: Query interface
 */
public interface Query {


    /**
     * restrict result to entries on given page. Corresponds to the 'start' and 'rows' parameter in elasticsearch
     * @param pageable
     * @return
     */
    <T extends Query> T setPageable(Pageable pageable);

    /**
     * Get page settings if defined
     * @return
     */
    Pageable getPageable();

    /**
     * Get Indices to be searched
     * @return
     */
    List<String> getIndices();

    /**
     * Add Indices to be added as part of search request
     * @param indices
     */
    void addIndices(String... indices);

    /**
     * Add types to be searched
     * @param types
     */
    void addTypes(String... types);

    /**
     * Get types to be searched
     * @return
     */
    List<String> getTypes();

    /**
     * Add fields to be added as part of search request
     * @param fields
     */
    void addFields(String... fields);

    /**
     * Get fields to be returned as part of search request
     * @return
     */
    List<String> getFields();

    /**
     * Add source filter to be added as part of search request
     * 添加源过滤
     * @param sourceFilter
     */
    void addSourceFilter(SourceFilter sourceFilter);

    /**
     * Get SourceFilter to be returned to get include and exclude source fields as part of search request.
     * 获取源过滤
     * @return SourceFilter
     */
    SourceFilter getSourceFilter();


}
