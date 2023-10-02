package com.melik.elastic.query.web.client.service.impl;

import com.melik.app.config.data.config.ElasticQueryWebClientConfigData;
import com.melik.elastic.query.web.client.exception.ElasticQueryWebClientException;
import com.melik.elastic.query.web.client.model.ElasticQueryWebClientRequestModel;
import com.melik.elastic.query.web.client.model.ElasticQueryWebClientResponseModel;
import com.melik.elastic.query.web.client.service.ElasticQueryWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Service
public class TwitterElasticQueryWebClient implements ElasticQueryWebClient {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryWebClient.class);
    private final WebClient.Builder webClientBuilder;
    private final ElasticQueryWebClientConfigData elasticQueryWebClientConfigData;

    public TwitterElasticQueryWebClient(@Qualifier("webClientBuilder") WebClient.Builder webClientBuilder,
                                        ElasticQueryWebClientConfigData elasticQueryWebClientConfigData) {
        this.webClientBuilder = webClientBuilder;
        this.elasticQueryWebClientConfigData = elasticQueryWebClientConfigData;
    }


    @Override
    public List<ElasticQueryWebClientResponseModel> getDataByText(ElasticQueryWebClientRequestModel requestModel) {
        LOG.info("Querying by text {}", requestModel.getText());
        List<ElasticQueryWebClientResponseModel> responseModels = getWebClient(requestModel)
                .bodyToFlux(ElasticQueryWebClientResponseModel.class)
                .collectList()
                .block();
        return responseModels;
    }

    private WebClient.ResponseSpec getWebClient(ElasticQueryWebClientRequestModel requestModel) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(elasticQueryWebClientConfigData.getQueryByText().getMethod()))
                .uri(elasticQueryWebClientConfigData.getQueryByText().getUri())
                .accept(MediaType.valueOf(elasticQueryWebClientConfigData.getQueryByText().getAccept()))
                .body(BodyInserters.fromPublisher(Mono.just(requestModel), createParameterizedTypeReference()))
                .retrieve()
                .onStatus(httpStatus -> httpStatus.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not Authenticated")))
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.just(new ElasticQueryWebClientException(HttpStatus.valueOf(clientResponse.statusCode().value()).getReasonPhrase())))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.just(new Exception(HttpStatus.valueOf(clientResponse.statusCode().value()).getReasonPhrase())));
    }

    private <T> ParameterizedTypeReference<T> createParameterizedTypeReference() {
        return new ParameterizedTypeReference<>() {
        };
    }
}
