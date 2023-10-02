package com.melik.elastic.query.web.client.config;

import com.melik.app.config.data.config.ElasticQueryWebClientConfigData;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Configuration
/*
 * Spring Cloud ile gelen diğer configuration'ları disabled hale getirip bu configuration'u etkin hale getirmekte işimizi görecek.
 * */
@Primary
public class ElasticQueryServiceInstanceListSupplierConfig implements ServiceInstanceListSupplier {

    private final ElasticQueryWebClientConfigData.WebClient webClientConfig;

    public ElasticQueryServiceInstanceListSupplierConfig(ElasticQueryWebClientConfigData webClientConfigData) {
        this.webClientConfig = webClientConfigData.getWebClient();
    }


    @Override
    public String getServiceId() {
        return webClientConfig.getServiceId();
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(
                webClientConfig.getInstances().stream()
                        .map(instance ->
                                new DefaultServiceInstance(
                                        instance.getId(),
                                        getServiceId(),
                                        instance.getHost(),
                                        instance.getPort(),
                                        false
                                )).collect(Collectors.toList()));
    }
}
