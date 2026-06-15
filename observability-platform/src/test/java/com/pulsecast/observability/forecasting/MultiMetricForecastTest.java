package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.MetricType;
import com.pulsecast.observability.forecasting.service.MetricForecastResult;
import com.pulsecast.observability.forecasting.service.MetricForecastService;
import org.junit.jupiter.api.Test;

public class MultiMetricForecastTest {

    @Test
    void forecastAllGoldenMetrics() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historical =
                loader.loadTwoMonthDataset();

        TimeSeries current =
                loader.loadCurrent();

        MetricForecastService service =
                new MetricForecastService();

        for (MetricType metricType : MetricType.values()) {

            MetricForecastResult result =
                    service.forecastMetric(
                            historical,
                            current,
                            metricType
                    );

            System.out.println(
                    "\n===== " + metricType + " FORECAST =====\n"
            );

            System.out.println(
                    "MAE = " + result.mae
            );

            System.out.println(
                    "RMSE = " + result.rmse
            );

            System.out.println(
                    "Compliance Score = "
                            + result.complianceScore
                            + "%"
            );

            System.out.println(
                    "Compliant = "
                            + result.compliant
            );

            System.out.println(
                    "Anomalies = "
                            + result.anomalyCount
            );

            System.out.println(
                    "Expected Average = "
                            + result.expectedAverage
            );

            System.out.println(
                    "Expected Min = "
                            + result.expectedMin
            );

            System.out.println(
                    "Expected Max = "
                            + result.expectedMax
            );
        }
    }
}