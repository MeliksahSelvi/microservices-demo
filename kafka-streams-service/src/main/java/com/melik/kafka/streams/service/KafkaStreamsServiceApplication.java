package com.melik.kafka.streams.service;

import com.melik.kafka.streams.service.init.StreamsInitializer;
import com.melik.kafka.streams.service.runner.StreamsRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = "com.melik")
public class KafkaStreamsServiceApplication implements CommandLineRunner {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaStreamsServiceApplication.class);

    private final StreamsRunner<String, Long> streamsRunner;
    private final StreamsInitializer streamsInitializer;

    public KafkaStreamsServiceApplication(StreamsRunner<String, Long> streamsRunner, StreamsInitializer streamsInitializer) {
        this.streamsRunner = streamsRunner;
        this.streamsInitializer = streamsInitializer;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaStreamsServiceApplication.class);
    }

    @Override
    public void run(String... args) {
        LOG.info("App starts...");
        streamsInitializer.init();
        streamsRunner.start();
    }
}
