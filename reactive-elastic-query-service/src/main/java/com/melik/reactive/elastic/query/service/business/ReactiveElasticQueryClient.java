package com.melik.reactive.elastic.query.service.business;

import com.melik.elastic.model.index.IndexModel;
import com.melik.elastic.model.index.impl.TwitterIndexModel;
import reactor.core.publisher.Flux;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

public interface ReactiveElasticQueryClient<T extends IndexModel> {

    Flux<TwitterIndexModel> getIndexModelByText(String text);
}
