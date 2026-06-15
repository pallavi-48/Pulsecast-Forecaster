package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceChecker;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceReport;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.evaluation.*;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForecastPipelineService {

    public ForecastPipelineResult runPipeline() {

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

        ForecastAccuracyEvaluator evaluator =
                new ForecastAccuracyEvaluator();

        AccuracyReport accuracy =
                evaluator.evaluate(
                        currentTraffic,
                        predictedTraffic
                );

        SeasonalityComplianceChecker complianceChecker =
                new SeasonalityComplianceChecker();

        SeasonalityComplianceReport compliance =
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

        ForecastPipelineResult result =
                new ForecastPipelineResult();

        result.mae = accuracy.getMae();
        result.rmse = accuracy.getRmse();
        result.complianceScore = compliance.getComplianceScore();
        result.compliant = compliance.isCompliant();
        result.anomalyCount = anomalies.size();

        result.predictedTraffic = predictedTraffic;
        result.actualTraffic = currentTraffic;
        result.anomalies = anomalies;

        fillForecastSummary(result);

        return result;
    }

    private void fillForecastSummary(
            ForecastPipelineResult result
    ) {

        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double value : result.predictedTraffic) {

            sum += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        result.expectedAverage =
                sum / result.predictedTraffic.length;

        result.expectedMin = min;
        result.expectedMax = max;
    }
}