package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.PacfCalculator;
import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.evaluation.AccuracyReport;
import com.pulsecast.observability.forecasting.evaluation.ForecastAccuracyEvaluator;
import com.pulsecast.observability.forecasting.forecasting.ForecastGenerator;
import com.pulsecast.observability.forecasting.forecasting.SarimaForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class ForecastComparisonTest {

    @Test
    void compareForecastingMethods() {

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

        ForecastAccuracyEvaluator evaluator =
                new ForecastAccuracyEvaluator();

        ForecastGenerator baselineGenerator =
                new ForecastGenerator();

        ForecastResult movingAverageForecast =
                baselineGenerator.forecast(
                        model,
                        training,
                        horizon
                );

        AccuracyReport movingAverageReport =
                evaluator.evaluate(
                        actual,
                        movingAverageForecast.getForecastValues()
                );

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(
                        training
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

        SarimaForecaster arForecaster =
                new SarimaForecaster(phi);

        ForecastResult arForecast =
                arForecaster.forecast(
                        model,
                        training,
                        horizon
                );

        AccuracyReport arReport =
                evaluator.evaluate(
                        actual,
                        arForecast.getForecastValues()

                );

        System.out.println(
                "\n===== FORECAST COMPARISON =====\n"
        );

        System.out.println(
                "Moving Average Forecast"
        );
        System.out.println(
                "MAE = " + movingAverageReport.getMae()
        );
        System.out.println(
                "RMSE = " + movingAverageReport.getRmse()
        );

        System.out.println(
                "\nAR(1) Forecast"
        );
        System.out.println(
                "Phi = " + phi
        );
        System.out.println(
                "MAE = " + arReport.getMae()
        );
        System.out.println(
                "RMSE = " + arReport.getRmse()
        );
    }
}