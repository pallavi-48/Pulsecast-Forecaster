package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.model.DatasetRow;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DatasetReplayScheduler {

    private final DatasetLoader loader;
    private final ObservabilityMetrics metrics;
    private final AtomicInteger index = new AtomicInteger(0);

    public DatasetReplayScheduler(
            DatasetLoader loader,
            ObservabilityMetrics metrics
    ) {
        this.loader = loader;
        this.metrics = metrics;
    }

    @Scheduled(fixedDelayString = "${dataset.replay-interval-ms:5000}")
    public void replay() {
        List<DatasetRow> rows = loader.getRows();

        if (rows.isEmpty()) {
            return;
        }

        int i =
                index.getAndUpdate(
                        n -> (n + 1) % rows.size()
                );

        metrics.update(rows.get(i));

        if (i % 288 == 0) {
            System.out.printf(
                    "Replaying day %d (row %d)%n",
                    (i / 288) + 1,
                    i
            );
        }
    }
}