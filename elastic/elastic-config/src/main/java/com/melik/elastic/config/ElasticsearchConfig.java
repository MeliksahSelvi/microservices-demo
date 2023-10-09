package com.melik.elastic.config;

import com.melik.app.config.data.config.ElasticConfigData;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * @Author mselvi
 * @Created 29.09.2023
 */

@Configuration
/*
 * hem elastic index client hem elastic query client moduüllerindeki repository'leri taramak için basePackages ifadesini daha genel bir hale çektik.
 * */
@EnableElasticsearchRepositories(basePackages = "com.melik.elastic")
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    private final ElasticConfigData elasticConfigData;

    public ElasticsearchConfig(ElasticConfigData elasticConfigData) {
        this.elasticConfigData = elasticConfigData;
    }

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticConfigData.getConnectionUrl())
                .withConnectTimeout(elasticConfigData.getConnectionTimeoutMs())
                .withSocketTimeout(elasticConfigData.getSocketTimeoutMs())
                .build();
    }
}
