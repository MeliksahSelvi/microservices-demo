package com.melik.elastic.query.service.model;

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
public class ElasticQueryServiceWordCountResponseModel {
    private String word;
    private Long wordCount;
}
