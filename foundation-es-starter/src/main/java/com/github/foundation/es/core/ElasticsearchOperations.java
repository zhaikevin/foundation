package com.github.foundation.es.core;
import com.github.foundation.es.domain.Page;
import com.github.foundation.es.query.GetQuery;
import com.github.foundation.es.query.IndexQuery;
import com.github.foundation.es.query.SearchQuery;
import com.github.foundation.es.query.StringQuery;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.sort.SortBuilder;

import java.util.List;

/**
 *  * @Description: Elasticsearch 操作操作接口类
 */
public interface ElasticsearchOperations {

    /**
     * 索引一个对象，保存或者更新
     *
     * @param query 索引
     * @return returns the document id
     */
    String index(IndexQuery query);

    /**
     * 批量索引所有的对象，会执行保存或更新的操作
     *
     * @param list 索引请求列表
     */
    void bulkIndex(List<IndexQuery> list);

    /**
     * 根据 id 删除指定的对象
     *
     * @param clazz 实体类
     * @param id    索引 id
     * @return 被删除文档的 id
     */
    <T> String delete(Class<T> clazz, String id);

    /**
     * 执行 es 查询，返回首个对象
     *
     * @param query getQuery
     * @param clazz 查询的实体类
     * @return the first matching object
     */
    <T> T queryForObject(GetQuery query, Class<T> clazz);


    /**
     * 批量删除 list
     *
     * @param ids   删除的 id 列表
     * @param clazz 实体类
     */
    <T> void bulkDelete(List<String> ids, Class<T> clazz);

    /**
     * 返回所有的文档数
     *
     * @param clazz 被计算的实体类
     * @return 返回所有的文档数量
     */
    <T> long countAll(Class<T> clazz);

    /**
     * 输入为 searchRequest 的查询接口
     *
     * @param request 源查询构建类
     */
    void search(SearchRequest request);

    /**
     * Execute the query against elasticsearch and return result as Page
     *
     * @param query searchQuery
     * @param clazz 实体类
     * @return Page 分页结果
     */
    <T> Page<T> queryForPage(SearchQuery query, Class<T> clazz);


    /**
     * Execute the query against elasticsearch and return result as Page
     *
     * @param query StringQuery
     * @param clazz 实体类
     * @return Page 查询结果
     */
    <T> Page<T> queryForPage(StringQuery query, Class<T> clazz);

    /**
     * Execute the search query against elasticsearch and return result as List
     *
     * @param query SearchQuery
     * @param clazz 实体类
     * @return List 查询结果列表
     */
    <T> List<T> queryForList(SearchQuery query, Class<T> clazz);

    /**
     * Execute the string query against elasticsearch and return result as List
     *
     * @param query stringQuery
     * @param clazz 实体类
     * @return List 查询结果列表
     */
    <T> List<T> queryForList(StringQuery query, Class<T> clazz);

    /**
     * 聚合查询
     * *注* 目前只支持一个聚合字段
     *
     * @param query          查询条件
     * @param clazz          结果集实体类
     * @param agg            聚合条件
     * @param sort           排序
     * @param groupAliasName 聚合字段别名
     * @return 结果集
     */
    <T> List<T> searchWithGroupBy(QueryBuilder query, Class<T> clazz, AggregationBuilder agg, SortBuilder<?> sort, String groupAliasName);

    /**
     * sum，单个
     * @param query 查询条件
     * @param clazz 结果类型
     * @param sumAgg 聚合条件
     * @param <T> 需要转换的类
     * @return 结果
     */
    <T> T sum(QueryBuilder query, Class<T> clazz, AggregationBuilder sumAgg);

    /**
     * sum，多个
     * @param query 查询条件
     * @param clazz 结果类型
     * @param sumAggList 聚合条件
     * @param <T> 需要转换的类
     * @return 结果
     */
    <T> T sum(QueryBuilder query, Class<T> clazz, List<AggregationBuilder> sumAggList);

}
