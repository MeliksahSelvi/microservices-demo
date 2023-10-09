package com.melik.elastic.query.service.business;

import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(String id);

    ElasticQueryServiceAnalyticsResponseModel getDocumentByText(String text,String accessToken);

    List<ElasticQueryServiceResponseModel> getAllDocuments();
}
