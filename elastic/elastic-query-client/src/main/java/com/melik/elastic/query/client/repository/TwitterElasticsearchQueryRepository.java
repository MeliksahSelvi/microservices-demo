package com.melik.elastic.query.client.repository;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Repository
public interface TwitterElasticsearchQueryRepository extends ElasticsearchRepository<TwitterIndexModel,String> {

    List<TwitterIndexModel> findByText(String text);
}
