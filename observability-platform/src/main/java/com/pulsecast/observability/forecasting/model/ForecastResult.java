package com.pulsecast.observability.forecasting.model;

public class ForecastResult {

    double[] forecastValues;
    double[] lowerBounds;
    double[] upperBounds;

    public ForecastResult(
            double[] forecastValues
    ) {
        this.forecastValues = forecastValues;
        this.lowerBounds = new double[forecastValues.length];
        this.upperBounds = new double[forecastValues.length];
    }

    public ForecastResult(
            double[] forecastValues,
            double[] lowerBounds,
            double[] upperBounds
    ) {
        this.forecastValues = forecastValues;
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    public double[] getForecastValues() {
        return forecastValues;
    }

    public double[] getLowerBounds() {
        return lowerBounds;
    }

    public double[] getUpperBounds() {
        return upperBounds;
    }
}