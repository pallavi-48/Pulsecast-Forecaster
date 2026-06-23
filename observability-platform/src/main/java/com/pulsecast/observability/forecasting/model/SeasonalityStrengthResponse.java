package com.pulsecast.observability.forecasting.model;

import java.util.List;

public class SeasonalityStrengthResponse {

    public String domain;
    public String metric;

    public double dailyStrengthPercent;
    public double weeklyStrengthPercent;
    public double harmonicStrengthPercent;
    public double seasonalVarianceExplainedPercent;
    public double nonPeriodicPowerPercent;

    public List<DominantPeriod> dominantPeriods;

    public SeasonalityStrengthResponse(
            String domain,
            String metric,
            double dailyStrengthPercent,
            double weeklyStrengthPercent,
            double harmonicStrengthPercent,
            double seasonalVarianceExplainedPercent,
            double nonPeriodicPowerPercent,
            List<DominantPeriod> dominantPeriods
    ) {
        this.domain = domain;
        this.metric = metric;
        this.dailyStrengthPercent = dailyStrengthPercent;
        this.weeklyStrengthPercent = weeklyStrengthPercent;
        this.harmonicStrengthPercent = harmonicStrengthPercent;
        this.seasonalVarianceExplainedPercent = seasonalVarianceExplainedPercent;
        this.nonPeriodicPowerPercent = nonPeriodicPowerPercent;
        this.dominantPeriods = dominantPeriods;
    }
}