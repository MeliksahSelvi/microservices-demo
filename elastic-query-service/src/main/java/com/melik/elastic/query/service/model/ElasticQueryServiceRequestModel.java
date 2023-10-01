package com.melik.elastic.query.service.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Data
@Builder
/*
 * NoArgsConstructor json objeleri java objelerine deserialize edilirken ihtiyaç duyuluyor.
 * */
@NoArgsConstructor
/*
 * AllArgsConstructor builder class'ı içerisinde işlemler yapılırken ihtiyaç duyuluyor.
 * */
@AllArgsConstructor
public class ElasticQueryServiceRequestModel {

    private String id;

    @NotEmpty(message = "text can not be empty")
    private String text;
}
