package com.melik.elastic.query.service.model.assembler;

import com.melik.elastic.model.index.impl.TwitterIndexModel;
import com.melik.elastic.query.service.api.ElasticDocumentController;
import com.melik.elastic.query.service.model.ElasticQueryServiceResponseModel;
import com.melik.elastic.query.service.transformer.ElasticToResponseModelTransformer;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * @Author mselvi
 * @Created 30.09.2023
 */

@Component
public class ElasticQueryServiceResponseModelAssembler
        extends RepresentationModelAssemblerSupport<TwitterIndexModel, ElasticQueryServiceResponseModel> {

    private final ElasticToResponseModelTransformer transformer;

    public ElasticQueryServiceResponseModelAssembler(ElasticToResponseModelTransformer transformer) {
        super(ElasticDocumentController.class, ElasticQueryServiceResponseModel.class);
        this.transformer = transformer;
    }

    @Override
    public ElasticQueryServiceResponseModel toModel(TwitterIndexModel twitterIndexModel) {
        ElasticQueryServiceResponseModel responseModel = transformer.getResponseModel(twitterIndexModel);

        responseModel.add(
                linkTo(methodOn(ElasticDocumentController.class)
                        .getDocumentById(twitterIndexModel.getId()))
                        .withSelfRel());
        responseModel.add(linkTo(ElasticDocumentController.class)
                .withRel("documents"));
        return responseModel;
    }

    public List<ElasticQueryServiceResponseModel> toModels(List<TwitterIndexModel> twitterIndexModels){
        return twitterIndexModels.stream().map(this::toModel).collect(Collectors.toList());
    }
}
