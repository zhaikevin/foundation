package com.github.foundation.test.es;

import com.github.foundation.es.AbstractElasticsearchRepository;
import com.github.foundation.es.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/12 14:05
 */
@Repository
public class LogRepository extends AbstractElasticsearchRepository<Log, String> {
    public LogRepository(ElasticsearchOperations elasticsearchOperations) {
        super(elasticsearchOperations, Log.class);
    }
}
