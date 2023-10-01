package com.melik.elastic.query.service.api;

import com.melik.elastic.query.service.business.ElasticQueryService;
import com.melik.elastic.query.service.model.ElasticQueryServiceRequestModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceResponseModel;
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

    @GetMapping("/get-document-by-text")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getDocumentByText(@RequestBody @Valid ElasticQueryServiceRequestModel requestModel){
        List<ElasticQueryServiceResponseModel> response=elasticQueryService.getDocumentByText(requestModel.getText());
        LOG.info("ElasticSearch returned {} of documents",response.size());
        return ResponseEntity.ok(response);
    }
}
