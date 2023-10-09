package com.melik.elastic.query.web.client.multiply.api;

import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;
import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientResponseModel;
import com.melik.elastic.query.web.client.multiply.service.ElasticQueryWebClient;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * @Author mselvi
 * @Created 05.10.2023
 */

/*
 * @RestController kullanmadık çünkü thymeleaf ile çalışabilmek için json response etmemeliyiz.
 * RestController @ResponseBody içerdiği için otomatik json kullanıyor.
 * */
@Controller
public class QueryController {

    private final static Logger LOG = LoggerFactory.getLogger(QueryController.class);

    private final ElasticQueryWebClient elasticQueryWebClient;

    public QueryController(ElasticQueryWebClient elasticQueryWebClient) {
        this.elasticQueryWebClient = elasticQueryWebClient;
    }

    /*
     * Return type string olmalı return ettiğimiz değer html thymeleaf ile eşleşecek html dosyasının adı olmalı.
     * biz burada default page'de index.html dosyasını göstereceğiz.
     * */
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
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }

    @PostMapping("/query-by-text")
    public String queryByText(@Valid ElasticQueryWebClientRequestModel requestModel, Model model) {
        LOG.info("Querying with text {}", requestModel.getText());
        List<ElasticQueryWebClientResponseModel> responseModels = elasticQueryWebClient.getDataByText(requestModel);
        model.addAttribute("elasticQueryWebClientResponseModels", responseModels);
        model.addAttribute("searchText", requestModel.getText());
        model.addAttribute("elasticQueryWebClientRequestModel", ElasticQueryWebClientRequestModel.builder().build());
        return "home";
    }
}
