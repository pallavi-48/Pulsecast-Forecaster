package com.pulsecast.observability.forecasting.analysis;

public record MetricStatistics(
        double mean,
        double min,
        double max
) {
}