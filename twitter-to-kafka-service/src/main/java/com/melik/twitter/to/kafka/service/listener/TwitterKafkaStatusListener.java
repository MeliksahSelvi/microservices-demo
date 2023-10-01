package com.melik.twitter.to.kafka.service.listener;

import com.melik.app.config.data.config.KafkaConfigData;
import com.melik.kafka.avro.model.TwitterAvroModel;
import com.melik.kafka.producer.config.service.KafkaProducer;
import com.melik.twitter.to.kafka.service.transformer.TwitterStatusToAvroTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import twitter4j.Status;
import twitter4j.StatusAdapter;

/**
 * @Author mselvi
 * @Created 25.09.2023
 */

@Component
public class TwitterKafkaStatusListener extends StatusAdapter {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaStatusListener.class);

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducer<Long, TwitterAvroModel> kafkaProducer;
    private final TwitterStatusToAvroTransformer transformer;

    public TwitterKafkaStatusListener(KafkaConfigData kafkaConfigData, KafkaProducer<Long, TwitterAvroModel> kafkaProducer, TwitterStatusToAvroTransformer transformer) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducer = kafkaProducer;
        this.transformer = transformer;
    }

    @Override
    public void onStatus(Status status) {
        LOG.info("Received status text {} sending to kafka topic {}", status.getText(), kafkaConfigData.getTopicName());
        TwitterAvroModel twitterAvroModel = transformer.getTwitterAvroModelFromStatus(status);
        kafkaProducer.send(kafkaConfigData.getTopicName(), twitterAvroModel.getId(), twitterAvroModel);
    }
}
