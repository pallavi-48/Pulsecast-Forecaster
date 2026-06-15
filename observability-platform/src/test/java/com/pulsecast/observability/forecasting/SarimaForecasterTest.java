package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.PacfCalculator;
import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.SarimaForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;


import org.junit.jupiter.api.Test;

public class SarimaForecasterTest {

    @Test
    void forecastUsingAR1() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        traffic,
                        parameters
                );

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(
                        traffic
                );

        PacfCalculator pacfCalculator =
                new PacfCalculator();

        double[] pacf =
                pacfCalculator.calculate(
                        differenced,
                        5
                );

        double phi =
                pacf[1];

        System.out.println(
                "\nAR(1) Coefficient = "
                        + phi
        );

        SarimaForecaster forecaster =
                new SarimaForecaster(phi);

        ForecastResult result =
                forecaster.forecast(
                        model,
                        traffic,
                        10
                );

        System.out.println(
                "\n===== AR FORECAST =====\n"
        );

        double[] forecast =
                result.getForecastValues();

        for (int i = 0; i < forecast.length; i++) {

            System.out.printf(
                    "Step %d = %.2f%n",
                    i + 1,
                    forecast[i]
            );
        }
    }
}