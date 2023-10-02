package com.melik.elastic.query.service.common.transformer;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Component
public class ElasticToResponseModelTransformer {

    public ElasticQueryServiceResponseModel getResponseModel(TwitterIndexModel twitterIndexModel){

        return ElasticQueryServiceResponseModel.builder()
                .id(twitterIndexModel.getId())
                .userId(twitterIndexModel.getUserId())
                .text(twitterIndexModel.getText())
                .createdAt(twitterIndexModel.getCreatedAt())
                .build();
    }

    public List<ElasticQueryServiceResponseModel> getResponseModels(List<TwitterIndexModel> twitterIndexModels){
        return twitterIndexModels.stream().map(this::getResponseModel).collect(Collectors.toList());
    }
}
