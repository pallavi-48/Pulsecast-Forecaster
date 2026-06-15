package com.pulsecast.observability.forecasting.analysis;

import com.pulsecast.observability.forecasting.domain.TimeSeries;

import java.util.Arrays;

public class DatasetAnalysisService {

    public MetricStatistics analyzeTraffic(TimeSeries series) {
        return calculate(series.getTrafficSeries());
    }

    public MetricStatistics analyzeLatency(TimeSeries series) {
        return calculate(series.getLatencySeries());
    }

    public MetricStatistics analyzeErrors(TimeSeries series) {
        return calculate(series.getErrorSeries());
    }

    public MetricStatistics analyzeCpu(TimeSeries series) {
        return calculate(series.getCpuSeries());
    }

    private MetricStatistics calculate(double[] values) {

        double mean =
                Arrays.stream(values)
                        .average()
                        .orElse(0);

        double min =
                Arrays.stream(values)
                        .min()
                        .orElse(0);

        double max =
                Arrays.stream(values)
                        .max()
                        .orElse(0);

        return new MetricStatistics(
                mean,
                min,
                max
        );
    }
}