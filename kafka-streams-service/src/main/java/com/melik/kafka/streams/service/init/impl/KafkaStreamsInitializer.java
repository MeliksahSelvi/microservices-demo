package com.melik.kafka.streams.service.init.impl;

import com.melik.app.config.data.config.KafkaConfigData;
import com.melik.kafka.admin.client.KafkaAdminClient;
import com.melik.kafka.streams.service.init.StreamsInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Component
public class KafkaStreamsInitializer implements StreamsInitializer {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaStreamsInitializer.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaAdminClient kafkaAdminClient;

    public KafkaStreamsInitializer(KafkaConfigData kafkaConfigData, KafkaAdminClient kafkaAdminClient) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaAdminClient = kafkaAdminClient;
    }

    @Override
    public void init() {
        kafkaAdminClient.checkTopicsCreated();
        kafkaAdminClient.checkSchemaRegistry();
        LOG.info("Topics with name {} is ready for operations", kafkaConfigData.getTopicNamesToCreate().toArray());
    }
}
