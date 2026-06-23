package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.TimeSeriesAnalysisService;
import com.pulsecast.observability.forecasting.loader.AnalysisCsvLoader;
import com.pulsecast.observability.forecasting.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForecastAnalysisService {

    private final AnalysisCsvLoader csvLoader;
    private final TimeSeriesAnalysisService analysisService;
    private final AnalysisForecastService forecastService;

    public ForecastAnalysisService(
            AnalysisCsvLoader csvLoader,
            TimeSeriesAnalysisService analysisService,
            AnalysisForecastService forecastService
    ) {
        this.csvLoader =
                csvLoader;

        this.analysisService =
                analysisService;

        this.forecastService =
                forecastService;
    }

    public List<GraphPoint> trainingTrend(
            AnalysisDomain domain,
            AnalysisMetric metric,
            int movingAverageWindow
    ) {
        List<AnalysisCsvRow> rows =
                csvLoader.load(domain.getTrainingPath());

        double[] values =
                extractMetric(rows, metric);

        double[] trend =
                analysisService.movingAverage(
                        values,
                        movingAverageWindow
                );

        List<GraphPoint> result =
                new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {
            result.add(
                    new GraphPoint(
                            rows.get(i).getTimestamp().toString(),
                            values[i],
                            trend[i],
                            0.0,
                            0.0
                    )
            );
        }

        return result;
    }

    public List<GraphPoint> forecastComparison(
            AnalysisDomain domain,
            TestType testType,
            AnalysisMetric metric
    ) {
        List<AnalysisCsvRow> trainingRows =
                csvLoader.load(domain.getTrainingPath());

        List<AnalysisCsvRow> testRows =
                csvLoader.load(domain.getTestPath(testType));

        double[] trainingValues =
                extractMetric(trainingRows, metric);

        double[] actual =
                extractMetric(testRows, metric);

        double[] forecast =
                forecastService.forecast(
                        trainingValues,
                        actual.length
                );

        List<GraphPoint> result =
                new ArrayList<>();

        for (int i = 0; i < actual.length; i++) {
            result.add(
                    new GraphPoint(
                            testRows.get(i).getTimestamp().toString(),
                            actual[i],
                            0.0,
                            forecast[i],
                            0.0
                    )
            );
        }

        return result;
    }

    public List<GraphPoint> residuals(
            AnalysisDomain domain,
            TestType testType,
            AnalysisMetric metric
    ) {
        List<GraphPoint> comparison =
                forecastComparison(
                        domain,
                        testType,
                        metric
                );

        List<GraphPoint> result =
                new ArrayList<>();

        for (GraphPoint point : comparison) {

            double residual =
                    point.actual - point.forecast;

            result.add(
                    new GraphPoint(
                            point.timestamp,
                            point.actual,
                            0.0,
                            point.forecast,
                            residual
                    )
            );
        }

        return result;
    }

    public List<FftPoint> fftSpectrum(
            AnalysisDomain domain,
            AnalysisMetric metric
    ) {
        List<AnalysisCsvRow> rows =
                csvLoader.load(domain.getTrainingPath());

        double[] values =
                extractMetric(rows, metric);

        return analysisService.fftSpectrum(values);
    }

    private double[] extractMetric(
            List<AnalysisCsvRow> rows,
            AnalysisMetric metric
    ) {
        double[] values =
                new double[rows.size()];

        for (int i = 0; i < rows.size(); i++) {
            values[i] =
                    rows.get(i).getValue(
                            metric.getColumnName()
                    );
        }

        return values;
    }
}