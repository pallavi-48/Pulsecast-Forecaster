package com.pulsecast.observability.forecasting.model;

public class SarimaModel {

    private final SarimaParameters parameters;
    private final double meanTraffic;
    private final double arCoefficient;
    private final double maCoefficient;
    private final double seasonalCoefficient;
    private final double seasonalMaCoefficient;
    private final double residualStdDev;
    private final double[] residuals;

    public SarimaModel(
            SarimaParameters parameters,
            double meanTraffic,
            double arCoefficient,
            double maCoefficient,
            double seasonalCoefficient,
            double seasonalMaCoefficient,
            double residualStdDev,
            double[] residuals
    ) {
        this.parameters = parameters;
        this.meanTraffic = meanTraffic;
        this.arCoefficient = arCoefficient;
        this.maCoefficient = maCoefficient;
        this.seasonalCoefficient = seasonalCoefficient;
        this.seasonalMaCoefficient = seasonalMaCoefficient;
        this.residualStdDev = residualStdDev;
        this.residuals = residuals;
    }

    public SarimaParameters getParameters() {
        return parameters;
    }

    public double getMeanTraffic() {
        return meanTraffic;
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

    public double getResidualStdDev() {
        return residualStdDev;
    }

    public double[] getResiduals() {
        return residuals;
    }
}