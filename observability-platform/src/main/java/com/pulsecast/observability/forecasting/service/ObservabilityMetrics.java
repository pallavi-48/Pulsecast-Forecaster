package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.model.DatasetRow;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ObservabilityMetrics {

    private final AtomicReference<DatasetRow> currentRow =
            new AtomicReference<>(new DatasetRow());

    public ObservabilityMetrics(MeterRegistry registry) {

        Gauge.builder(
                        "pulsecast_requests_per_second",
                        currentRow,
                        row -> row.get().requestsPerSecond
                )
                .register(registry);

        Gauge.builder(
                        "pulsecast_response_time_ms",
                        currentRow,
                        row -> row.get().responseTimeMs
                )
                .register(registry);

        Gauge.builder(
                        "pulsecast_error_rate_percent",
                        currentRow,
                        row -> row.get().errorRatePercent
                )
                .register(registry);

        Gauge.builder(
                        "pulsecast_cpu_usage_percent",
                        currentRow,
                        row -> row.get().cpuUsagePercent
                )
                .register(registry);

        Gauge.builder(
                        "pulsecast_memory_usage_percent",
                        currentRow,
                        row -> row.get().memoryUsagePercent
                )
                .register(registry);

        Gauge.builder(
                        "pulsecast_disk_io_percent",
                        currentRow,
                        row -> row.get().diskIo
                )
                .register(registry);

        Gauge.builder(
                        "pulsecast_network_latency_ms",
                        currentRow,
                        row -> row.get().networkLatencyMs
                )
                .register(registry);
    }

    public void update(DatasetRow row) {
        currentRow.set(row);
    }
}