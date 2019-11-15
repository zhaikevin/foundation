package com.github.foundation.es;

import com.github.foundation.common.exception.DataAccessException;
import com.github.foundation.es.core.ElasticsearchOperations;
import com.github.foundation.es.domain.Page;
import com.github.foundation.es.domain.Pageable;
import com.github.foundation.es.query.GetQuery;
import com.github.foundation.es.query.IndexQuery;
import com.github.foundation.es.query.NativeSearchQuery;
import com.github.foundation.es.query.SourceFilter;
import com.github.foundation.es.query.StringQuery;
import com.google.common.base.Preconditions;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  * @Description: AbstractElasticsearchRepository
 */
public abstract class AbstractElasticsearchRepository<T, ID extends Serializable> implements ElasticsearchRepository<T, ID> {

    static final Logger LOGGER = LoggerFactory.getLogger(AbstractElasticsearchRepository.class);
    protected ElasticsearchOperations elasticsearchOperations;
    protected Class<T> entityClass;

    public AbstractElasticsearchRepository(ElasticsearchOperations elasticsearchOperations) {
        Preconditions.checkNotNull(elasticsearchOperations, "ElasticsearchOperations must not be null!");
        this.entityClass = getEntityClass();
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public <S extends T> S save(S entity) {
        IndexQuery indexQuery = new IndexQuery(entity);
        elasticsearchOperations.index(indexQuery);
        return entity;
    }

    @Override
    public <S extends T> void saveAll(List<S> entities) {
        List<IndexQuery> list = entities
                .stream()
                .map(item -> new IndexQuery(item))
                .collect(Collectors.toList());

        elasticsearchOperations.bulkIndex(list);
    }

    @Override
    public T findById(ID id) {
        GetQuery query = new GetQuery();
        query.setId(String.valueOf(id));
        return elasticsearchOperations.queryForObject(query, entityClass);
    }

    @Override
    public void deleteById(ID id) {
        elasticsearchOperations.delete(entityClass, String.valueOf(id));
    }

    @Override
    public void deleteAll(List<ID> ids) {
        List<String> translateIds = ids.stream().map(String::valueOf).collect(Collectors.toList());
        elasticsearchOperations.bulkDelete(translateIds, entityClass);
    }

    @Override
    public long count() {
        return elasticsearchOperations.countAll(entityClass);
    }

    @Override
    public Iterable<T> search(QueryBuilder query) {
        NativeSearchQuery searchQuery = new NativeSearchQuery(query);
        return elasticsearchOperations.queryForList(searchQuery, entityClass);
    }

    @Override
    public Iterable<T> search(QueryBuilder query, SourceFilter sourceFilter) {
        NativeSearchQuery searchQuery = new NativeSearchQuery(query);
        searchQuery.addSourceFilter(sourceFilter);
        return elasticsearchOperations.queryForList(searchQuery, entityClass);
    }

    @Override
    public Page<T> search(QueryBuilder queryBuilder, Pageable pageable) {
        NativeSearchQuery query = new NativeSearchQuery(queryBuilder, pageable.getSortList());
        query.setPageable(pageable);
        Page<T> page = elasticsearchOperations.queryForPage(query, entityClass);
        return page;
    }

    @Override
    public Iterable<T> search(String source) {
        StringQuery stringQuery = new StringQuery(source);
        return elasticsearchOperations.queryForList(stringQuery, entityClass);
    }

    @Override
    public Page<T> search(String source, Pageable pageable) {
        StringQuery stringQuery = new StringQuery(source, pageable);
        return elasticsearchOperations.queryForPage(stringQuery, entityClass);
    }

    @Override
    public List<T> searchWithGroupBy(QueryBuilder query, AggregationBuilder agg, SortBuilder<?> sort, String groupAliasName) {
        return elasticsearchOperations.searchWithGroupBy(query, entityClass, agg, sort, groupAliasName);
    }

    @Override
    public Class<T> getEntityClass() {
        if (entityClass == null) {
            try {
                entityClass = resolveReturnedClassFromGenericType();
            } catch (Exception e) {
                LOGGER.error("Unable to resolve EntityClass. Please use according setter!:", e.getMessage(), e);
                throw new DataAccessException("Unable to resolve EntityClass. Please use according setter!", e);
            }
        }

        return entityClass;
    }

    private Class<T> resolveReturnedClassFromGenericType() {
        ParameterizedType parameterizedType = resolveReturnedClassFromGenericType(getClass());
        return (Class<T>) parameterizedType.getActualTypeArguments()[0];
    }

    private ParameterizedType resolveReturnedClassFromGenericType(Class<?> clazz) {

        Object genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
            return parameterizedType;
        }

        return resolveReturnedClassFromGenericType(clazz.getSuperclass());
    }
}
