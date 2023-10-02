package com.melik.reactive.elastic.query.service.business.impl;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.common.transformer.ElasticToResponseModelTransformer;
import com.melik.reactive.elastic.query.service.business.ElasticQueryService;
import com.melik.reactive.elastic.query.service.business.ReactiveElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient;
    private final ElasticToResponseModelTransformer transformer;

    public TwitterElasticQueryService(ReactiveElasticQueryClient<TwitterIndexModel> reactiveElasticQueryClient, ElasticToResponseModelTransformer transformer) {
        this.reactiveElasticQueryClient = reactiveElasticQueryClient;
        this.transformer = transformer;
    }

    @Override
    public Flux<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        LOG.info("Querying reactive elasticsearch fo text {}", text);
        Flux<TwitterIndexModel> twitterIndexModelFlux = reactiveElasticQueryClient.getIndexModelByText(text);
        return twitterIndexModelFlux.map(transformer::getResponseModel);
    }
}
