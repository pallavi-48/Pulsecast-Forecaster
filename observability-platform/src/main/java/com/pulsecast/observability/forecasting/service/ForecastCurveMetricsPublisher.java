package com.pulsecast.observability.forecasting.service;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class ForecastCurveMetricsPublisher {

    private double trafficActual;
    private double trafficForecast;
    private double trafficLowerBound;
    private double trafficUpperBound;

    private double latencyActual;
    private double latencyForecast;

    private double errorsActual;
    private double errorsForecast;

    private double saturationActual;
    private double saturationForecast;

    public ForecastCurveMetricsPublisher(MeterRegistry registry) {

        Gauge.builder("pulsecast_traffic_actual", this,
                ForecastCurveMetricsPublisher::getTrafficActual).register(registry);

        Gauge.builder("pulsecast_traffic_forecast", this,
                ForecastCurveMetricsPublisher::getTrafficForecast).register(registry);

        Gauge.builder("pulsecast_traffic_lower_bound", this,
                ForecastCurveMetricsPublisher::getTrafficLowerBound).register(registry);

        Gauge.builder("pulsecast_traffic_upper_bound", this,
                ForecastCurveMetricsPublisher::getTrafficUpperBound).register(registry);

        Gauge.builder("pulsecast_latency_actual", this,
                ForecastCurveMetricsPublisher::getLatencyActual).register(registry);

        Gauge.builder("pulsecast_latency_forecast", this,
                ForecastCurveMetricsPublisher::getLatencyForecast).register(registry);

        Gauge.builder("pulsecast_errors_actual", this,
                ForecastCurveMetricsPublisher::getErrorsActual).register(registry);

        Gauge.builder("pulsecast_errors_forecast", this,
                ForecastCurveMetricsPublisher::getErrorsForecast).register(registry);

        Gauge.builder("pulsecast_saturation_actual", this,
                ForecastCurveMetricsPublisher::getSaturationActual).register(registry);

        Gauge.builder("pulsecast_saturation_forecast", this,
                ForecastCurveMetricsPublisher::getSaturationForecast).register(registry);
    }

    public void updateTraffic(
            double actual,
            double forecast,
            double lowerBound,
            double upperBound
    ) {
        this.trafficActual = actual;
        this.trafficForecast = forecast;
        this.trafficLowerBound = lowerBound;
        this.trafficUpperBound = upperBound;
    }

    public void updateLatency(double actual, double forecast) {
        this.latencyActual = actual;
        this.latencyForecast = forecast;
    }

    public void updateErrors(double actual, double forecast) {
        this.errorsActual = actual;
        this.errorsForecast = forecast;
    }

    public void updateSaturation(double actual, double forecast) {
        this.saturationActual = actual;
        this.saturationForecast = forecast;
    }

    public double getTrafficActual() {
        return trafficActual;
    }

    public double getTrafficForecast() {
        return trafficForecast;
    }

    public double getTrafficLowerBound() {
        return trafficLowerBound;
    }

    public double getTrafficUpperBound() {
        return trafficUpperBound;
    }

    public double getLatencyActual() {
        return latencyActual;
    }

    public double getLatencyForecast() {
        return latencyForecast;
    }

    public double getErrorsActual() {
        return errorsActual;
    }

    public double getErrorsForecast() {
        return errorsForecast;
    }

    public double getSaturationActual() {
        return saturationActual;
    }

    public double getSaturationForecast() {
        return saturationForecast;
    }
}