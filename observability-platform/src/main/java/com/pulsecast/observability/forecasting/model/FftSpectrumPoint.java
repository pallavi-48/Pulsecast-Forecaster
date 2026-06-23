package com.pulsecast.observability.forecasting.model;

public class FftSpectrumPoint {

    public int binIndex;
    public double periodSamples;
    public double periodHours;
    public double power;
    public double percentageOfTotalPower;

    public FftSpectrumPoint(
            int binIndex,
            double periodSamples,
            double periodHours,
            double power,
            double percentageOfTotalPower
    ) {
        this.binIndex = binIndex;
        this.periodSamples = periodSamples;
        this.periodHours = periodHours;
        this.power = power;
        this.percentageOfTotalPower = percentageOfTotalPower;
    }
}