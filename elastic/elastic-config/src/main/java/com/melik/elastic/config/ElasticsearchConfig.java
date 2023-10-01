package com.melik.elastic.config;

import com.melik.app.config.data.config.ElasticConfigData;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

/**
 * @Author mselvi
 * @Created 29.09.2023
 */

@Configuration
/*
* hem elastic index client hem elastic query client moduüllerindeki repository'leri taramak için basePackages ifadesini daha genel bir hale çektik.
* */
@EnableElasticsearchRepositories(basePackages = "com.melik.elastic")
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    private final ElasticConfigData elasticConfigData;

    public ElasticsearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        UriComponents serverUri= UriComponentsBuilder.fromHttpUrl(elasticConfigData.getConnectionUrl()).build();

        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(
                        Objects.requireNonNull(serverUri.getHost()),
                        serverUri.getPort(),
                        serverUri.getScheme()
                )).setRequestConfigCallback(
                        builder -> builder
                                .setConnectTimeout(elasticConfigData.getConnectionTimeoutMs())
                                .setSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                )
        );
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations(){
        return new ElasticsearchRestTemplate((elasticsearchClient()));
    }
}
