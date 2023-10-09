package com.melik.analytics.service.business;

import com.melik.analytics.service.model.AnalyticsResponseModel;

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

public interface AnalyticsService {

    Optional<AnalyticsResponseModel> getWordAnalytics(String word);
}
