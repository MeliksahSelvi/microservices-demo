package com.melik.kafka.to.elastic.service.consumer.impl;

import com.melik.app.config.data.config.KafkaConfigData;
import com.melik.app.config.data.config.KafkaConsumerConfigData;
import com.melik.elastic.index.client.service.ElasticIndexClient;
import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.kafka.admin.client.KafkaAdminClient;
import com.melik.kafka.avro.model.TwitterAvroModel;
import com.melik.kafka.to.elastic.service.consumer.KafkaConsumer;
import com.melik.kafka.to.elastic.service.transformer.AvroToElasticModelTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Author mselvi
 * @Created 28.09.2023
 */

@Service
public class TwitterKafkaConsumer implements KafkaConsumer<Long, TwitterAvroModel> {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterKafkaConsumer.class);

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaAdminClient kafkaAdminClient;
    private final KafkaConfigData kafkaConfigData;
    private final KafkaConsumerConfigData kafkaConsumerConfigData;
    private final AvroToElasticModelTransformer transformer;
    private final ElasticIndexClient<TwitterIndexModel> elasticIndexClient;

    public TwitterKafkaConsumer(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
                                KafkaAdminClient kafkaAdminClient,
                                KafkaConfigData kafkaConfigData,
                                KafkaConsumerConfigData kafkaConsumerConfigData,
                                AvroToElasticModelTransformer transformer,
                                ElasticIndexClient<TwitterIndexModel> elasticIndexClient) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaAdminClient = kafkaAdminClient;
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaConsumerConfigData = kafkaConsumerConfigData;
        this.transformer = transformer;
        this.elasticIndexClient = elasticIndexClient;
    }

    @EventListener
    public void onAppStarted(ApplicationStartedEvent event) {
        kafkaAdminClient.checkTopicsCreated();
        LOG.info("Topics with name {} is ready for operations", kafkaConfigData.getTopicNamesToCreate().toArray());
        /*
         * KafkaConsumer'ın autostartup özelliğini kapattık ve manuel olarak başlatıyoruz. çünkü öncesinde topic'lerin oluşup oluşmadığından emin olmak istedik.
         * */
        Objects.requireNonNull(kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.getConsumerGroupId())).start();
    }

    /*
     * consuumer listener'da id değişkenine verdiğimiz değer consumer groupid ile aynı olmalı yoksa verdiğimiz değer consumer groupid değerini override eder.
     * */
    @Override
    @KafkaListener(id = "${kafka-consumer-config.consumer-group-id}", topics = "${kafka-config.topic-name}")
    public void receive(@Payload List<TwitterAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<Integer> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        LOG.info("{} number of message received with keys {}, partitions {} and offsets {}, " +
                        "sending it to elastic: Thread id {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString(),
                Thread.currentThread().getId());

        List<TwitterIndexModel> elasticModels = transformer.getElasticModels(messages);
        List<String> documentIds = elasticIndexClient.save(elasticModels);
        LOG.info("Documents saved to elasticsearch with ids {}", documentIds.toArray());
    }
}
