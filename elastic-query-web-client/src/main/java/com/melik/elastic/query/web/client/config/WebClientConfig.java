package com.melik.elastic.query.web.client.config;

import com.melik.app.config.data.config.ElasticQueryWebClientConfigData;
import com.melik.app.config.data.config.UserConfigData;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.concurrent.TimeUnit;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Configuration
/*
* Manuel olarak Client Load Balancer ekledik.
* */
@LoadBalancerClient(name = "elastic-query-service",configuration = ElasticQueryServiceInstanceListSupplierConfig.class)
public class WebClientConfig {

    private final ElasticQueryWebClientConfigData.WebClient elasticQueryWebClientConfigData;
    private final UserConfigData userConfigData;

    public WebClientConfig(ElasticQueryWebClientConfigData webClientConfigData, UserConfigData userConfigData) {
        this.elasticQueryWebClientConfigData = webClientConfigData.getWebClient();
        this.userConfigData = userConfigData;
    }

    @LoadBalanced
    @Bean("webClientBuilder")
    /*
    * default olarak bu name ile oluşuyor zaten. Qualifier kullandığımız yerde bu ismi kullanıyoruz daha kolay anlaşılsın diye manuel isim ekledik.
    * Qualifier kullandık çünkü spring'in default webclient configurationunu değil de aşağıda kendi oluşturduğumuz configuration'u kullanmak istedik.
    * */
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .filter(ExchangeFilterFunctions
                        .basicAuthentication(userConfigData.getUsername(), userConfigData.getPassword()))
                .baseUrl(elasticQueryWebClientConfigData.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, elasticQueryWebClientConfigData.getContentType())
                .defaultHeader(HttpHeaders.ACCEPT, elasticQueryWebClientConfigData.getAcceptType())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(getTcpClient())))
                .codecs(clientCodecConfigurer -> clientCodecConfigurer
                        .defaultCodecs()
                        .maxInMemorySize(elasticQueryWebClientConfigData.getMaxInMemorySize()));
    }

    private TcpClient getTcpClient() {
        return TcpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, elasticQueryWebClientConfigData.getConnectTimeoutMs())
                .doOnConnected(connection -> {
                    connection.addHandlerLast(
                            new ReadTimeoutHandler(elasticQueryWebClientConfigData.getReadTimeoutMs(), TimeUnit.MILLISECONDS));
                    connection.addHandlerLast(
                            new WriteTimeoutHandler(elasticQueryWebClientConfigData.getWriteTimeoutMs(),
                                    TimeUnit.MILLISECONDS));
                });
    }
}
