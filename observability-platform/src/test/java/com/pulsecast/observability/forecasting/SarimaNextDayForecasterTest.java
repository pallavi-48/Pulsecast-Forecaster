package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;

import org.junit.jupiter.api.Test;

public class SarimaNextDayForecasterTest {

    @Test
    void forecastNextDayTraffic() {

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

        SarimaNextDayForecaster forecaster =
                new SarimaNextDayForecaster();

        ForecastResult result =
                forecaster.forecastNextDay(
                        model,
                        traffic
                );

        System.out.println(
                "\n===== NEXT DAY FORECAST =====\n"
        );

        double[] forecastValues =
                result.getForecastValues();

        double[] lowerBounds =
                result.getLowerBounds();

        double[] upperBounds =
                result.getUpperBounds();

        for (int i = 0;
             i < forecastValues.length;
             i++) {

            System.out.printf(
                    "Point %d = %.2f [%.2f, %.2f]%n",
                    i + 1,
                    forecastValues[i],
                    lowerBounds[i],
                    upperBounds[i]
            );
        }
    }
}