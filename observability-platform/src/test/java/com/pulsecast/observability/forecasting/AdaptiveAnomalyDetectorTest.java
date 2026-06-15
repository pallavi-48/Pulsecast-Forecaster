package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.evaluation.AnomalyDetector;
import com.pulsecast.observability.forecasting.evaluation.AnomalyPoint;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;

import org.junit.jupiter.api.Test;

import java.util.List;

public class AdaptiveAnomalyDetectorTest {

    @Test
    void detectCurrentMetricsAnomalies() {

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

        ForecastResult result =
                forecaster.forecastNextDay(
                        model,
                        historicalTraffic
                );

        double[] predicted =
                result.getForecastValues()
                        ;

        AnomalyDetector detector =
                new AnomalyDetector();

        List<AnomalyPoint> anomalies =
                detector.detect(
                        currentTraffic,
                        predicted
                );

        System.out.println(
                "\n===== ADAPTIVE ANOMALY REPORT =====\n"
        );

        System.out.println(
                "Total Anomalies = "
                        + anomalies.size()
        );

        System.out.println(
                "\n===== FIRST 30 ANOMALIES =====\n"
        );

        for (int i = 0;
             i < Math.min(30, anomalies.size());
             i++) {

            AnomalyPoint anomaly =
                    anomalies.get(i);

            System.out.printf(
                    "Point %d | Actual = %.2f | Predicted = %.2f | Error = %.2f | Error%% = %.2f | Severity = %s%n",
                    anomaly.getIndex(),
                    anomaly.getActual(),
                    anomaly.getPredicted(),
                    anomaly.getAbsoluteError(),
                    anomaly.getPercentageError(),
                    anomaly.getSeverity()
            );
        }
    }
}