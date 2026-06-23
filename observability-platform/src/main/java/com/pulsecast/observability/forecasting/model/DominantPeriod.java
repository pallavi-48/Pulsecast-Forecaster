package com.pulsecast.observability.forecasting.model;

public class DominantPeriod {

    public double periodSamples;
    public double periodHours;
    public double power;
    public double percentageOfTotalPower;

    public DominantPeriod(
            double periodSamples,
            double periodHours,
            double power,
            double percentageOfTotalPower
    ) {
        this.periodSamples = periodSamples;
        this.periodHours = periodHours;
        this.power = power;
        this.percentageOfTotalPower = percentageOfTotalPower;
    }
}