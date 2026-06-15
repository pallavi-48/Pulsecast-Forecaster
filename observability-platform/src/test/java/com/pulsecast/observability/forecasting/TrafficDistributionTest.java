package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class TrafficDistributionTest {

    @Test
    void analyzeTrafficDistribution() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        int horizon = 100;

        double[] actual =
                Arrays.copyOfRange(
                        traffic,
                        traffic.length - horizon,
                        traffic.length
                );

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;

        for (double value : actual) {

            min = Math.min(min, value);
            max = Math.max(max, value);
            sum += value;
        }

        double mean = sum / actual.length;

        System.out.println(
                "\n===== LAST 100 POINTS =====\n"
        );

        System.out.println(
                "Mean = " + mean
        );

        System.out.println(
                "Min = " + min
        );

        System.out.println(
                "Max = " + max
        );
    }
}