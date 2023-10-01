package com.melik.elastic.query.client.service.impl;

import com.melik.app.config.data.config.ElasticConfigData;
import com.melik.app.config.data.config.ElasticQueryConfigData;
import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.client.exception.ElasticQueryClientException;
import com.melik.elastic.query.client.service.ElasticQueryClient;
import com.melik.elastic.query.client.util.ElasticQueryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Service
public class TwitterElasticQueryClient implements ElasticQueryClient<TwitterIndexModel> {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryClient.class);

    private final ElasticConfigData elasticConfigData;
    private final ElasticQueryConfigData elasticQueryConfigData;
    private final ElasticsearchOperations elasticsearchOperations;
    private final ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil;

    public TwitterElasticQueryClient(ElasticConfigData elasticConfigData,
                                     ElasticQueryConfigData elasticQueryConfigData,
                                     ElasticsearchOperations elasticsearchOperations,
                                     ElasticQueryUtil<TwitterIndexModel> elasticQueryUtil) {
        this.elasticConfigData = elasticConfigData;
        this.elasticQueryConfigData = elasticQueryConfigData;
        this.elasticsearchOperations = elasticsearchOperations;
        this.elasticQueryUtil = elasticQueryUtil;
    }

    @Override
    public TwitterIndexModel getIndexModelById(String id) {
        Query query = elasticQueryUtil.getSearchQueryById(id);
        SearchHit<TwitterIndexModel> searchResult = elasticsearchOperations.searchOne(query, TwitterIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));

        if (searchResult == null) {
            LOG.error("No document found at elasticsearch with id {}", id);
            throw new ElasticQueryClientException("No document found at elasticsearch with id " + id);
        }
        LOG.info("Document with id {} retrieved successfully", searchResult.getId());
        return searchResult.getContent();
    }

    @Override
    public List<TwitterIndexModel> getIndexModelByText(String text) {
        Query query = elasticQueryUtil.getSearchQueryByFieldText(elasticQueryConfigData.getTextField(), text);
        String logMessage = "{} of documents with text {} retrieved successfully";
        return search(query, logMessage, text);
    }

    @Override
    public List<TwitterIndexModel> getAllIndexModels() {
        Query query = elasticQueryUtil.getSearchQueryForAll();
        String logMessage = "{} of documents retrieved successfully";
        return search(query, logMessage);
    }

    private List<TwitterIndexModel> search(Query query, String logMessage, Object... logParams) {
        SearchHits<TwitterIndexModel> searchResult = elasticsearchOperations.search(query, TwitterIndexModel.class,
                IndexCoordinates.of(elasticConfigData.getIndexName()));

        LOG.info(logMessage, searchResult.getTotalHits(), logParams);
        return searchResult.get().map(SearchHit::getContent).collect(Collectors.toList());
    }
}
