package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.service.FftDiagnosticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fft")
public class FftDiagnosticsController {

    private final FftDiagnosticsService service;

    public FftDiagnosticsController(
            FftDiagnosticsService service
    ) {
        this.service = service;
    }

    @GetMapping("/spectrum")
    public List<FftSpectrumPoint> spectrum(
            @RequestParam String domain,
            @RequestParam String metric
    ) {
        return service.spectrum(
                AnalysisDomain.from(domain),
                AnalysisMetric.from(metric)
        );
    }

    @GetMapping("/seasonality-strength")
    public SeasonalityStrengthResponse seasonalityStrength(
            @RequestParam String domain,
            @RequestParam String metric
    ) {
        return service.seasonalityStrength(
                AnalysisDomain.from(domain),
                AnalysisMetric.from(metric)
        );
    }

    @GetMapping("/compare")
    public List<DatasetComparisonResponse> compare(
            @RequestParam String metric
    ) {
        return service.compare(
                AnalysisMetric.from(metric)
        );
    }
}