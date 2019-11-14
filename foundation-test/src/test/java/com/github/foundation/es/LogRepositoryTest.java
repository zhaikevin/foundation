package com.github.foundation.es;

import com.github.foundation.SpringBootTestAbstract;
import com.github.foundation.common.utils.JsonUtils;
import com.github.foundation.es.domain.PageRequest;
import com.github.foundation.es.domain.Pageable;
import com.github.foundation.test.es.LogRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/12 14:10
 */
public class LogRepositoryTest extends SpringBootTestAbstract {

    @Autowired
    private LogRepository logRepository;

    @Test
    public void searchTest() {
        String ip = "fields.ip";
        Pageable pageable = PageRequest.of(1, 10);
        //QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(ip, "172.16.39.34"));
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        System.out.println(JsonUtils.toJson(logRepository.search(queryBuilder, pageable).getContent()));
    }
}
