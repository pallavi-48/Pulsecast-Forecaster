package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.PacfCalculator;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;

import org.junit.jupiter.api.Test;

public class PacfRealDatasetTest {

    @Test
    void testDatasetPacf() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(traffic);

        PacfCalculator pacfCalculator =
                new PacfCalculator();

        double[] pacf =
                pacfCalculator.calculate(
                        differenced,
                        20
                );

        System.out.println(
                "\nPACF (Differenced Traffic)\n"
        );

        for (int lag = 1;
             lag < pacf.length;
             lag++) {

            System.out.printf(
                    "Lag %d = %.4f%n",
                    lag,
                    pacf[lag]
            );
        }
    }
}