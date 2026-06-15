package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.service.ForecastPipelineResult;
import com.pulsecast.observability.forecasting.service.ForecastPipelineService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/forecast")
public class ForecastController {

    private final ForecastPipelineService service;

    public ForecastController(
            ForecastPipelineService service
    ) {
        this.service = service;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {

        ForecastPipelineResult result =
                service.runPipeline();

        return Map.of(
                "expectedAverage", result.expectedAverage,
                "expectedMin", result.expectedMin,
                "expectedMax", result.expectedMax,
                "complianceScore", result.complianceScore,
                "compliant", result.compliant,
                "anomalyCount", result.anomalyCount
        );
    }

    @GetMapping("/accuracy")
    public Map<String, Object> accuracy() {

        ForecastPipelineResult result =
                service.runPipeline();

        return Map.of(
                "mae", result.mae,
                "rmse", result.rmse
        );
    }

    @GetMapping("/compliance")
    public Map<String, Object> compliance() {

        ForecastPipelineResult result =
                service.runPipeline();

        return Map.of(
                "score", result.complianceScore,
                "compliant", result.compliant
        );
    }

    @GetMapping("/anomalies")
    public Object anomalies() {

        ForecastPipelineResult result =
                service.runPipeline();

        return result.anomalies;
    }

    @GetMapping("/next-day")
    public double[] nextDayForecast() {

        ForecastPipelineResult result =
                service.runPipeline();

        return result.predictedTraffic;
    }
}