package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceChecker;
import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceReport;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.forecasting.SarimaNextDayForecaster;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import com.pulsecast.observability.forecasting.model.SarimaParameters;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;

import org.junit.jupiter.api.Test;

public class SeasonalityComplianceCheckerTest {

    @Test
    void checkCurrentMetricsSeasonalityCompliance() {

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

        double[] expectedTraffic =
                forecastResult.getForecastValues();


        SeasonalityComplianceChecker checker =
                new SeasonalityComplianceChecker();

        SeasonalityComplianceReport report =
                checker.checkCompliance(
                        expectedTraffic,
                        currentTraffic
                );

        System.out.println(
                "\n===== SEASONALITY COMPLIANCE REPORT =====\n"
        );

        System.out.println(
                "Compliance Score = "
                        + report.getComplianceScore()
                        + "%"
        );

        System.out.println(
                "Compliant = "
                        + report.isCompliant()
        );
    }
}