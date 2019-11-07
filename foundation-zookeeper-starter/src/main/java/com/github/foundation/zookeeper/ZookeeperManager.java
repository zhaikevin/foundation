package com.github.foundation.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/1 11:24
 */
@Configuration
public class ZookeeperManager {

    @Value("${foundation.zookeeper.namespace}")
    private String namespace;

    @Value("${foundation.project.name}")
    private String projectName;

    @Value("${foundation.zookeeper.servers}")
    private String servers;

    @Value("${foundation.zookeeper.sessionTimeoutMs:30000}")
    private int sessionTimeoutMs;

    @Value("${foundation.zookeeper.connectionTimeoutMs:30000}")
    private int connectionTimeoutMs;

    /**
     * 重试间隔
     */
    @Value("${foundation.zookeeper.baseSleepTimeMs:2000}")
    private int baseSleepTimeMs;

    /**
     * 最大重试次数
     */
    @Value("${foundation.zookeeper.maxRetries:10}")
    private int maxRetries;

    @Bean
    public CuratorFramework zookeeperClientFactory() {
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(servers)
                .sessionTimeoutMs(sessionTimeoutMs).connectionTimeoutMs(connectionTimeoutMs).canBeReadOnly(false)
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries)).namespace(namespace + "/" + projectName)
                .defaultData(null).build();
        client.start();
        return client;
    }
}
