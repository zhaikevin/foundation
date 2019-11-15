package com.github.foundation.es;

import com.github.foundation.SpringBootTestAbstract;
import com.github.foundation.common.utils.JsonUtils;
import com.github.foundation.es.domain.PageRequest;
import com.github.foundation.es.domain.Pageable;
import com.github.foundation.test.es.Fields;
import com.github.foundation.test.es.Log;
import com.github.foundation.test.es.LogRepository;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Assert;
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
    public void searchByPageTest() {
        String ip = "fields.ip";
        Pageable pageable = PageRequest.of(1, 10);
        QueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.matchQuery(ip, "172.16.39.34"));
        //QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        System.out.println(JsonUtils.toJson(logRepository.search(queryBuilder, pageable)));
    }

    @Test
    public void searchTest() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        System.out.println(JsonUtils.toJson(logRepository.search(queryBuilder)));
    }

    @Test
    public void countTest() {
        logRepository.setIndexName("log-2019.11.11-finance-bank");
        System.out.println(logRepository.count());
    }

    @Test
    public void deleteByIdTest() {
        Log log = new Log();
        log.setMessage("test");
        Fields fields = new Fields();
        fields.setIp("127.0.0.1");
        fields.setProject("test");
        log.setFields(fields);
        String id = logRepository.save(log).getId();
        Log newLog = logRepository.findById(id);
        Assert.assertEquals(log.getFields(), newLog.getFields());
        logRepository.deleteById(id);
        newLog = logRepository.findById(id);
        Assert.assertNull(newLog);
    }
}
