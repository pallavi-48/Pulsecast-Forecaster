package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ArForecaster;
import com.pulsecast.observability.forecasting.model.ArModelTrainer;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import org.junit.jupiter.api.Test;

public class ForecastTest {

    @Test
    void forecastTraffic() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadHistorical();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(traffic);

        ArModelTrainer trainer =
                new ArModelTrainer();

        double phi =
                trainer.train(differenced);

        System.out.println(
                "AR(1) coefficient = "
                        + phi
        );

        ArForecaster forecaster =
                new ArForecaster();

        double[] forecast =
                forecaster.forecast(
                        phi,
                        differenced[differenced.length - 1],
                        20
                );

        for (int i = 0; i < forecast.length; i++) {

            System.out.println(
                    "Forecast "
                            + (i + 1)
                            + " = "
                            + forecast[i]
            );
        }
    }
}