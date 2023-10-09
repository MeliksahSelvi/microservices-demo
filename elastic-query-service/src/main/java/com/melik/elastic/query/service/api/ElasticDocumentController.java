package com.melik.elastic.query.service.api;

import com.melik.elastic.query.service.business.ElasticQueryService;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceRequestModel;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceResponseModelV2;
import com.melik.elastic.query.service.security.TwitterQueryUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

/*
 * Keycloak'dan app_user olarak token almak için linux komutunu çalıştırabiliriz.
 *
 * curl -X POST -d 'grant_type=password&username=app_user&password=app_user!&client_id=elastic_query_web_client&client_secret='adminpanelindenal' http://
 * localhost:8081/auth/realms/microservices_realm/protocol/openid-connect/token

 *
 * postman'den http:/localhost:8183/elastic-query-service/documents/1
 *
 * 1 id'ye sahip document'i getirmeye çalışıyoruz. init-data.sql'de 1 id'li document'e sadece app_user için yetki vermiştik bizim token'ımız app_user
 * için olduğu için bu istek çalışacaktır.
 *
 * postman'den http:/localhost:8183/elastic-query-service/documents/2
 *
 * 2 id'ye sahip document'i getirmeye çalışıyoruz. init-data.sql'de 2 id'li document'e sadece app_admin için yetki vermiştik bizim token'ımız app_user
 * için olduğu için bu istek çalışmayacaktır.
 * */
/*
 * RestController = Controller + Response Body
 *
 * produces kısmında verdiğimiz ifade request'lerin accept header kısmına koyulmalı.
 * */
@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping(value = "/documents", produces = "application/vnd.api.v1+json")
public class ElasticDocumentController {

    private final static Logger LOG = LoggerFactory.getLogger(ElasticDocumentController.class);

    private final ElasticQueryService elasticQueryService;

    @Value("${server.port}")
    private String port;

    public ElasticDocumentController(ElasticQueryService elasticQueryService) {
        this.elasticQueryService = elasticQueryService;
    }

    @PostAuthorize("hasPermission(returnObject,'READ')")
    @Operation(summary = "Get all elastic documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succesfull Response", content = {
                    @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/")
    public @ResponseBody ResponseEntity<List<ElasticQueryServiceResponseModel>> getAllDocuments() {
        List<ElasticQueryServiceResponseModel> response = elasticQueryService.getAllDocuments();
        LOG.info("ElasticSearch returned {} of documents", response.size());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasPermission(#id,'ElasticQueryServiceResponseModel','READ')")
    @Operation(summary = "Get elastic document by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succesfull Response", content = {
                    @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/{id}")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModel> getDocumentById(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        LOG.debug("ElasticSearch returned document with id {} on port {}", id, port);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get elastic document by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succesfull Response", content = {
                    @Content(
                            mediaType = "application/vnd.api.v2+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping(value = "/{id}", produces = "application/vnd.api.v2+json")
    public @ResponseBody ResponseEntity<ElasticQueryServiceResponseModelV2> getDocumentByIdV2(@PathVariable @NotEmpty String id) {
        ElasticQueryServiceResponseModel response = elasticQueryService.getDocumentById(id);
        LOG.debug("ElasticSearch returned document with id {} on port {}", id, port);
        ElasticQueryServiceResponseModelV2 modelV2 = getModelV2(response);
        return ResponseEntity.ok(modelV2);
    }

    /*
     * APP_USER_ROLE veya APP_SUPER_USER_ROLE rolüne sahip olan kullanıcıya izin vereceğiz
     * SCOPE_APP_USER_ROLE başına spring tarafından TwitterQueryUserJwtConverter'de belirttiğimiz gibi SCOPE_ prefixi eklenir
     * hasRole veya hasAuthority'den herhangi birini kullanabiliriz ikisi de true dönmeli.
     * */
    @PreAuthorize("hasRole('APP_USER_ROLE')||hasRole('APP_SUPER_USER_ROLE') ||hasAuthority('SCOPE_APP_USER_ROLE')")
    @PostAuthorize("hasPermission(returnObject,'READ')")
    @Operation(summary = "Get elastic document by text")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succesfull Response", content = {
                    @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = ElasticQueryServiceResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/get-document-by-text")
    public @ResponseBody ResponseEntity<ElasticQueryServiceAnalyticsResponseModel> getDocumentByText(
            @RequestBody @Valid ElasticQueryServiceRequestModel requestModel,
            @AuthenticationPrincipal TwitterQueryUser principal,
            @RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient oAuth2AuthorizedClient) {

        LOG.info("User {} querying documents for text {}", principal.getUsername(), requestModel.getText());

        ElasticQueryServiceAnalyticsResponseModel response = elasticQueryService.getDocumentByText(requestModel.getText(),
                oAuth2AuthorizedClient.getAccessToken().getTokenValue());

        LOG.info("ElasticSearch returned {} of documents on port {}", response.getElasticQueryServiceResponseModels().size(), port);
        return ResponseEntity.ok(response);
    }

    private ElasticQueryServiceResponseModelV2 getModelV2(ElasticQueryServiceResponseModel model) {
        ElasticQueryServiceResponseModelV2 modelV2 = ElasticQueryServiceResponseModelV2.builder()
                .id(Long.parseLong(model.getId()))
                .text(model.getText())
                .text2("Text V2")
                .userId(model.getUserId())
                .build();

        modelV2.add(model.getLinks());

        return modelV2;
    }
}
