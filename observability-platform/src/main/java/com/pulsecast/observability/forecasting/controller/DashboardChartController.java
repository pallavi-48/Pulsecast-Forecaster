package com.pulsecast.observability.forecasting.controller;

import com.pulsecast.observability.forecasting.model.ChartDataResponse;
import com.pulsecast.observability.forecasting.model.MetricType;
import com.pulsecast.observability.forecasting.service.ChartDataService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forecast/dashboard")
public class DashboardChartController {

    private final ChartDataService chartDataService;

    public DashboardChartController(
            ChartDataService chartDataService
    ) {
        this.chartDataService =
                chartDataService;
    }

    @GetMapping("/compare")
    public ChartDataResponse compare(
            @RequestParam String metric,
            @RequestParam(defaultValue = "daily") String view
    ) {
        return chartDataService.getComparisonChart(
                MetricType.valueOf(
                        metric.toUpperCase()
                ),
                view
        );
    }

    @GetMapping("/training")
    public ChartDataResponse training(
            @RequestParam String metric,
            @RequestParam(defaultValue = "daily") String view
    ) {
        return chartDataService.getTrainingChart(
                MetricType.valueOf(
                        metric.toUpperCase()
                ),
                view
        );
    }
}