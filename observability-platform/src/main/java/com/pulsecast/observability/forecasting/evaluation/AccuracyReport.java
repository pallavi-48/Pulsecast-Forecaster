package com.pulsecast.observability.forecasting.evaluation;

public class AccuracyReport {

    private final double mae;
    private final double rmse;

    public AccuracyReport(
            double mae,
            double rmse
    ) {
        this.mae = mae;
        this.rmse = rmse;
    }

    public double getMae() {
        return mae;
    }

    public double getRmse() {
        return rmse;
    }
}