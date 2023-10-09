package com.melik.app.config.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "elastic-query-service")
public class ElasticQueryServiceConfigData {
    private String version;
    private String customAudience;
    private Long backPressureDelayMs;
    private WebClient webClient;
    private Query queryFromKafkaStateStore;
    private Query queryFromAnalyticsDatabase;

    @Data
    public static class WebClient {
        private Integer connectTimeoutMs;
        private Integer readTimeoutMs;
        private Integer writeTimeoutMs;
        private Integer maxInMemorySize;
        private String contentType;
        private String acceptType;
        private String queryType;
    }

    @Data
    public static class Query {
        private String method;
        private String accept;
        private String uri;
    }
}
