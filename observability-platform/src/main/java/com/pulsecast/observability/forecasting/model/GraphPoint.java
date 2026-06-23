package com.pulsecast.observability.forecasting.model;

public class GraphPoint {

    public String timestamp;
    public double actual;
    public double trend;
    public double forecast;
    public double residual;

    public GraphPoint() {
    }

    public GraphPoint(
            String timestamp,
            double actual,
            double trend,
            double forecast,
            double residual
    ) {
        this.timestamp = timestamp;
        this.actual = actual;
        this.trend = trend;
        this.forecast = forecast;
        this.residual = residual;
    }
}