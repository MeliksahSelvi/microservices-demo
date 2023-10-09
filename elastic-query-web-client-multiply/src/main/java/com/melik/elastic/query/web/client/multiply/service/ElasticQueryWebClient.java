package com.melik.elastic.query.web.client.multiply.service;

import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;

import java.util.List;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

public interface ElasticQueryWebClient {
    List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);

}
