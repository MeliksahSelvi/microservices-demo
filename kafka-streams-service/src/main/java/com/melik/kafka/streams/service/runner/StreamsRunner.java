package com.melik.kafka.streams.service.runner;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public interface StreamsRunner<K, V> {
    void start();

    default V getValueByKey(K key) {
        return null;
    }
}
