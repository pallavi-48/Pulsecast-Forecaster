package com.pulsecast.observability.forecasting.loader;

import com.pulsecast.observability.forecasting.domain.TimePoint;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.model.DatasetFile;
import com.pulsecast.observability.forecasting.model.DatasetKind;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class ForecastDatasetLoader {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TimeSeries loadHistorical() {
        return loadDataset(DatasetFile.OBSERVABILITY_TRAINING);
    }

    public TimeSeries loadCurrent() {
        return loadDataset(DatasetFile.OBSERVABILITY_CURRENT);
    }

    public TimeSeries loadTwoMonthDataset() {
        return loadDataset(DatasetFile.OBSERVABILITY_TRAINING);
    }

    public TimeSeries loadCurrentMetricsDataset() {
        return loadDataset(DatasetFile.OBSERVABILITY_CURRENT);
    }

    public TimeSeries loadDataset(DatasetFile datasetFile) {
        return load(datasetFile.getPath(), datasetFile.getKind());
    }

    private TimeSeries load(String path, DatasetKind kind) {

        try {
            List<TimePoint> points = new ArrayList<>();

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(
                                    new ClassPathResource(path).getInputStream()
                            )
                    );

            String headerLine = reader.readLine();

            if (headerLine == null) {
                throw new RuntimeException("CSV file is empty: " + path);
            }

            String[] headers = headerLine.split(",");
            Map<String, Integer> headerMap = buildHeaderMap(headers);

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.isBlank()) {
                    continue;
                }

                String[] row = line.split(",");

                TimePoint point = mapRow(row, headerMap, kind);

                points.add(point);
            }

            return new TimeSeries(points);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to load dataset: " + path,
                    e
            );
        }
    }

    private Map<String, Integer> buildHeaderMap(String[] headers) {

        Map<String, Integer> map = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            map.put(headers[i].trim().toLowerCase(), i);
        }

        return map;
    }

    private TimePoint mapRow(
            String[] row,
            Map<String, Integer> headerMap,
            DatasetKind kind
    ) {

        LocalDateTime timestamp =
                parseTimestamp(
                        getString(row, headerMap, "timestamp")
                );

        return switch (kind) {

            case OBSERVABILITY -> new TimePoint(
                    timestamp,
                    getDoubleFlexible(row, headerMap, "requests_per_second"),
                    getDoubleFlexible(row, headerMap, "response_time_ms"),
                    getDoubleFlexible(row, headerMap, "error_rate_percent"),
                    getDoubleFlexible(row, headerMap, "cpu_usage_percent"),
                    getDoubleFlexible(row, headerMap, "memory_usage_percent"),
                    getDoubleFlexible(row, headerMap, "active_threads"),
                    getDoubleFlexible(row, headerMap, "active_users"),
                    getDoubleFlexible(row, headerMap, "database_connections"),
                    getDoubleFlexible(row, headerMap, "network_latency_ms"),
                    getDoubleFlexible(row, headerMap, "disk_io_percent")
            );

            case STOCK -> new TimePoint(
                    timestamp,

                    // TRAFFIC equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "volume",
                            "trade_volume",
                            "requests_per_second"
                    ),

                    // LATENCY equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "close_price",
                            "close",
                            "price",
                            "response_time_ms"
                    ),

                    // ERRORS equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "volatility_percent",
                            "volatility",
                            "error_rate_percent"
                    ),

                    // SATURATION equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "market_activity_percent",
                            "market_activity",
                            "cpu_usage_percent"
                    ),

                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0
            );

            case SMART_CITY_TRAFFIC -> new TimePoint(
                    timestamp,

                    // TRAFFIC equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "vehicle_count",
                            "traffic_volume",
                            "requests_per_second"
                    ),

                    // LATENCY equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "average_speed_kmph",
                            "avg_speed",
                            "response_time_ms"
                    ),

                    // ERRORS equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "incident_rate_percent",
                            "incident_rate",
                            "error_rate_percent"
                    ),

                    // SATURATION equivalent
                    getDoubleFlexible(
                            row,
                            headerMap,
                            "congestion_percent",
                            "congestion",
                            "cpu_usage_percent"
                    ),

                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0,
                    0.0
            );
        };
    }

    private LocalDateTime parseTimestamp(String value) {

        value = value.trim();

        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (Exception ignored) {
            return LocalDateTime.parse(value);
        }
    }

    private String getString(
            String[] row,
            Map<String, Integer> headerMap,
            String columnName
    ) {

        Integer index =
                headerMap.get(columnName.toLowerCase());

        if (index == null || index >= row.length) {
            throw new RuntimeException("Missing column: " + columnName);
        }

        return row[index].trim();
    }

    private double getDoubleFlexible(
            String[] row,
            Map<String, Integer> headerMap,
            String... possibleColumnNames
    ) {

        for (String columnName : possibleColumnNames) {

            Integer index =
                    headerMap.get(columnName.toLowerCase());

            if (index != null && index < row.length) {

                String value =
                        row[index].trim();

                if (value.isBlank()) {
                    return 0.0;
                }

                return Double.parseDouble(value);
            }
        }

        throw new RuntimeException(
                "Missing expected column. Tried: "
                        + Arrays.toString(possibleColumnNames)
        );
    }
}