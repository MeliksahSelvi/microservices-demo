package com.melik.elastic.query.service.business.impl;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.client.service.ElasticQueryClient;
import com.melik.elastic.query.service.business.ElasticQueryService;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceResponseModelAssembler modelAssembler;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler modelAssembler, ElasticQueryClient<TwitterIndexModel> elasticQueryClient) {
        this.modelAssembler = modelAssembler;
        this.elasticQueryClient = elasticQueryClient;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        TwitterIndexModel twitterIndexModel = elasticQueryClient.getIndexModelById(id);
        ElasticQueryServiceResponseModel responseModel = modelAssembler.toModel(twitterIndexModel);
        LOG.info("Querying elasticsearch by id {}", id);
        return responseModel;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getDocumentByText(String text) {
        List<TwitterIndexModel> twitterIndexModels = elasticQueryClient.getIndexModelByText(text);
        List<ElasticQueryServiceResponseModel> responseModels = modelAssembler.toModels(twitterIndexModels);
        LOG.info("Querying elasticsearch by text {}", text);
        return responseModels;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        List<TwitterIndexModel> twitterIndexModels = elasticQueryClient.getAllIndexModels();
        List<ElasticQueryServiceResponseModel> responseModels = modelAssembler.toModels(twitterIndexModels);
        LOG.info("Querying all documents in elasticsearch");
        return responseModels;
    }
}
