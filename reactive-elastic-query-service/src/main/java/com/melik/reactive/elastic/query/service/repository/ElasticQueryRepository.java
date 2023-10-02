package com.melik.reactive.elastic.query.service.repository;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Repository
public interface ElasticQueryRepository extends ReactiveCrudRepository<TwitterIndexModel,String> {

    Flux<TwitterIndexModel> findByText(String text);
}
