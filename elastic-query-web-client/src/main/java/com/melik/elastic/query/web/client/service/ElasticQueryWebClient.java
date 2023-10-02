package com.melik.elastic.query.web.client.service;

import com.melik.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.melik.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;

import java.util.List;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

public interface ElasticQueryWebClient {

    List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);
}