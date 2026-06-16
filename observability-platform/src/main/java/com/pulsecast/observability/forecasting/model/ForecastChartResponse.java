package com.pulsecast.observability.forecasting.model;

public class ForecastChartResponse {

    public String metric;

    public String trainingDataset;

    public String testDataset;

    public double[] actualValues;

    public double[] forecastValues;

    public double[] lowerBounds;

    public double[] upperBounds;

    public double[] trainingValues;

    public ForecastChartResponse(
            String metric,
            String trainingDataset,
            String testDataset,
            double[] actualValues,
            double[] forecastValues,
            double[] lowerBounds,
            double[] upperBounds,
            double[] trainingValues
    ) {
        this.metric = metric;
        this.trainingDataset = trainingDataset;
        this.testDataset = testDataset;
        this.actualValues = actualValues;
        this.forecastValues = forecastValues;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.trainingValues = trainingValues;
    }
}