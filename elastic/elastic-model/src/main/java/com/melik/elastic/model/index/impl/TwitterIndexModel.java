package com.melik.elastic.model.index.impl;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.melik.elastic.model.index.IndexModel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * @Author mselvi
 * @Created 01.10.2023
 */

/*
 * Document Annotation sayesinde bu class verilen index'e göre elastic üzerinde mapping işlemi görecek.
 * index name'yi belirtirken SpEL(Spring Expression Language) kullandık.
 * Elasticsearch iletişim için rest kullanır.MimeType olarak da Json kullanır.Bu sebeple Elasticsearch ile
 * mapping yapılacak class'ın field'ları jsona uygun olmalı.
 * */
@Document(indexName = "#{elasticConfigData.indexName}")
@Builder
@Data
public class TwitterIndexModel implements IndexModel {

    @JsonProperty
    private String id;

    @JsonProperty
    private Long userId;

    @JsonProperty
    private String text;

    @Field(type = FieldType.Date,format = DateFormat.date ,pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    @JsonProperty
    private LocalDateTime createdAt;
}
