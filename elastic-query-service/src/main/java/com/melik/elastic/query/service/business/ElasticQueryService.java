package com.melik.elastic.query.service.business;

import com.melik.elastic.query.service.model.ElasticQueryServiceResponseModel;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

public interface ElasticQueryService {

    ElasticQueryServiceResponseModel getDocumentById(String id);

    List<ElasticQueryServiceResponseModel> getDocumentByText(String text);

    List<ElasticQueryServiceResponseModel> getAllDocuments();
}
