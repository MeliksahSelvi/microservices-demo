package com.melik.elastic.query.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

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
public class ElasticQueryServiceResponseModelV2 extends RepresentationModel<ElasticQueryServiceResponseModelV2> {
    private Long id;
    private Long userId;
    private String text;
    private String text2;
}
