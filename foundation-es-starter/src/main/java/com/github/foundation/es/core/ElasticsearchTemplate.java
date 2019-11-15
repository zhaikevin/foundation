package com.github.foundation.es.core;

import com.github.foundation.es.annotations.Document;
import com.github.foundation.es.annotations.Id;
import com.github.foundation.es.domain.Page;
import com.github.foundation.es.domain.PageImpl;
import com.github.foundation.es.domain.PageRequest;
import com.github.foundation.es.domain.Pageable;
import com.github.foundation.es.query.GetQuery;
import com.github.foundation.es.query.IndexQuery;
import com.github.foundation.es.query.NativeSearchQuery;
import com.github.foundation.es.query.SearchQuery;
import com.github.foundation.es.query.SourceFilter;
import com.github.foundation.es.query.StringQuery;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *  * @Description: Elasticsearch 操作实现类
 */
public class ElasticsearchTemplate implements ElasticsearchOperations {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchTemplate.class);

    /**
     * request option
     */
    private RequestOptions options;

    /**
     * 日期格式
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * 批量请求超时时长
     */
    private static final String BULK_REQUEST_TIMEOUT = "2m";

    /**
     * utf-8 编码
     */
    private static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * 实体类 id 字段名称
     */
    private static final String ID_NAME = "id";

    /**
     * 最大窗口长度
     */
    private int MAX_WINDOW_SIZE = 10000;

    /**
     * rest 请求 high level 客户端
     */
    private RestHighLevelClient client;

    /**
     * 索引名字
     */
    private String indexName;

    /**
     * gson intend to parse the es response
     */
    private static final Gson gson = new GsonBuilder().serializeNulls().setDateFormat(DATE_FORMAT)
            .registerTypeHierarchyAdapter(Date.class, new JsonSerializer<Date>() {
                @Override
                public JsonElement serialize(Date date, Type typeOfSrc, JsonSerializationContext context) {
                    if (date == null) {
                        return JsonNull.INSTANCE;
                    }

                    return new JsonPrimitive(DateFormatUtils.format(date, DATE_FORMAT));
                }
            }).create();

    /**
     * 构造函数
     * @param client RestHighLevelClient 客户端
     */
    public ElasticsearchTemplate(RestHighLevelClient client) {
        Preconditions.checkNotNull(client);
        this.client = client;
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        builder.addHeader("Content-Type", "application/json");
        builder.setHttpAsyncResponseConsumerFactory(
                new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(100 * 1024 * 1024));
        this.options = builder.build();
    }

    @Override
    public String index(IndexQuery query) {
        IndexRequest indexRequest = this.prepareIndex(query);
        if (indexRequest == null) {
            return null;
        }

        try {
            IndexResponse indexResponse = client.index(indexRequest, options);
            String index = indexResponse.getIndex();
            String id = indexResponse.getId();
            long version = indexResponse.getVersion();
            if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                // 文档第一次创建
                LOGGER.debug("elasticsearch index created. index:{},  id:{}, version:{}", index, id,
                        version);
            } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                // 文档更新
                LOGGER.debug("elasticsearch index updated. index:{},  id:{}, version:{}", index, id,
                        version);
            }
            ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
            if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                // 成功的分片数量少于总分片数量 
                LOGGER.warn("elasticsearch index shadinfo warning. index:{}, id:{}, version:{}", index,
                        id, version);
            }
            if (shardInfo.getFailed() > 0) {
                for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                    // 处理潜在的失败信息
                    String reason = failure.reason();
                    LOGGER.warn("elasticsearch index shadinfo failure. index:{}, id:{}, version:{}, reason:{}",
                            index, id, version, reason);
                }
            }
            if (query.getObject() != null) {
                setPersistentEntityId(query.getObject(), id);
            }
            return id;
        } catch (Exception e) {
            LOGGER.error("index exception", e);
            throw new ElasticsearchException("index exception", e);
        }
    }

    private void setPersistentEntityId(Object entity, String documentId) throws IllegalAccessException {
        Class<?> clazz = entity.getClass();
        for (Field f : clazz.getDeclaredFields()) {
            Id id = f.getAnnotation(Id.class);
            if (id != null && f.getType().isAssignableFrom(String.class)) {
                f.setAccessible(true);
                f.set(entity, documentId);
            }
        }
    }

    /**
     * 构建 index
     * @param query 索引查询
     * @return IndexRequest 索引请求
     */
    private IndexRequest prepareIndex(IndexQuery query) {
        try {
            String indexName = StringUtils.isEmpty(query.getIndexName())
                    ? retrieveIndexNameFromPersistentEntity(query.getObject().getClass())
                    : query.getIndexName();

            String id = query.getId();
            if (StringUtils.isEmpty(id)) {
                id = getPersistentEntityId(query.getObject());
            }

            IndexRequest indexRequest = new IndexRequest(indexName);
            indexRequest.id(id);

            // source 变量只能是 json 格式
            String jsonSource = query.getSource();
            if (StringUtils.isNotBlank(query.getSource())) {
                indexRequest.source(jsonSource, XContentType.JSON);
                return indexRequest;
            }

            jsonSource = gson.toJson(query.getObject());
            if (StringUtils.isBlank(jsonSource)) {
                throw new Exception("failed to get json source");
            }
            indexRequest.source(jsonSource, XContentType.JSON);
            return indexRequest;
        } catch (Exception e) {
            LOGGER.error("Exception", e);
            throw new ElasticsearchException("failed to index the document [id: " + query.getId() + "]", e);
        }
    }

    /**
     * 获取持久化实体的索引名称, 从  @Document 注解中获取
     * @param clazz 实体类
     * @return index name 索引名称
     */
    private String retrieveIndexNameFromPersistentEntity(Class<?> clazz) {
        if (this.indexName != null) {
            return this.indexName;
        }
        if (clazz != null) {
            Document document = clazz.getAnnotation(Document.class);
            if (document != null) {
                this.indexName = document.indexName();
            }
        }
        return this.indexName;
    }

    /**
     * 获取持久化实体 Id
     * @param entity 要获取 id 的实体
     * @return 持久化实体 Id
     * @throws IllegalAccessException   字段访问异常
     * @throws IllegalArgumentException 参数非法异常
     */
    private String getPersistentEntityId(Object entity) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = entity.getClass();

        for (Field f : clazz.getDeclaredFields()) {
            Id id = f.getAnnotation(Id.class);
            if (id != null) {
                f.setAccessible(true);
                Object idStr = f.get(entity);
                return (String) idStr;
            }
        }

        return null;
    }

    @Override
    public void bulkIndex(List<IndexQuery> queries) {
        BulkRequest bulkRequest = prepareBulkRequest();

        for (IndexQuery query : queries) {
            bulkRequest.add(prepareIndex(query));
        }

        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, options);

            // 遍历所有的操作结果
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                // 获取操作结果的响应，可以是  IndexResponse, UpdateResponse or DeleteResponse
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.INDEX
                        || bulkItemResponse.getOpType() == DocWriteRequest.OpType.CREATE) {
                    // index 操作后的响应结果
                    IndexResponse indexResponse = (IndexResponse) itemResponse;

                    String index = indexResponse.getIndex();
                    String type = indexResponse.getType();
                    String id = indexResponse.getId();
                    long version = indexResponse.getVersion();

                    LOGGER.debug("elasticsearch index created. index:{}, type:{}, id:{}, version:{}", index, type, id,
                            version);

                } else if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.UPDATE) {
                    // update 操作后的响应结果
                    UpdateResponse updateResponse = (UpdateResponse) itemResponse;
                    String index = updateResponse.getIndex();
                    String type = updateResponse.getType();
                    String id = updateResponse.getId();
                    long version = updateResponse.getVersion();

                    LOGGER.debug("elasticsearch index updated. index:{}, type:{}, id:{}, version:{}", index, type, id,
                            version);
                }
            }
        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }
    }

    /**
     * 准备 BulkRequest
     * @return BulkRequest 批量请求
     */
    private BulkRequest prepareBulkRequest() {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout(BULK_REQUEST_TIMEOUT);
        return bulkRequest;
    }

    @Override
    public <T> String delete(Class<T> clazz, String id) {
        String index = retrieveIndexNameFromPersistentEntity(clazz);

        DeleteRequest deleteRequest = prepareDeleteRequest(index, id);
        try {
            DeleteResponse deleteResponse = client.delete(deleteRequest, options);

            if (deleteResponse.getResult() == DocWriteResponse.Result.NOT_FOUND) {
                LOGGER.warn("elasticsearch index not found. index:{}, type:{}, id:{}", index, id);
                return id;
            }

            LOGGER.debug("elasticsearch index deleted. index:{}, type:{}, id:{}", index, id);
            return id;

        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }
    }

    /**
     * 构建 DeleteRequest
     * @param index 索引
     * @param id    删除请求的 id
     * @return DeleteRequest 删除请求
     */
    private DeleteRequest prepareDeleteRequest(String index, String id) {
        return new DeleteRequest(index, id);
    }

    @Override
    public <T> T queryForObject(GetQuery query, Class<T> clazz) {

        String index = retrieveIndexNameFromPersistentEntity(clazz);

        GetRequest getRequest = new GetRequest(index, query.getId());

        try {
            GetResponse getResponse = client.get(getRequest, options);

            if (getResponse.isExists()) {
                // string 形式  
                String sourceAsString = getResponse.getSourceAsString();
                LOGGER.info("sourceString {}", sourceAsString);
                return gson.fromJson(sourceAsString, clazz);
            }
        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }

        return null;
    }

    @Override
    public <T> long countAll(Class<T> clazz) {
        String indexName = retrieveIndexNameFromPersistentEntity(clazz);

        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 添加 match_all 查询
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 配置不获取源数据
        searchSourceBuilder.fetchSource(false);
        // 将 SearchSourceBuilder  添加到 SeachRequest 中
        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse response = client.search(searchRequest, options);
            SearchHits hits = response.getHits();
            if (hits != null) {
                // 返回命中的数量
                return hits.getTotalHits().value;
            }
        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }

        return 0;
    }

    @Override
    public void search(SearchRequest request) {

        try {
            SearchResponse searchResponse = client.search(request, options);

            SearchHits hits = searchResponse.getHits();

            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                String sourceAsString = hit.getSourceAsString();
                LOGGER.info("post circle sourceAsString: {}", sourceAsString);
            }

        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }
    }

    @Override
    public <T> Page<T> queryForPage(SearchQuery query, Class<T> clazz) {
        SearchRequest searchRequest = prepareSearchRequest(query, clazz);
        SearchResponse response = doSearch(searchRequest);
        return mapResults(response, clazz, query.getPageable());
    }

    /**
     * 将 response 结果映射成 page 对象
     * @param searchResponse 搜索响应
     * @param clazz          实体类
     * @param pageable       分页信息
     * @return Page 分页结果
     */
    private <T> Page<T> mapResults(SearchResponse searchResponse, Class<T> clazz, Pageable pageable) {

        // 1. 分析结果
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();

        LOGGER.debug("sarch response status: {}, took: {}, terminatedEarly: {}, timeout: {} ", status, took,
                terminatedEarly, timedOut);
        SearchHits hits = searchResponse.getHits();

        long totalHits = hits.getTotalHits().value;

        float maxScore = hits.getMaxScore();

        LOGGER.debug("sarch response totalHits: {}, maxScore: {}", totalHits, maxScore);

        // 2. 将结果中内容映射成  clazz 对象列表， 并塞入 Page 对象中
        List<T> content = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 添加 id
            String sourceAsString = hit.getSourceAsString();
            JsonElement jelem = gson.fromJson(sourceAsString, JsonElement.class);
            JsonObject jsonObj = jelem.getAsJsonObject();
            if (jsonObj.get(ID_NAME) == null) {
                jsonObj.addProperty(ID_NAME, hit.getId());
            }

            content.add(gson.fromJson(jsonObj, clazz));
            LOGGER.info("post circle sourceAsString: {}", sourceAsString);
        }

        // 3. 将页面位置，页面大小填入 page
        Page<T> page = new PageImpl<>(content, pageable, totalHits);

        // 4. 返回 page
        return page;
    }

    /**
     * 构建  SearchRequest
     * @param query searchQuery
     * @param clazz 实体类
     * @return searchRequest 查询请求
     */
    private <T> SearchRequest prepareSearchRequest(SearchQuery query, Class<T> clazz) {
        setPersistentEntityIndex(query, clazz);

        SearchSourceBuilder searchSourceBuilder = prepareSearchSourceBuilder(query);
        SearchRequest searchRequest = new SearchRequest();

        List<String> indices = query.getIndices();
        searchRequest.indices(indices.toArray(new String[indices.size()]));
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    /**
     * 设置  class 注解中的 index, type
     * @param query 查询请求
     * @param clazz 实体类
     */
    private <T> void setPersistentEntityIndex(SearchQuery query, Class<T> clazz) {
        if (query.getIndices().isEmpty()) {
            query.addIndices(retrieveIndexNameFromPersistentEntity(clazz));
        }
    }

    /**
     * 执行查询操作，并获取响应
     * @param searchRequest 查询请求
     * @return SearchResponse 查询响应
     */
    private SearchResponse doSearch(SearchRequest searchRequest) {
        LOGGER.info("searchRequest: {}", searchRequest.toString());
        try {
            SearchResponse searchResponse = client.search(searchRequest, options);
            return searchResponse;
        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }
    }

    /**
     * 构建 searchSourceBuilder
     * @param query 查询请求
     * @return SearchSourceBuilder 查询请求构建结果
     */
    private SearchSourceBuilder prepareSearchSourceBuilder(SearchQuery query) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 配置 queryBuilder
        searchSourceBuilder.query(query.getQuery());

        Pageable pageable = query.getPageable();

        // 表示  pagable 不为空
        if (pageable != null) {
            // 分页起始点
            int from = pageable.getOffset();
            searchSourceBuilder.from(from);

            // 分页大小
            int size = pageable.getPageSize();
            searchSourceBuilder.size(size);
        }

        // 配置排序
        List<SortBuilder<?>> sortList = query.getElasticsearchSorts();
        if (sortList != null && sortList.size() > 0) {
            sortList.stream().forEach(searchSourceBuilder::sort);
        }

        // 配置要获取的数据
        SourceFilter sourceFilter = query.getSourceFilter();
        if (sourceFilter != null) {
            String[] includeFields = sourceFilter.getIncludes();
            String[] excludeFields = sourceFilter.getExcludes();
            searchSourceBuilder.fetchSource(includeFields, excludeFields);
        }

        searchSourceBuilder.fetchSource(true);

        return searchSourceBuilder;
    }

    @Override
    public <T> Page<T> queryForPage(StringQuery query, Class<T> clazz) {
        SearchRequest searchRequest = prepareSearchRequest(query, clazz);
        SearchResponse response = doSearch(searchRequest);
        if (response == null) {
            // 表示出了 IOException
            return Page.empty();
        }

        return mapResults(response, clazz, query.getPageable());
    }

    @Override
    public <T> void bulkDelete(List<String> ids, Class<T> clazz) {

        final String index = retrieveIndexNameFromPersistentEntity(clazz);

        List<DeleteRequest> deleteList = ids.stream().map(id -> prepareDeleteRequest(index, id))
                .collect(Collectors.toList());

        final BulkRequest bulkRequest = prepareBulkRequest();
        deleteList.stream().forEach(deleteItem -> bulkRequest.add(deleteItem));

        try {
            BulkResponse bulkResponse = client.bulk(bulkRequest, options);

            // 遍历所有的操作结果
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                // 获取操作结果的响应，可以是  IndexResponse, UpdateResponse or DeleteResponse, 
                // 它们都可以惭怍是 DocWriteResponse 实例
                DocWriteResponse itemResponse = bulkItemResponse.getResponse();

                if (bulkItemResponse.getOpType() == DocWriteRequest.OpType.DELETE) {
                    // delete 操作后的响应结果
                    DeleteResponse deleteResponse = (DeleteResponse) itemResponse;

                    String deleteId = deleteResponse.getId();
                    String deleteIndex = deleteResponse.getIndex();

                    LOGGER.debug("delete response id: {}, index: {}", deleteId, deleteIndex);
                }
            }

        } catch (IOException e) {
            LOGGER.error("index IOException", e);
            throw new ElasticsearchException("index IOException", e);
        }
    }

    @Override
    public <T> List<T> queryForList(SearchQuery query, Class<T> clazz) {
        // 如果 pageable 为空，设置成 int 的最大值
        if (query.getPageable() == null) {
            query.setPageable(PageRequest.of(0, MAX_WINDOW_SIZE));
        }

        SearchRequest searchRequest = prepareSearchRequest(query, clazz);
        SearchResponse response = doSearch(searchRequest);
        return mapResults(response, clazz);
    }

    /**
     * 将 response 结果映射成 page 对象
     * @param searchResponse 查询响应
     * @param clazz          实体类
     * @return content 映射结果列表
     */
    private <T> List<T> mapResults(SearchResponse searchResponse, Class<T> clazz) {

        // 1. 分析结果
        RestStatus status = searchResponse.status();
        TimeValue took = searchResponse.getTook();
        Boolean terminatedEarly = searchResponse.isTerminatedEarly();
        boolean timedOut = searchResponse.isTimedOut();

        LOGGER.debug("sarch response status: {}, took: {}, terminatedEarly: {}, timeout: {} ", status, took,
                terminatedEarly, timedOut);
        SearchHits hits = searchResponse.getHits();

        long totalHits = hits.getTotalHits().value;

        float maxScore = hits.getMaxScore();

        LOGGER.debug("sarch response totalHits: {}, maxScore: {}", totalHits, maxScore);

        // 2. 将结果中内容映射成  clazz 对象列表， 并塞入 Page 对象中
        List<T> content = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            // 添加 id
            String sourceAsString = hit.getSourceAsString();
            JsonElement jelem = gson.fromJson(sourceAsString, JsonElement.class);
            JsonObject jsonObj = jelem.getAsJsonObject();
            if (jsonObj.get(ID_NAME) == null) {
                jsonObj.addProperty(ID_NAME, hit.getId());
            }

            content.add(gson.fromJson(jsonObj, clazz));
            LOGGER.info("post circle sourceAsString: {}", sourceAsString);
        }

        // content
        return content;
    }

    @Override
    public <T> List<T> queryForList(StringQuery query, Class<T> clazz) {
        // 如果 pageable 为空，设置成 int 的最大值
        if (query.getPageable() == null) {
            query.setPageable(PageRequest.of(0, MAX_WINDOW_SIZE));
        }
        SearchRequest searchRequest = prepareSearchRequest(query, clazz);
        SearchResponse response = doSearch(searchRequest);
        return mapResults(response, clazz);
    }

    @Override
    public <T> List<T> searchWithGroupBy(QueryBuilder query, Class<T> clazz, AggregationBuilder agg, SortBuilder<?> sort, String groupAliasName) {
        NativeSearchQuery searchQuery = new NativeSearchQuery(query);
        setPersistentEntityIndex(searchQuery, clazz);
        SearchSourceBuilder searchSourceBuilder = prepareSearchSourceBuilderWithGroupBy(query, searchQuery, sort, agg);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(searchQuery.getIndices().toArray(new String[searchQuery.getIndices().size()]));
        searchRequest.source(searchSourceBuilder);

        SearchResponse response = doSearch(searchRequest);
        return mapResults4GroupBy(response, clazz, groupAliasName);
    }

    @Override
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    /**
     * 将 response 结果映射成 结果 对象
     * *注* 目前只支持一个聚合字段
     * @param searchResponse 查询响应
     * @param clazz          实体类
     * @param groupAliasName 聚合字段别名
     * @return 结果集
     */
    private <T> List<T> mapResults4GroupBy(SearchResponse searchResponse, Class<T> clazz, String groupAliasName) {
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> aggregationsMap = aggregations.asMap();
        ParsedStringTerms stringTerms = (ParsedStringTerms) aggregationsMap.get(groupAliasName);
        List<ParsedStringTerms.ParsedBucket> buckets = (List<ParsedStringTerms.ParsedBucket>) stringTerms.getBuckets();
        if (CollectionUtils.isEmpty(buckets)) {
            return Lists.newArrayList();
        }

        List<T> re = Lists.newArrayList();
        Field[] fields = clazz.getDeclaredFields();
        for (ParsedStringTerms.ParsedBucket bucket : buckets) {
            Aggregations agg = bucket.getAggregations();
            T t = this.processAggData(agg, fields, clazz, bucket.getKeyAsString(), groupAliasName);
            re.add(t);
        }
        return re;
    }

    /**
     * 处理聚合返回数据
     * @param agg      聚合返回数据
     * @param fields   实体类field
     * @param clazz    实体类
     * @param keyValue key
     * @return clazz实例
     */
    private <T> T processAggData(Aggregations agg, Field[] fields, Class<T> clazz, String keyValue, String groupAliasName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(groupAliasName, keyValue);
        for (Field field : fields) {
            String fieldName = field.getName();
            ParsedSum sum = agg.get(fieldName);
            if (null == sum) {
                continue;
            }
            jsonObject.addProperty(fieldName, sum.getValue());
        }
        return gson.fromJson(jsonObject, clazz);
    }

    /**
     * 聚合查询准备查询条件
     * @param query       查询条件
     * @param searchQuery 封装查询条件
     * @param sortBuilder 排序
     * @param agg         聚合
     * @return
     */
    private SearchSourceBuilder prepareSearchSourceBuilderWithGroupBy(QueryBuilder query, NativeSearchQuery searchQuery,
                                                                      SortBuilder<?> sortBuilder, AggregationBuilder agg) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);

        List<SortBuilder<?>> sortList = new ArrayList<>();
        if (null != sortBuilder) {
            sortList.add(sortBuilder);
            sortList.stream().forEach(searchSourceBuilder::sort);
        }

        searchQuery.getIndices();
        searchSourceBuilder.fetchSource(searchQuery.getIndices().toArray(new String[searchQuery.getIndices().size()]), new String[0]);
        searchSourceBuilder.fetchSource(true);

        searchSourceBuilder.aggregation(agg);
        return searchSourceBuilder;
    }

    /**
     * prepare the search request
     * @param query StringQuery 查询的字符串
     * @param clazz 实体类
     * @return SearchRequest 查询请求
     */
    private <T> SearchRequest prepareSearchRequest(StringQuery query, Class<T> clazz) {

        final String index = retrieveIndexNameFromPersistentEntity(clazz);

        SearchSourceBuilder searchSourceBuilder = prepareSearchSourceBuilder(query);
        SearchRequest searchRequest = new SearchRequest();

        List<String> indices = new ArrayList<>();

        indices.add(index);

        searchRequest.indices(indices.toArray(new String[indices.size()]));
        searchRequest.source(searchSourceBuilder);

        return searchRequest;
    }

    /**
     * 构建 searchSourceBuilder
     * @param query StringQuery 查询的字符串
     * @return SearchSourceBuilder 查询源构建类
     */
    private SearchSourceBuilder prepareSearchSourceBuilder(StringQuery query) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        try {
            byte[] queryBytes = query.getSource().getBytes(CHARSET_UTF_8);

            // 配置 queryBuilder
            searchSourceBuilder.query(new WrapperQueryBuilder(queryBytes));

            Pageable pageable = query.getPageable();

            // 配置分页信息
            if (pageable != null) {
                // 分页起始点
                int from = pageable.getOffset();
                searchSourceBuilder.from(from);

                // 分页大小
                int size = pageable.getPageSize();
                searchSourceBuilder.size(size);
            }

            // 配置排序
            List<SortBuilder<?>> sortList = query.getSortList();
            if (sortList != null && sortList.size() > 0) {
                sortList.stream().forEach(searchSourceBuilder::sort);
            }

            searchSourceBuilder.fetchSource(true);

            return searchSourceBuilder;
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException", e);
            throw new ElasticsearchException("prepareSearchSourceBuilder UnsupportedEncodingException {}", e);
        }
    }

}