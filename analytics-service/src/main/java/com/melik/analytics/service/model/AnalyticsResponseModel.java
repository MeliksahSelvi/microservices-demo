package com.melik.analytics.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsResponseModel {
    private UUID id;
    private String word;
    private long wordCount;
}
