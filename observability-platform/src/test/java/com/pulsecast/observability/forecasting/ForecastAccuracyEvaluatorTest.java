package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.evaluation.AccuracyReport;
import com.pulsecast.observability.forecasting.evaluation.ForecastAccuracyEvaluator;
import com.pulsecast.observability.forecasting.forecasting.ForecastGenerator;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ForecastAccuracyEvaluatorTest {

    @Test
    void evaluateForecast() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        int horizon = 100;

        double[] training =
                Arrays.copyOfRange(
                        traffic,
                        0,
                        traffic.length - horizon
                );

        double[] actual =
                Arrays.copyOfRange(
                        traffic,
                        traffic.length - horizon,
                        traffic.length
                );

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        training,
                        parameters
                );

        ForecastGenerator generator =
                new ForecastGenerator();

        ForecastResult forecast =
                generator.forecast(
                        model,
                        training,
                        horizon
                );

        ForecastAccuracyEvaluator evaluator =
                new ForecastAccuracyEvaluator();

        AccuracyReport report =
                evaluator.evaluate(
                        actual,
                        forecast.getForecastValues()
                );

        System.out.println(
                "\n===== FORECAST ACCURACY =====\n"
        );

        System.out.println(
                "MAE = "
                        + report.getMae()
        );

        System.out.println(
                "RMSE = "
                        + report.getRmse()
        );
    }
}