package com.pulsecast.observability.forecasting.service;

public class ForecastQuestionResponse {

    public String metric;
    public String questionType;
    public String horizon;
    public String day;

    public double expectedAverage;
    public double expectedMin;
    public double expectedMax;

    public Double mae;
    public Double rmse;

    public Double complianceScore;
    public Boolean compliant;

    public Integer anomalyCount;

    public String answer;
}