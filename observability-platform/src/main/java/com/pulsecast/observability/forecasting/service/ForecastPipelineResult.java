package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.evaluation.AnomalyPoint;

import java.util.List;

public class ForecastPipelineResult {

    public double mae;
    public double rmse;
    public double complianceScore;
    public boolean compliant;
    public int anomalyCount;

    public double expectedAverage;
    public double expectedMin;
    public double expectedMax;

    public double[] predictedTraffic;
    public double[] actualTraffic;

    public List<AnomalyPoint> anomalies;
}