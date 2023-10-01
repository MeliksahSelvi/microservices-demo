package com.melik.elastic.query.client.service;

import com.melik.elastic.model.index.IndexModel;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

public interface ElasticQueryClient<T extends IndexModel> {

    T getIndexModelById(String id);

    List<T> getIndexModelByText(String text);

    List<T> getAllIndexModels();
}
