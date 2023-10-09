package com.melik.analytics.service.business;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public interface KafkaConsumer<T extends SpecificRecordBase> {
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
