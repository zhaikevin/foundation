package com.github.foundation.es;

import com.github.foundation.es.domain.Page;
import com.github.foundation.es.domain.Pageable;
import com.github.foundation.es.query.SourceFilter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.io.Serializable;
import java.util.List;


/**
 *  * @Description: ElasticsearchRepository
 */
public interface ElasticsearchRepository<T, ID extends Serializable> {

    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
     * entity instance completely.
     * @param entity must not be {@literal null}.
     * @return the saved entity will never be {@literal null}.
     */
    <S extends T> S save(S entity);

    /**
     * Saves all given entities.
     * @param entities must not be {@literal null}.
     * @return the saved entities will never be {@literal null}.
     * @throws IllegalArgumentException in case the given entity is {@literal null}.
     */
    <S extends T> void saveAll(List<S> entities);

    /**
     * Retrieves an entity by its id.
     * @param id must not be {@literal null}.
     */
    T findById(ID id);

    /**
     * Deletes the entity with the given id.
     * @param id must not be {@literal null}.
     * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
     */
    void deleteById(ID id);

    /**
     * Deletes the given entities.
     * @param ids
     * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
     */
    void deleteAll(List<ID> ids);


    /**
     * Returns the number of entities available.
     * @return the number of entities
     */
    long count();


    /**
     * @param query
     * @return Iterable
     * @Description: 仅包含查询条件, 不含分页信息
     */
    Iterable<T> search(QueryBuilder query);

    /**
     * @param query
     * @param pageable
     * @return
     * @Description: 包含分页的查询
     */
    Page<T> search(QueryBuilder query, Pageable pageable);


    /**
     * @param source
     * @return Iterable<T>
     * @Description: 根据 json 串查询列表
     */
    Iterable<T> search(String source);


    /**
     * @param source
     * @param pageable
     * @return Page<T>
     * @Description: 根据 json 查询分页列表
     */
    Page<T> search(String source, Pageable pageable);


    /**
     * @param query
     * @param sourceFilter
     * @return
     * @Description: 带过滤条件
     */
    Iterable<T> search(QueryBuilder query, SourceFilter sourceFilter);

    /**
     * 聚合查询
     * *注* 目前只支持一个聚合字段
     * @param query          查询条件
     * @param agg            聚合条件
     * @param sort           排序
     * @param groupAliasName 聚合字段别名
     * @return 结果集
     */
    List<T> searchWithGroupBy(QueryBuilder query, AggregationBuilder agg, SortBuilder<?> sort, String groupAliasName);

    /**
     * 获取实体类的class
     * @return
     */
    Class<T> getEntityClass();

    /**
     * 设置索引名字
     * @param indexName
     */
    void setIndexName(String indexName);

}
