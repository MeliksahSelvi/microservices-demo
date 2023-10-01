package com.melik.elastic.query.client.service.impl;

import com.melik.common.util.CollectionsUtil;
import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.client.exception.ElasticQueryClientException;
import com.melik.elastic.query.client.repository.TwitterElasticsearchQueryRepository;
import com.melik.elastic.query.client.service.ElasticQueryClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Service
@Primary
public class TwitterElasticRepositoryQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticRepositoryQueryClient.class);

    private final TwitterElasticsearchQueryRepository repository;

    public TwitterElasticRepositoryQueryClient(TwitterElasticsearchQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Optional<TwitterIndexModel> searchResult = repository.findById(id);
        LOG.info("Document with id {} retrieved successfully", searchResult.orElseThrow(() -> {
            LOG.error("No document found at elasticsearch with id {}", id);
            throw new ElasticQueryClientException("No document found at elasticsearch with id " + id);
        }).getId());
        return searchResult.get();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        List<TwitterIndexModel> searchResult = repository.findByText(text);
        LOG.info("{} of documents with text {} retrieved successfully", searchResult.size(), text);
        return searchResult;
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Iterable<TwitterIndexModel> all = repository.findAll();
        List<TwitterIndexModel> searchResult = CollectionsUtil.getInstance().getListFromIterable(all);
        LOG.info("{} of documents retrieved successfully", searchResult.size());
        return searchResult;
    }
}
