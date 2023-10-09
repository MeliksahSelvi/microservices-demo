package com.melik.elastic.query.service.business.impl;

import com.melik.app.config.data.config.ElasticQueryServiceConfigData;
import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.client.service.ElasticQueryClient;
import com.melik.elastic.query.service.business.ElasticQueryService;
import com.melik.elastic.query.service.common.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.exception.ElasticQueryServiceException;
import com.melik.elastic.query.service.model.ElasticQueryServiceAnalyticsResponseModel;
import com.melik.elastic.query.service.model.ElasticQueryServiceWordCountResponseModel;
import com.melik.elastic.query.service.model.assembler.ElasticQueryServiceResponseModelAssembler;
import com.melik.elastic.query.service.util.QueryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.melik.mdc.util.Constants.CORRELATION_ID_HEADER;
import static com.melik.mdc.util.Constants.CORRELATION_ID_KEY;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Service
public class TwitterElasticQueryService implements ElasticQueryService {

    private final static Logger LOG = LoggerFactory.getLogger(TwitterElasticQueryService.class);

    private final ElasticQueryServiceResponseModelAssembler modelAssembler;
    private final ElasticQueryClient<TwitterIndexModel> elasticQueryClient;
    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;
    private final WebClient.Builder webClientBuilder;

    public TwitterElasticQueryService(ElasticQueryServiceResponseModelAssembler modelAssembler,
                                      ElasticQueryClient<TwitterIndexModel> elasticQueryClient,
                                      ElasticQueryServiceConfigData elasticQueryServiceConfigData,
                                      @Qualifier("webClientBuilder") WebClient.Builder webClientBuilder) {
        this.modelAssembler = modelAssembler;
        this.elasticQueryClient = elasticQueryClient;
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public ElasticQueryServiceResponseModel getDocumentById(String id) {
        TwitterIndexModel twitterIndexModel = elasticQueryClient.getIndexModelById(id);
        ElasticQueryServiceResponseModel responseModel = modelAssembler.toModel(twitterIndexModel);
        LOG.info("Querying elasticsearch by id {}", id);
        return responseModel;
    }

    @Override
    public List<ElasticQueryServiceResponseModel> getAllDocuments() {
        List<TwitterIndexModel> twitterIndexModels = elasticQueryClient.getAllIndexModels();
        List<ElasticQueryServiceResponseModel> responseModels = modelAssembler.toModels(twitterIndexModels);
        LOG.info("Querying all documents in elasticsearch");
        return responseModels;
    }

    @Override
    public ElasticQueryServiceAnalyticsResponseModel getDocumentByText(String text, String accessToken) {
        LOG.info("Querying elasticsearch by text {}", text);
        List<TwitterIndexModel> twitterIndexModels = elasticQueryClient.getIndexModelByText(text);
        List<ElasticQueryServiceResponseModel> responseModels = modelAssembler.toModels(twitterIndexModels);
        return ElasticQueryServiceAnalyticsResponseModel.builder()
                .elasticQueryServiceResponseModels(responseModels)
                .wordCount(getWordCount(text, accessToken))
                .build();
    }

    private Long getWordCount(String text, String accessToken) {
        if (QueryType.KAFKA_STATE_STORE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromKafkaStateStore(text, accessToken).getWordCount();
        } else if (QueryType.ANALYTICS_DATABASE.getType().equals(elasticQueryServiceConfigData.getWebClient().getQueryType())) {
            return getFromAnalyticsDatabase(text, accessToken).getWordCount();
        }
        return 0L;
    }

    private ElasticQueryServiceWordCountResponseModel getFromKafkaStateStore(String text, String accessToken) {
        ElasticQueryServiceConfigData.Query queryFromKafkaStateStore = elasticQueryServiceConfigData.getQueryFromKafkaStateStore();
        return retrieveResponseModel(text, accessToken, queryFromKafkaStateStore);
    }

    private ElasticQueryServiceWordCountResponseModel getFromAnalyticsDatabase(String text, String accessToken) {
        ElasticQueryServiceConfigData.Query queryFromAnalyticsDatabase = elasticQueryServiceConfigData.getQueryFromAnalyticsDatabase();
        return retrieveResponseModel(text, accessToken, queryFromAnalyticsDatabase);
    }

    private ElasticQueryServiceWordCountResponseModel retrieveResponseModel(String text,
                                                                            String accessToken,
                                                                            ElasticQueryServiceConfigData.Query
                                                                                    queryFromKafkaStateStore) {
        return webClientBuilder
                .build()
                .method(HttpMethod.valueOf(queryFromKafkaStateStore.getMethod()))
                .uri(queryFromKafkaStateStore.getUri(), uriBuilder -> uriBuilder.build(text))
                .headers(h -> {
                    h.setBearerAuth(accessToken);
                    h.set(CORRELATION_ID_HEADER, MDC.get(CORRELATION_ID_KEY));
                })
                .accept(MediaType.valueOf(queryFromKafkaStateStore.getAccept()))
                .retrieve()
                .onStatus(
                        s -> s.equals(HttpStatus.UNAUTHORIZED),
                        clientResponse -> Mono.just(new BadCredentialsException("Not authenticated")))
                .onStatus(
                        s -> s.equals(HttpStatus.BAD_REQUEST),
                        clientResponse -> Mono.just(new
                                ElasticQueryServiceException(clientResponse.statusCode().toString())))
                .onStatus(
                        s -> s.equals(HttpStatus.INTERNAL_SERVER_ERROR),
                        clientResponse -> Mono.just(new Exception(clientResponse.statusCode().toString())))
                .bodyToMono(ElasticQueryServiceWordCountResponseModel.class)
                .log()
                .block();
    }


}
