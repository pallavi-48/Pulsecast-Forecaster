package com.pulsecast.observability.forecasting.model;

import java.time.LocalDateTime;
import java.util.Map;

public class AnalysisCsvRow {

    private final LocalDateTime timestamp;
    private final Map<String, Double> values;

    public AnalysisCsvRow(
            LocalDateTime timestamp,
            Map<String, Double> values
    ) {
        this.timestamp = timestamp;
        this.values = values;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getValue(String columnName) {
        return values.getOrDefault(columnName, 0.0);
    }
}