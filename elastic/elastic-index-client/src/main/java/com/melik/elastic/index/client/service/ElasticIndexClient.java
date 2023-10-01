package com.melik.elastic.index.client.service;

import com.melik.elastic.model.index.IndexModel;

import java.util.List;

/**
 * @Author mselvi
 * @Created 29.09.2023
 */

public interface ElasticIndexClient<T extends IndexModel> {
    List<String> save(List<T> documents);
}
