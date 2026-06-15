package com.pulsecast.observability.forecasting.model;

public class DatasetRow {
    public String  timestamp;
    public double  cpuUsagePercent;
    public double  memoryUsagePercent;
    public double  diskIo;
    public double  networkLatencyMs;
    public int     activeThreads;
    public int     databaseConnections;
    public double  requestsPerSecond;
    public double  responseTimeMs;
    public double  errorRatePercent;
    public int     activeUsers;
    public double  apiSuccessCount;
    public double  apiFailureCount;
    public double  trafficLoadScore;
    public double  latencyVolatilityIndex;
    public double  anomalyProbability;
    public double  seasonalComplianceScore;
    public double  peakHourIntensity;
    public double  stabilityIndex;
    public int     isAnomaly;
}