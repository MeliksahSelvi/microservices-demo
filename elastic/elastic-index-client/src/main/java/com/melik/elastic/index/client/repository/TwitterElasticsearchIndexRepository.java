package com.melik.elastic.index.client.repository;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author mselvi
 * @Created 29.09.2023
 */

/*
* Elasticsearch üzerinde sorguları çalıştırmak için ElasticsearchOperations kullanmak yerine direkt daha kolay olan bu repository kullanılabilir.
* */
@Repository
public interface TwitterElasticsearchIndexRepository extends ElasticsearchRepository<TwitterIndexModel, String> {
}
