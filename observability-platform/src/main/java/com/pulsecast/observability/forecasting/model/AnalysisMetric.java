package com.pulsecast.observability.forecasting.model;

public enum AnalysisMetric {

    REQUESTS_PER_SECOND("requests_per_second"),
    RESPONSE_TIME_MS("response_time_ms"),
    ERROR_RATE_PERCENT("error_rate_percent"),
    CPU_USAGE_PERCENT("cpu_usage_percent");

    private final String columnName;

    AnalysisMetric(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public static AnalysisMetric from(String value) {
        for (AnalysisMetric metric : values()) {
            if (metric.columnName.equalsIgnoreCase(value)
                    || metric.name().equalsIgnoreCase(value)) {
                return metric;
            }
        }

        throw new IllegalArgumentException("Unknown metric: " + value);
    }
}