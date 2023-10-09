package com.melik.kafka.streams.service.runner.impl;

import com.melik.app.config.data.config.KafkaConfigData;
import com.melik.app.config.data.config.KafkaStreamsConfigData;
import com.melik.kafka.avro.model.TwitterAnalyticsAvroModel;
import com.melik.kafka.avro.model.TwitterAvroModel;
import com.melik.kafka.streams.service.runner.StreamsRunner;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Component
public class KafkaStreamsRunner implements StreamsRunner<String, Long> {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaStreamsRunner.class);
    private final static String REGEX = "\\W+";

    private final KafkaStreamsConfigData kafkaStreamsConfigData;
    private final KafkaConfigData kafkaConfigData;
    private final Properties streamsConfiguration;

    private KafkaStreams kafkaStreams;

    private volatile ReadOnlyKeyValueStore<String ,Long> keyValueStore;

    public KafkaStreamsRunner(KafkaStreamsConfigData kafkaStreamsConfigData,
                              KafkaConfigData kafkaConfigData,
                              @Qualifier("streamConfiguration") Properties streamsConfiguration) {
        this.kafkaStreamsConfigData = kafkaStreamsConfigData;
        this.kafkaConfigData = kafkaConfigData;
        this.streamsConfiguration = streamsConfiguration;
    }

    @Override
    public Long getValueByKey(String key) {
        if (kafkaStreams != null && kafkaStreams.state()==KafkaStreams.State.RUNNING) {
            if (keyValueStore == null) {

                synchronized (this){
                    if (keyValueStore==null){
                        keyValueStore=kafkaStreams.store(StoreQueryParameters
                                .fromNameAndType(kafkaStreamsConfigData.getWordCountStoreName(),
                                        QueryableStoreTypes.keyValueStore()));
                    }
                }
            }
            return keyValueStore.get(key.toLowerCase());
        }
        return 0L;
    }

    @PreDestroy
    public void close(){
        if (kafkaStreams != null) {
            kafkaStreams.close();
            LOG.info("Kafka streaming closed");
        }
    }

    @Override
    public void start() {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                kafkaConfigData.getSchemaRegistryUrlKey(),
                kafkaConfigData.getSchemaRegistryUrl());

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<Long, TwitterAvroModel> twitterAvroModelKStream = getLongTwitterAvroModelKStream(serdeConfig, streamsBuilder);

        createTopology(twitterAvroModelKStream, serdeConfig);

        startStreaming(streamsBuilder);
    }

    private KStream<Long, TwitterAvroModel> getLongTwitterAvroModelKStream(Map<String, String> serdeConfig, StreamsBuilder streamsBuilder) {
        final Serde<TwitterAvroModel> serdeTwitterAvroModel = new SpecificAvroSerde<>();
        serdeTwitterAvroModel.configure(serdeConfig, false);
        return streamsBuilder.stream(kafkaStreamsConfigData.getInputTopicName(), Consumed.with(Serdes.Long(), serdeTwitterAvroModel));
    }

    private void createTopology(KStream<Long, TwitterAvroModel> twitterAvroModelKStream, Map<String, String> serdeConfig) {
        Pattern pattern = Pattern.compile(REGEX, Pattern.UNICODE_CHARACTER_CLASS);

        Serde<TwitterAnalyticsAvroModel> serdeTwitterAnalyticsModel = getSerdeAnalyticsModel(serdeConfig);

        twitterAvroModelKStream
                .flatMapValues(value -> Arrays.asList(pattern.split(value.getText().toLowerCase())))
                .groupBy((key, word) -> word)
                .count(Materialized.as(kafkaStreamsConfigData.getWordCountStoreName()))
                .toStream()
                .map(mapToAnalyticsModel())
                .to(kafkaStreamsConfigData.getOutputTopicName(),
                        Produced.with(Serdes.String(), serdeTwitterAnalyticsModel));
    }

    private Serde<TwitterAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
        Serde<TwitterAnalyticsAvroModel> serdeTwitterAnalyticsModel = new SpecificAvroSerde<>();
        serdeTwitterAnalyticsModel.configure(serdeConfig, false);
        return serdeTwitterAnalyticsModel;
    }

    private KeyValueMapper<String, Long, KeyValue<? extends String, ? extends TwitterAnalyticsAvroModel>> mapToAnalyticsModel() {
        return (word, count) -> {
            LOG.info("Sending to topic {}, word {} - count {}",
                    kafkaStreamsConfigData.getOutputTopicName(), word, count);
            return new KeyValue<>(word, TwitterAnalyticsAvroModel
                    .newBuilder()
                    .setWord(word)
                    .setWordCount(count)
                    .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))
                    .build());
        };
    }

    private void startStreaming(StreamsBuilder streamsBuilder) {
        final Topology topology = streamsBuilder.build();
        LOG.info("Defined topology: {}", topology.describe());
        kafkaStreams = new KafkaStreams(topology, streamsConfiguration);
        kafkaStreams.start();
        LOG.info("Kafka streaming started...");
    }


}
