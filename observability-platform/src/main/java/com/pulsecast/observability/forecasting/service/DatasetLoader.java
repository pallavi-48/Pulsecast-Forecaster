package com.pulsecast.observability.forecasting.service;

import com.opencsv.CSVReaderHeaderAware;
import com.pulsecast.observability.forecasting.model.DatasetRow;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DatasetLoader {

    @Value("${dataset.path}")
    private Resource csvResource;

    private final List<DatasetRow> rows = new ArrayList<>();

    @PostConstruct
    public void load() throws Exception {
        try (CSVReaderHeaderAware reader =
                     new CSVReaderHeaderAware(
                             new InputStreamReader(csvResource.getInputStream())
                     )) {

            Map<String, String> line;

            while ((line = reader.readMap()) != null) {
                DatasetRow row = new DatasetRow();

                row.timestamp = line.get("timestamp");
                row.cpuUsagePercent = parseDouble(line, "cpu_usage_percent");
                row.memoryUsagePercent = parseDouble(line, "memory_usage_percent");
                row.diskIo = parseDouble(line, "disk_io_percent");
                row.networkLatencyMs = parseDouble(line, "network_latency_ms");
                row.activeThreads = parseInt(line, "active_threads");
                row.databaseConnections = parseInt(line, "database_connections");
                row.requestsPerSecond = parseDouble(line, "requests_per_second");
                row.responseTimeMs = parseDouble(line, "response_time_ms");
                row.errorRatePercent = parseDouble(line, "error_rate_percent");
                row.activeUsers = parseInt(line, "active_users");

                rows.add(row);
            }
        }

        System.out.printf("Dataset loaded: %d rows%n", rows.size());
    }

    public List<DatasetRow> getRows() {
        return rows;
    }

    private double parseDouble(Map<String, String> map, String key) {
        String value = map.get(key);
        return value == null || value.isBlank()
                ? 0.0
                : Double.parseDouble(value);
    }

    private int parseInt(Map<String, String> map, String key) {
        String value = map.get(key);
        return value == null || value.isBlank()
                ? 0
                : (int) Double.parseDouble(value);
    }
}