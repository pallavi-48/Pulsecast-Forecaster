package com.pulsecast.observability.forecasting.model;

public class ForecastChartResponse {

    public String metric;

    public double[] actualValues;
    public double[] forecastValues;

    public double[] lowerBounds;
    public double[] upperBounds;

    public ForecastChartResponse(
            String metric,
            double[] actualValues,
            double[] forecastValues,
            double[] lowerBounds,
            double[] upperBounds
    ) {
        this.metric = metric;
        this.actualValues = actualValues;
        this.forecastValues = forecastValues;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }
}