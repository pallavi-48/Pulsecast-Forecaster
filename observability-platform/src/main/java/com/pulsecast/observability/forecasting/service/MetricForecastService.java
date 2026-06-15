package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceChecker;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceReport;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.evaluation.AccuracyReport;
import com.pulsecast.observability.forecasting.evaluation.AnomalyDetector;
import com.pulsecast.observability.forecasting.evaluation.ForecastAccuracyEvaluator;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.springframework.stereotype.Service;

@Service
public class MetricForecastService {

    public MetricForecastResult forecastMetric(
            TimeSeries historicalSeries,
            TimeSeries currentSeries,
            MetricType metricType
    ) {

        MetricSeriesExtractor extractor =
                new MetricSeriesExtractor();

        double[] historical =
                extractor.extract(
                        historicalSeries,
                        metricType
                );

        double[] actual =
                extractor.extract(
                        currentSeries,
                        metricType
                );

        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        SarimaModel model =
                trainer.train(
                        historical,
                        parameters
                );

        SarimaNextDayForecaster forecaster =
                new SarimaNextDayForecaster();

        ForecastResult forecast =
                forecaster.forecastNextDay(
                        model,
                        historical
                );

        double[] predicted =
                forecast.getForecastValues();




        ForecastAccuracyEvaluator evaluator =
                new ForecastAccuracyEvaluator();

        AccuracyReport accuracy =
                evaluator.evaluate(
                        actual,
                        predicted
                );

        SeasonalityComplianceChecker complianceChecker =
                new SeasonalityComplianceChecker();

        SeasonalityComplianceReport compliance =
                complianceChecker.checkCompliance(
                        predicted,
                        actual
                );

        AnomalyDetector anomalyDetector =
                new AnomalyDetector();

        MetricForecastResult result =
                new MetricForecastResult();

        result.metricType =
                metricType;

        result.mae =
                accuracy.getMae();

        result.rmse =
                accuracy.getRmse();

        result.complianceScore =
                compliance.getComplianceScore();

        result.compliant =
                compliance.isCompliant();

        result.anomalyCount =
                anomalyDetector.detect(
                        actual,
                        predicted
                ).size();

        calculateForecastSummary(
                predicted,
                result
        );

        return result;
    }

    private void calculateForecastSummary(
            double[] forecast,
            MetricForecastResult result
    ) {

        double sum = 0;

        double min =
                Double.MAX_VALUE;

        double max =
                Double.MIN_VALUE;

        for (double value : forecast) {

            sum += value;

            min =
                    Math.min(
                            min,
                            value
                    );

            max =
                    Math.max(
                            max,
                            value
                    );
        }

        result.expectedAverage =
                sum / forecast.length;

        result.expectedMin =
                min;

        result.expectedMax =
                max;
    }
}