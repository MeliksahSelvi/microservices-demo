package com.melik.elastic.index.client.util;

import com.melik.elastic.model.index.IndexModel;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 29.09.2023
 */

@Component
public class ElasticIndexUtil<T extends IndexModel> {

    public List<IndexQuery> getIndexQueries(List<T> documents) {

        return documents.stream()
                .map(document -> new IndexQueryBuilder()
                        .withId(document.getId())
                        .withObject(document)
                        .build()
                ).collect(Collectors.toList());
    }
}
