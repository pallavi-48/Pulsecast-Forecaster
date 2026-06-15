package com.pulsecast.observability.forecasting.domain;

import java.time.LocalDateTime;

public class TimePoint {

    private final LocalDateTime timestamp;
    private final double requestsPerSecond;
    private final double responseTimeMs;
    private final double errorRatePercent;
    private final double cpuUsagePercent;
    private final double memoryUsagePercent;
    private final double activeThreads;
    private final double activeUsers;
    private final double databaseConnections;
    private final double networkLatencyMs;
    private final double diskIoPercent;

    public TimePoint(
            LocalDateTime timestamp,
            double requestsPerSecond,
            double responseTimeMs,
            double errorRatePercent,
            double cpuUsagePercent,
            double memoryUsagePercent,
            double activeThreads,
            double activeUsers,
            double databaseConnections,
            double networkLatencyMs,
            double diskIoPercent
    ) {
        this.timestamp = timestamp;
        this.requestsPerSecond = requestsPerSecond;
        this.responseTimeMs = responseTimeMs;
        this.errorRatePercent = errorRatePercent;
        this.cpuUsagePercent = cpuUsagePercent;
        this.memoryUsagePercent = memoryUsagePercent;
        this.activeThreads = activeThreads;
        this.activeUsers = activeUsers;
        this.databaseConnections = databaseConnections;
        this.networkLatencyMs = networkLatencyMs;
        this.diskIoPercent = diskIoPercent;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getRequestsPerSecond() {
        return requestsPerSecond;
    }

    public double getResponseTimeMs() {
        return responseTimeMs;
    }

    public double getErrorRatePercent() {
        return errorRatePercent;
    }

    public double getCpuUsagePercent() {
        return cpuUsagePercent;
    }

    public double getMemoryUsagePercent() {
        return memoryUsagePercent;
    }

    public double getActiveThreads() {
        return activeThreads;
    }

    public double getActiveUsers() {
        return activeUsers;
    }

    public double getDatabaseConnections() {
        return databaseConnections;
    }

    public double getNetworkLatencyMs() {
        return networkLatencyMs;
    }

    public double getDiskIoPercent() {
        return diskIoPercent;
    }

    public double getTraffic() {
        return requestsPerSecond;
    }

    public double getResponseTime() {
        return responseTimeMs;
    }

    public double getErrors() {
        return errorRatePercent;
    }

    public double getCpu() {
        return cpuUsagePercent;
    }
}