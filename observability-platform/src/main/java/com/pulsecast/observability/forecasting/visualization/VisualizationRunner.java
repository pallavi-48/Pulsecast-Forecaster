package com.pulsecast.observability.forecasting.visualization;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;

public class VisualizationRunner {

    public static void main(String[] args) {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        System.out.println("=== TRAFFIC VISUALIZATION ===");

        for (int i = 0; i < 100; i++) {

            int stars =
                    (int) (traffic[i] / 2);

            System.out.printf(
                    "%3d | %6.2f | %s%n",
                    i,
                    traffic[i],
                    "*".repeat(Math.max(1, stars))
            );
        }
    }
}