package com.melik.reactive.elastic.query.web.client.service;

import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import reactor.core.publisher.Flux;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

public interface ElasticQueryWebClient {

    Flux<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
