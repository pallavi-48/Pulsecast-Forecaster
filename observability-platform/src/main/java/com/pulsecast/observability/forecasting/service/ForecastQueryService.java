package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SeasonalityComplianceReport;
import com.pulsecast.observability.forecasting.evaluation.AnomalyPoint;

import java.util.List;

public class ForecastQueryService {

    public String summarizeForecast(
            double[] forecast
    ) {

        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double sum = 0;

        for (double value : forecast) {

            min = Math.min(min, value);
            max = Math.max(max, value);
            sum += value;
        }

        double average =
                sum / forecast.length;

        return String.format(
                """
                Forecast Summary
                
                Average Expected Traffic = %.2f
                Minimum Expected Traffic = %.2f
                Maximum Expected Traffic = %.2f
                """,
                average,
                min,
                max
        );
    }

    public String summarizeCompliance(
            SeasonalityComplianceReport report
    ) {

        return String.format(
                """
                Seasonality Compliance
                
                Score = %.2f%%
                Compliant = %s
                """,
                report.getComplianceScore(),
                report.isCompliant()
        );
    }

    public String summarizeAnomalies(
            List<AnomalyPoint> anomalies
    ) {

        return String.format(
                """
                Anomaly Summary
                
                Total Anomalies = %d
                """,
                anomalies.size()
        );
    }
}