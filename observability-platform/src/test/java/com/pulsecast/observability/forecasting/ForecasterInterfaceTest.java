package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.Forecaster;
import com.pulsecast.observability.forecasting.forecasting.SarimaForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.junit.jupiter.api.Test;

public class ForecasterInterfaceTest {

    @Test
    void testForecasterInterface() {

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

        Forecaster forecaster =
                new SarimaForecaster(
                        model.getArCoefficient()
                );

        ForecastResult result =
                forecaster.forecast(
                        model,
                        traffic,
                        100
                );

        System.out.println(
                "\n===== FORECASTER INTERFACE TEST =====\n"
        );

        System.out.println(
                "Forecast Points = "
                        + result.getForecastValues().length
        );

        System.out.println(
                "First Forecast Value = "
                        + result.getForecastValues()[0]
        );
    }
}