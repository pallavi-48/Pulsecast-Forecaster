package com.pulsecast.observability.forecasting.model;

public class ForecastPoint {

    private final double predictedValue;
    private final double lowerBound;
    private final double upperBound;
    private final double residualStdDev;

    public ForecastPoint(
            double predictedValue,
            double lowerBound,
            double upperBound,
            double residualStdDev
    ) {
        this.predictedValue = predictedValue;
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.residualStdDev = residualStdDev;
    }

    public double getPredictedValue() {
        return predictedValue;
    }

    public double getLowerBound() {
        return lowerBound;
    }

    public double getUpperBound() {
        return upperBound;
    }

    public double getResidualStdDev() {
        return residualStdDev;
    }
}