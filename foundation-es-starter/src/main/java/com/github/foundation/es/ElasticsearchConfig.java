package com.github.foundation.es;

import com.github.foundation.es.core.ElasticsearchOperations;
import com.github.foundation.es.core.ElasticsearchTemplate;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/14 15:01
 */
@Configuration
public class ElasticsearchConfig {

    @Value("${spring.elasticsearch.rest.uris}")
    private String clusterNodes;

    @Value("${spring.elasticsearch.rest.username}")
    private String username;

    @Value("${spring.elasticsearch.rest.password}")
    private String password;

    @Bean
    public ElasticsearchOperations elasticsearchOperations() {

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));

        String[] hostInfos = StringUtils.split(clusterNodes, ",");
        List<HttpHost> hostList = Arrays.asList(hostInfos).stream().map(hostInfo -> {
            String hostname = hostInfo.split(":")[0];
            int port = Integer.valueOf(hostInfo.split(":")[1]);
            return new HttpHost(hostname, port);
        }).collect(Collectors.toList());

        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[hostList.size()]))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                });
        RestHighLevelClient client = new RestHighLevelClient(builder);

        ElasticsearchOperations elasticsearchOperations = new ElasticsearchTemplate(client);
        return elasticsearchOperations;
    }
}
