package com.melik.app.config.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Data
@Configuration
@ConfigurationProperties(prefix = "analytics-service")
public class AnalyticsServiceConfigData {
    private String version;
    private String customAudience;

}
