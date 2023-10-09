package com.melik.elastic.query.web.client.service;


import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientAnalyticsResponseModel;
import com.melik.elastic.query.web.client.common.model.ElasticQueryWebClientRequestModel;

/**
 * @Author mselvi
 * @Created 02.10.2023
 */

public interface ElasticQueryWebClient {

    ElasticQueryWebClientAnalyticsResponseModel getDataByText(ElasticQueryWebClientRequestModel requestModel);
}
