package com.melik.reactive.elastic.query.web.client.api;

import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import com.melik.reactive.elastic.query.web.client.service.ElasticQueryWebClient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

@Controller
public class QueryController {

    private final static Logger LOG = LoggerFactory.getLogger(QueryController.class);

    private final ElasticQueryWebClient elasticQueryWebClient;

    public QueryController(ElasticQueryWebClient elasticQueryWebClient) {
        this.elasticQueryWebClient = elasticQueryWebClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("elasticQueryClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel, Model model) {
        Flux<ElasticQueryWebClientResponseModel> responseModel = elasticQueryWebClient.getDataByText(requestModel);
        responseModel = responseModel.log();
        IReactiveDataDriverContextVariable reactiveData = new ReactiveDataDriverContextVariable(responseModel, 1);
        model.addAttribute("elasticQueryClientResponseModels", reactiveData);
        model.addAttribute("searchText", requestModel.getText());
        model.addAttribute("elasticQueryClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        LOG.info("Returning from reactive client controller for text {}", requestModel.getText());
        return "home";
    }
}
