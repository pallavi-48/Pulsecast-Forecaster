package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceChecker;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceReport;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.evaluation.AccuracyReport;
import com.pulsecast.observability.forecasting.evaluation.AnomalyDetector;
import com.pulsecast.observability.forecasting.evaluation.AnomalyPoint;
import com.pulsecast.observability.forecasting.evaluation.ForecastAccuracyEvaluator;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.service.TrafficInsightEngine;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;

import org.junit.jupiter.api.Test;

import java.util.List;

public class ForecastSystemDemo {

    @Test
    void runCompleteForecastingSystem() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historicalSeries =
                loader.loadTwoMonthDataset();

        TimeSeries currentSeries =
                loader.loadCurrent();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] historicalTraffic =
                extractor.extract(historicalSeries);

        double[] currentTraffic =
                extractor.extract(currentSeries);

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        historicalTraffic,
                        parameters
                );

        SarimaNextDayForecaster forecaster =
                new SarimaNextDayForecaster();

        ForecastResult forecastResult =
                forecaster.forecastNextDay(
                        model,
                        historicalTraffic
                );

        double[] predictedTraffic =
                forecastResult.getForecastValues();

        ForecastAccuracyEvaluator accuracyEvaluator =
                new ForecastAccuracyEvaluator();

        AccuracyReport accuracyReport =
                accuracyEvaluator.evaluate(
                        currentTraffic,
                        predictedTraffic
                );

        SeasonalityComplianceChecker complianceChecker =
                new SeasonalityComplianceChecker();

        SeasonalityComplianceReport complianceReport =
                complianceChecker.checkCompliance(
                        predictedTraffic,
                        currentTraffic
                );

        AnomalyDetector anomalyDetector =
                new AnomalyDetector();

        List<AnomalyPoint> anomalies =
                anomalyDetector.detect(
                        currentTraffic,
                        predictedTraffic
                );

        System.out.println(
                "\n===== COMPLETE FORECAST SYSTEM DEMO =====\n"
        );

        System.out.println(
                "SARIMA Parameters:"
        );

        System.out.println(
                "p = " + parameters.getP()
                        + ", d = " + parameters.getD()
                        + ", q = " + parameters.getQ()
        );

        System.out.println(
                "P = " + parameters.getSeasonalP()
                        + ", D = " + parameters.getSeasonalD()
                        + ", Q = " + parameters.getSeasonalQ()
                        + ", s = " + parameters.getSeasonLength()
        );

        System.out.println();

        System.out.println(
                "Model:"
        );

        System.out.println(
                "Mean Traffic = "
                        + model.getMeanTraffic()
        );

        System.out.println(
                "AR Coefficient = "
                        + model.getArCoefficient()
        );

        System.out.println(
                "Seasonal Coefficient = "
                        + model.getSeasonalCoefficient()
        );
        System.out.println(
                "MA Coefficient = "
                        + model.getMaCoefficient()
        );

        System.out.println(
                "Seasonal MA Coefficient = "
                        + model.getSeasonalMaCoefficient()
        );

        System.out.println(
                "Residual Std Dev = "
                        + model.getResidualStdDev()
        );

        System.out.println();

        System.out.println(
                "Forecast Accuracy:"
        );

        System.out.println(
                "MAE = "
                        + accuracyReport.getMae()
        );

        System.out.println(
                "RMSE = "
                        + accuracyReport.getRmse()
        );

        TrafficInsightEngine insightEngine =
                new TrafficInsightEngine();

        insightEngine.generateInsights(
                predictedTraffic,
                complianceReport,
                anomalies
        );
    }
}