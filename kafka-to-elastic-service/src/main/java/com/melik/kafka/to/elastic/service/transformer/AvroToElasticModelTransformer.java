package com.melik.kafka.to.elastic.service.transformer;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.kafka.avro.model.TwitterAvroModel;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 29.09.2023
 */

@Component
public class AvroToElasticModelTransformer {

    public List<TwitterIndexModel> getElasticModels(List<TwitterAvroModel> avroModels) {
        return avroModels.stream().map(avroModel -> TwitterIndexModel.builder()
                .userId(avroModel.getUserId())
                .id(String.valueOf(avroModel.getId()))
                .text(avroModel.getText())
                .createdAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(avroModel.getCreatedAt()), ZoneId.systemDefault()))
                .build()
        ).collect(Collectors.toList());
    }
}
