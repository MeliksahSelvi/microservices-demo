package com.melik.gateway.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mselvi
 * @Created 09.10.2023
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryServiceFallbackModel {
    private String fallbackMessage;
}
