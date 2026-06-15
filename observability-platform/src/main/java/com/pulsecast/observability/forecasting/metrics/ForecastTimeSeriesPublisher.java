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
public class ForecastTimeSeriesPublisher {

    private final ForecastPipelineService service;
    private final AtomicReference<Double>[] predicted;
    private final AtomicReference<Double>[] actual;

    public ForecastTimeSeriesPublisher(
            ForecastPipelineService service,
            MeterRegistry registry
    ) {
        this.service = service;

        int points = 288;

        predicted = new AtomicReference[points];
        actual = new AtomicReference[points];

        for (int i = 0; i < points; i++) {

            int pointNumber = i + 1;

            predicted[i] =
                    new AtomicReference<>(0.0);

            actual[i] =
                    new AtomicReference<>(0.0);

            Gauge.builder(
                            "forecast_predicted_traffic",
                            predicted[i],
                            AtomicReference::get
                    )
                    .tag("point", String.valueOf(pointNumber))
                    .register(registry);

            Gauge.builder(
                            "forecast_actual_traffic",
                            actual[i],
                            AtomicReference::get
                    )
                    .tag("point", String.valueOf(pointNumber))
                    .register(registry);
        }
    }

    @PostConstruct
    @Scheduled(fixedRate = 30000)
    public void publishTimeSeries() {

        ForecastPipelineResult result =
                service.runPipeline();

        for (int i = 0; i < 288; i++) {

            if (i < result.predictedTraffic.length) {
                predicted[i].set(result.predictedTraffic[i]);
            }

            if (i < result.actualTraffic.length) {
                actual[i].set(result.actualTraffic[i]);
            }
        }
    }
}