package com.melik.analytics.service.business.impl;

import com.melik.analytics.service.business.AnalyticsService;
import com.melik.analytics.service.dataaccess.repository.AnalyticsRepository;
import com.melik.analytics.service.model.AnalyticsResponseModel;
import com.melik.analytics.service.transformer.EntityToResponseModelTransformer;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Service
public class TwitterAnalyticsService implements AnalyticsService {

    private final AnalyticsRepository analyticsRepository;
    private final EntityToResponseModelTransformer entityToResponseModelTransformer;

    public TwitterAnalyticsService(AnalyticsRepository analyticsRepository,
                                   EntityToResponseModelTransformer entityToResponseModelTransformer) {
        this.analyticsRepository = analyticsRepository;
        this.entityToResponseModelTransformer = entityToResponseModelTransformer;
    }

    @Override
    public Optional<AnalyticsResponseModel> getWordAnalytics(String word) {
        return entityToResponseModelTransformer.getResponseModel(
                analyticsRepository.getAnalyticsEntitiesByWord(word, PageRequest.of(0, 1))
                        .stream().findFirst().orElse(null));
    }
}
