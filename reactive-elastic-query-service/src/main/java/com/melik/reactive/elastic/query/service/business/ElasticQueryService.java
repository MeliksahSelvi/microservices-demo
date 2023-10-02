package com.melik.reactive.elastic.query.service.business;

import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import reactor.core.publisher.Flux;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

public interface ElasticQueryService {

    Flux<ElasticQueryServiceResponseModel> getDocumentByText(String text);
}
