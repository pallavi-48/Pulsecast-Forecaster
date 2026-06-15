package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.AcfCalculator;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import org.junit.jupiter.api.Test;

public class AcfCalculatorTest {

    @Test
    void printAcfValues() {

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

        AcfCalculator calculator =
                new AcfCalculator();

        double[] acf =
                calculator.calculate(
                        diff,
                        20
                );

        for (int i = 0; i < acf.length; i++) {

            System.out.println(
                    "Lag "
                            + (i + 1)
                            + " = "
                            + acf[i]
            );
        }
    }
}