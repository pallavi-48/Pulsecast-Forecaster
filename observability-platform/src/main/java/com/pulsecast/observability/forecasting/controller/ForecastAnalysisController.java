package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.service.ForecastAnalysisService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
public class ForecastAnalysisController {

    private final ForecastAnalysisService service;

    public ForecastAnalysisController(
            ForecastAnalysisService service
    ) {
        this.service = service;
    }

    @GetMapping("/training-trend")
    public List<GraphPoint> trainingTrend(
            @RequestParam String domain,
            @RequestParam String metric,
            @RequestParam(defaultValue = "288") int window
    ) {
        return service.trainingTrend(
                AnalysisDomain.from(domain),
                AnalysisMetric.from(metric),
                window
        );
    }

    @GetMapping("/forecast-comparison")
    public List<GraphPoint> forecastComparison(
            @RequestParam String domain,
            @RequestParam String testType,
            @RequestParam String metric
    ) {
        return service.forecastComparison(
                AnalysisDomain.from(domain),
                TestType.from(testType),
                AnalysisMetric.from(metric)
        );
    }

    @GetMapping("/residuals")
    public List<GraphPoint> residuals(
            @RequestParam String domain,
            @RequestParam String testType,
            @RequestParam String metric
    ) {
        return service.residuals(
                AnalysisDomain.from(domain),
                TestType.from(testType),
                AnalysisMetric.from(metric)
        );
    }

    @GetMapping("/fft-spectrum")
    public List<FftPoint> fftSpectrum(
            @RequestParam String domain,
            @RequestParam String metric
    ) {
        return service.fftSpectrum(
                AnalysisDomain.from(domain),
                AnalysisMetric.from(metric)
        );
    }
}