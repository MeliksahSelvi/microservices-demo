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
 * */
@RestController
@RequestMapping(value = "/documents")
public class ElasticDocumentController {

    private final static Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @GetMapping("/v1")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("ElasticSearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        LOG.debug("ElasticSearch returned document with id {}", id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v2/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        LOG.debug("ElasticSearch returned document with id {}", id);
        ElasticQueryServiceResponseModelV2 v2Model = getV2Model(response);
        return ResponseEntity.ok(v2Model);
    }

    @GetMapping("/v1/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel) {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getDocumentByText(requestModel.getText());
        LOG.info("ElasticSearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponseModelV2 getV2Model(ElasticQueryServiceResponseModel model) {
        ElasticQueryServiceResponseModelV2 modelV2 = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.valueOf(model.getId()))
                .text(model.getText())
                .createdAt(model.getCreatedAt())
                .userId(model.getUserId())
                .build();

        return modelV2;
    }
}
