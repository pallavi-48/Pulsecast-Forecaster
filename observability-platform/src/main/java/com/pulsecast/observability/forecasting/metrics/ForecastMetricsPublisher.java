package com.pulsecast.observability.forecasting.metrics;

import com.pulsecast.observability.forecasting.service.ForecastPipelineResult;
import com.pulsecast.observability.forecasting.service.ForecastPipelineService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class ForecastMetricsPublisher {

    private final ForecastPipelineService service;

    private final AtomicReference<Double> mae =
            new AtomicReference<>(0.0);

    private final AtomicReference<Double> rmse =
            new AtomicReference<>(0.0);

    private final AtomicReference<Double> complianceScore =
            new AtomicReference<>(0.0);

    private final AtomicReference<Double> anomalyCount =
            new AtomicReference<>(0.0);

    private final AtomicReference<Double> expectedAverage =
            new AtomicReference<>(0.0);

    private final AtomicReference<Double> expectedMin =
            new AtomicReference<>(0.0);

    private final AtomicReference<Double> expectedMax =
            new AtomicReference<>(0.0);

    public ForecastMetricsPublisher(
            ForecastPipelineService service,
            MeterRegistry registry
    ) {
        this.service = service;

        Gauge.builder("forecast_mae", mae, AtomicReference::get)
                .register(registry);

        Gauge.builder("forecast_rmse", rmse, AtomicReference::get)
                .register(registry);

        Gauge.builder("seasonality_compliance_score", complianceScore, AtomicReference::get)
                .register(registry);

        Gauge.builder("forecast_anomaly_count", anomalyCount, AtomicReference::get)
                .register(registry);

        Gauge.builder("forecast_expected_average", expectedAverage, AtomicReference::get)
                .register(registry);

        Gauge.builder("forecast_expected_min", expectedMin, AtomicReference::get)
                .register(registry);

        Gauge.builder("forecast_expected_max", expectedMax, AtomicReference::get)
                .register(registry);
    }

    @PostConstruct
    @Scheduled(fixedRate = 30000)
    public void publishMetrics() {

        ForecastPipelineResult result =
                service.runPipeline();

        mae.set(result.mae);
        rmse.set(result.rmse);
        complianceScore.set(result.complianceScore);
        anomalyCount.set((double) result.anomalyCount);
        expectedAverage.set(result.expectedAverage);
        expectedMin.set(result.expectedMin);
        expectedMax.set(result.expectedMax);
    }
}