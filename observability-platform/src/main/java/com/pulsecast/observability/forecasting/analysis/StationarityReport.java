package com.pulsecast.observability.forecasting.analysis;

public class StationarityReport {

    private final double overallMean;
    private final double firstHalfMean;
    private final double secondHalfMean;

    private final double overallVariance;
    private final double firstHalfVariance;
    private final double secondHalfVariance;

    private final double varianceDifferencePercent;

    private final boolean stationary;

    public StationarityReport(
            double overallMean,
            double firstHalfMean,
            double secondHalfMean,
            double overallVariance,
            double firstHalfVariance,
            double secondHalfVariance,
            double varianceDifferencePercent,
            boolean stationary
    ) {

        this.overallMean = overallMean;
        this.firstHalfMean = firstHalfMean;
        this.secondHalfMean = secondHalfMean;

        this.overallVariance = overallVariance;
        this.firstHalfVariance = firstHalfVariance;
        this.secondHalfVariance = secondHalfVariance;

        this.varianceDifferencePercent =
                varianceDifferencePercent;

        this.stationary = stationary;
    }

    public double getOverallMean() {
        return overallMean;
    }

    public double getFirstHalfMean() {
        return firstHalfMean;
    }

    public double getSecondHalfMean() {
        return secondHalfMean;
    }

    public double getOverallVariance() {
        return overallVariance;
    }

    public double getFirstHalfVariance() {
        return firstHalfVariance;
    }

    public double getSecondHalfVariance() {
        return secondHalfVariance;
    }

    public double getVarianceDifferencePercent() {
        return varianceDifferencePercent;
    }

    public boolean isStationary() {
        return stationary;
    }
}