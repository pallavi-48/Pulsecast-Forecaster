package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.model.DatasetFile;
import com.pulsecast.observability.forecasting.model.ForecastChartResponse;
import com.pulsecast.observability.forecasting.model.MetricType;
import com.pulsecast.observability.forecasting.service.MetricForecastService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forecast/chart")
public class ForecastChartController {

    private final MetricForecastService metricForecastService;

    public ForecastChartController(
            MetricForecastService metricForecastService
    ) {
        this.metricForecastService =
                metricForecastService;
    }

    @GetMapping("/{metric}")
    public ForecastChartResponse oldChart(
            @PathVariable String metric
    ) {

        MetricType metricType =
                MetricType.valueOf(
                        metric.toUpperCase()
                );

        return metricForecastService.forecastChart(
                DatasetFile.OBSERVABILITY_TRAINING,
                DatasetFile.OBSERVABILITY_CURRENT,
                metricType
        );
    }

    @GetMapping
    public ForecastChartResponse chart(
            @RequestParam String training,
            @RequestParam String test,
            @RequestParam String metric
    ) {

        DatasetFile trainingFile =
                DatasetFile.fromName(training);

        DatasetFile testFile =
                DatasetFile.fromName(test);

        MetricType metricType =
                MetricType.valueOf(
                        metric.toUpperCase()
                );

        return metricForecastService.forecastChart(
                trainingFile,
                testFile,
                metricType
        );
    }
}