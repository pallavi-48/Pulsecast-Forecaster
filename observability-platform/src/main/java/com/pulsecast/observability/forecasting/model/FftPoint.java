package com.pulsecast.observability.forecasting.model;

public class FftPoint {

    public double periodSamples;
    public double periodHours;
    public double power;

    public FftPoint(
            double periodSamples,
            double periodHours,
            double power
    ) {
        this.periodSamples = periodSamples;
        this.periodHours = periodHours;
        this.power = power;
    }
}