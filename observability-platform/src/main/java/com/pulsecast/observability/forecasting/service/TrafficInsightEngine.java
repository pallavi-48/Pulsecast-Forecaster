package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceReport;
import com.pulsecast.observability.forecasting.evaluation.AnomalyPoint;

import java.util.List;

public class TrafficInsightEngine {

    public void generateInsights(
            double[] forecast,
            SeasonalityComplianceReport compliance,
            List<AnomalyPoint> anomalies
    ) {

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0.0;

        int peakIndex = 0;
        int lowIndex = 0;

        for (int i = 0; i < forecast.length; i++) {

            double value = forecast[i];

            sum += value;

            if (value > max) {
                max = value;
                peakIndex = i + 1;
            }

            if (value < min) {
                min = value;
                lowIndex = i + 1;
            }
        }

        double average =
                sum / forecast.length;

        System.out.println(
                "\n===== TRAFFIC INSIGHTS =====\n"
        );

        System.out.println(
                "Average Expected Traffic = "
                        + average
        );

        System.out.println(
                "Minimum Expected Traffic = "
                        + min
        );

        System.out.println(
                "Maximum Expected Traffic = "
                        + max
        );

        System.out.println(
                "Lowest Expected Interval = "
                        + lowIndex
        );

        System.out.println(
                "Peak Expected Interval = "
                        + peakIndex
        );

        System.out.println();

        if (compliance.isCompliant()) {

            System.out.println(
                    "Current traffic follows historical seasonality."
            );

        } else {

            System.out.println(
                    "Current traffic does NOT follow historical seasonality."
            );
        }

        System.out.println(
                "Seasonality Compliance Score = "
                        + compliance.getComplianceScore()
                        + "%"
        );

        System.out.println(
                "Total Anomalous Intervals = "
                        + anomalies.size()
        );

        if (anomalies.size() > 50) {

            System.out.println(
                    "Warning: Large number of anomalous intervals detected."
            );

        } else if (!anomalies.isEmpty()) {

            System.out.println(
                    "Some unusual traffic intervals were detected."
            );

        } else {

            System.out.println(
                    "No significant anomalies detected."
            );
        }
    }
}