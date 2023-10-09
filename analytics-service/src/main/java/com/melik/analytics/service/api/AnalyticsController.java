package com.melik.analytics.service.api;

import com.melik.analytics.service.business.AnalyticsService;
import com.melik.analytics.service.model.AnalyticsResponseModel;
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

import java.util.Optional;

/**
 * @Author mselvi
 * @Created 06.10.2023
 */

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(value = "/", produces = "application/vnd.api.v1+json")
public class AnalyticsController {

    private static final Logger LOG = LoggerFactory.getLogger(AnalyticsController.class);

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }


    @GetMapping("/get-word-count-by-word/{word}")
    @Operation(summary = "Get analytics by word")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Succes", content = {
                    @Content(
                            mediaType = "application/vnd.api.v1+json",
                            schema = @Schema(implementation = AnalyticsResponseModel.class)
                    )
            }),
            @ApiResponse(responseCode = "400", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Unexpected Error")
    })
    public @ResponseBody ResponseEntity<AnalyticsResponseModel> getWordCountByWord(@PathVariable @NotEmpty String word) {
        Optional<AnalyticsResponseModel> response = analyticsService.getWordAnalytics(word);
        if (response.isPresent()) {
            AnalyticsResponseModel analyticsResponseModel = response.get();
            LOG.info("Analytics data returned with id {}", analyticsResponseModel.getId());
            return ResponseEntity.ok(analyticsResponseModel);
        }
        return ResponseEntity.ok(AnalyticsResponseModel.builder().build());
    }
}

