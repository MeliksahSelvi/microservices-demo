package com.melik.elastic.query.service.model;

import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ElasticQueryServiceAnalyticsResponseModel {
    private List<ElasticQueryServiceResponseModel> elasticQueryServiceResponseModels;
    private Long wordCount;
}
