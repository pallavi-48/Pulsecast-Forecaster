package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import org.junit.jupiter.api.Test;

public class TrafficDifferencingTest {

    @Test
    void printTrafficDifferences() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        double[] traffic =
                series.getTrafficSeries();

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] diff =
                transformer.difference(
                        traffic
                );

        for (int i = 0; i < 20; i++) {

            System.out.println(
                    diff[i]
            );
        }
    }
}