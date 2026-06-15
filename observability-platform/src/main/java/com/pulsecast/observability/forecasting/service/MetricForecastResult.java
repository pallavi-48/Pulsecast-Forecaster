package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.model.MetricType;

public class MetricForecastResult {

    public MetricType metricType;

    public double mae;
    public double rmse;

    public double complianceScore;
    public boolean compliant;

    public int anomalyCount;

    public double expectedAverage;
    public double expectedMin;
    public double expectedMax;
}