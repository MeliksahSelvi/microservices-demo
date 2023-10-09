package com.melik.kafka.streams.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaStreamsResponseModel {
    private String word;
    private Long wordCount;
}
