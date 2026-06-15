package com.pulsecast.observability.forecasting.domain;

import java.time.LocalDateTime;
import java.util.List;

public class TimeSeries {

    private final List<TimePoint> points;

    public TimeSeries(List<TimePoint> points) {
        this.points = points;
    }

    public List<TimePoint> getPoints() {
        return points;
    }

    public int size() {
        return points.size();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public LocalDateTime start() {
        return points.get(0).getTimestamp();
    }

    public LocalDateTime end() {
        return points.get(points.size() - 1).getTimestamp();
    }

    public double[] getTrafficSeries() {
        return points.stream()
                .mapToDouble(TimePoint::getRequestsPerSecond)
                .toArray();
    }

    public double[] getLatencySeries() {
        return points.stream()
                .mapToDouble(TimePoint::getResponseTimeMs)
                .toArray();
    }

    public double[] getErrorSeries() {
        return points.stream()
                .mapToDouble(TimePoint::getErrorRatePercent)
                .toArray();
    }

    public double[] getCpuSeries() {
        return points.stream()
                .mapToDouble(TimePoint::getCpuUsagePercent)
                .toArray();
    }
}