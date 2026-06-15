package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.ForecastGenerator;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;

import org.junit.jupiter.api.Test;

public class ForecastGeneratorTest {

    @Test
    void generateForecast() {

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

        ForecastGenerator generator =
                new ForecastGenerator();

        ForecastResult result =
                generator.forecast(
                        model,
                        traffic,
                        10
                );

        System.out.println(
                "\n===== FORECAST =====\n"
        );

        double[] values =
                result.getForecastValues();

        for (int i = 0; i < values.length; i++) {

            System.out.printf(
                    "Step %d = %.2f%n",
                    i + 1,
                    values[i]
            );
        }
    }
}