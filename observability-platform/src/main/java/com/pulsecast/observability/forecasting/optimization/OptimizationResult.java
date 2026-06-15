package com.pulsecast.observability.forecasting.optimization;

public class OptimizationResult {

    private final double arCoefficient;
    private final double maCoefficient;
    private final double seasonalCoefficient;
    private final double seasonalMaCoefficient;
    private final double sse;

    public OptimizationResult(
            double arCoefficient,
            double maCoefficient,
            double seasonalCoefficient,
            double seasonalMaCoefficient,
            double sse
    ) {
        this.arCoefficient = arCoefficient;
        this.maCoefficient = maCoefficient;
        this.seasonalCoefficient = seasonalCoefficient;
        this.seasonalMaCoefficient = seasonalMaCoefficient;
        this.sse = sse;
    }

    public double getArCoefficient() {
        return arCoefficient;
    }

    public double getMaCoefficient() {
        return maCoefficient;
    }

    public double getSeasonalCoefficient() {
        return seasonalCoefficient;
    }

    public double getSeasonalMaCoefficient() {
        return seasonalMaCoefficient;
    }

    public double getSse() {
        return sse;
    }
}