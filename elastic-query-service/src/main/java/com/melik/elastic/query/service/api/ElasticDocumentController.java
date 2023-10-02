package com.melik.elastic.query.service.api;

import com.melik.elastic.query.service.business.ElasticQueryService;
import com.melik.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

/*
 * RestController = Controller + Response Body
 *
 * produces kısmında verdiğimiz ifade request'lerin accept header kısmına koyulmalı.
 * */
@RestController
@RequestMapping(value = "/documents",produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {

    private final static Logger LOG= LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @GetMapping("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments(){
        List<ElasticQueryServiceResponseModel> response=elasticQueryService.getAllDocuments();
        LOG.info("ElasticSearch returned {} of documents",response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponseModel response=elasticQueryService.getDocumentById(id);
        LOG.debug("ElasticSearch returned document with id {}",id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}",produces = "application/vnd.api.v2+json")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id){
        ElasticQueryServiceResponseModel response=elasticQueryService.getDocumentById(id);
        LOG.debug("ElasticSearch returned document with id {}",id);
        ElasticQueryServiceResponseModelV2 modelV2 = getModelV2(response);
        return ResponseEntity.ok(modelV2);
    }

    @GetMapping("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel){
        List<ElasticQueryServiceResponseModel> response=elasticQueryService.getDocumentByText(requestModel.getText());
        LOG.info("ElasticSearch returned {} of documents",response.size());
        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponseModelV2 getModelV2(ElasticQueryServiceResponseModel model){
        ElasticQueryServiceResponseModelV2 modelV2=ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(model.getId()))
                .text(model.getText())
                .text2("Text V2")
                .userId(model.getUserId())
                .build();

        modelV2.add(model.getLinks());

        return modelV2;
    }
}
