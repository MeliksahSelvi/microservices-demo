package com.melik.kafka.admin.config;

import com.melik.app.config.data.config.KafkaConfigData;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

import java.util.Map;

/**
 * @Author mselvi
 * @Created 26.09.2023
 */

@EnableRetry
@Configuration
public class KafkaAdminConfig {

    private final KafkaConfigData configData;

    public KafkaAdminConfig(KafkaConfigData configData) {
        this.configData = configData;
    }
    /*
    * Broker, Topics ve Configuration'ları yöneten ve inceleyen yapı AdminClient
    * */
    @Bean
    public AdminClient adminClient(){
        return AdminClient.create(Map.of(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG,
                configData.getBootstrapServers()));
    }
}
