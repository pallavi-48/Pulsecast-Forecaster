package com.pulsecast.observability.forecasting.evaluation;

import java.util.ArrayList;
import java.util.List;

public class AnomalyDetector {

    public List<AnomalyPoint> detect(
            double[] actual,
            double[] predicted
    ) {

        List<AnomalyPoint> anomalies =
                new ArrayList<>();

        int length =
                Math.min(
                        actual.length,
                        predicted.length
                );

        for (int i = 0; i < length; i++) {

            double absoluteError =
                    Math.abs(
                            actual[i] - predicted[i]
                    );

            double denominator =
                    Math.max(
                            Math.abs(predicted[i]),
                            1.0
                    );

            double percentageError =
                    absoluteError / denominator * 100.0;

            if (percentageError >= 50.0) {

                AnomalySeverity severity =
                        getSeverity(
                                percentageError
                        );

                anomalies.add(
                        new AnomalyPoint(
                                i + 1,
                                actual[i],
                                predicted[i],
                                absoluteError,
                                percentageError,
                                severity
                        )
                );
            }
        }

        return anomalies;
    }

    private AnomalySeverity getSeverity(
            double percentageError
    ) {

        if (percentageError >= 80.0) {
            return AnomalySeverity.HIGH;
        }

        if (percentageError >= 65.0) {
            return AnomalySeverity.MEDIUM;
        }

        return AnomalySeverity.LOW;
    }
}