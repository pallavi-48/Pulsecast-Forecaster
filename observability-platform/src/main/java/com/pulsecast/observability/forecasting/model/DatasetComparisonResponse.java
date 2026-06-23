package com.pulsecast.observability.forecasting.model;

public class DatasetComparisonResponse {

    public String domain;
    public String metric;

    public double dailyStrengthPercent;
    public double weeklyStrengthPercent;
    public double seasonalVarianceExplainedPercent;
    public double nonPeriodicPowerPercent;

    public String interpretation;

    public DatasetComparisonResponse(
            String domain,
            String metric,
            double dailyStrengthPercent,
            double weeklyStrengthPercent,
            double seasonalVarianceExplainedPercent,
            double nonPeriodicPowerPercent,
            String interpretation
    ) {
        this.domain = domain;
        this.metric = metric;
        this.dailyStrengthPercent = dailyStrengthPercent;
        this.weeklyStrengthPercent = weeklyStrengthPercent;
        this.seasonalVarianceExplainedPercent = seasonalVarianceExplainedPercent;
        this.nonPeriodicPowerPercent = nonPeriodicPowerPercent;
        this.interpretation = interpretation;
    }
}