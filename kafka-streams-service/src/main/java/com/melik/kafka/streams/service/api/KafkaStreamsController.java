package com.melik.kafka.streams.service.api;

import com.melik.kafka.streams.service.model.KafkaStreamsResponseModel;
import com.melik.kafka.streams.service.runner.StreamsRunner;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/", produces = "application/vnd.api.v1+json")
public class KafkaStreamsController {

    private final static Logger LOG = LoggerFactory.getLogger(KafkaStreamsController.class);

    private final StreamsRunner<String, Long> kafkaStreamsRunner;

    public KafkaStreamsController(StreamsRunner<String, Long> kafkaStreamsRunner) {
        this.kafkaStreamsRunner = kafkaStreamsRunner;
    }

    @Operation(summary = "Get word count by word")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succes", content = {
                    @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = KafkaStreamsResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })
    @GetMapping("/get-word-count-by-word/{word}")
    public @ResponseBody ResponseEntity<KafkaStreamsResponseModel> getWordCountByWord(@PathVariable @NotEmpty String word) {
        Long wordCount = kafkaStreamsRunner.getValueByKey(word);
        LOG.info("Word count {} returned for word {}", wordCount, word);
        return ResponseEntity.ok(KafkaStreamsResponseModel.builder()
                .word(word)
                .wordCount(wordCount)
                .build());
    }
}
