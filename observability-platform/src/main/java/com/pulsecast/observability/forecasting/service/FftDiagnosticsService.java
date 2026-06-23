package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.FftAnalysisService;
import com.pulsecast.observability.forecasting.loader.AnalysisCsvLoader;
import com.pulsecast.observability.forecasting.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FftDiagnosticsService {

    private final AnalysisCsvLoader csvLoader;
    private final FftAnalysisService fftAnalysisService;

    public FftDiagnosticsService(
            AnalysisCsvLoader csvLoader,
            FftAnalysisService fftAnalysisService
    ) {
        this.csvLoader = csvLoader;
        this.fftAnalysisService = fftAnalysisService;
    }

    public List<FftSpectrumPoint> spectrum(
            AnalysisDomain domain,
            AnalysisMetric metric
    ) {
        double[] values =
                loadMetricValues(domain, metric);

        return fftAnalysisService.calculateSpectrum(values);
    }

    public SeasonalityStrengthResponse seasonalityStrength(
            AnalysisDomain domain,
            AnalysisMetric metric
    ) {
        double[] values =
                loadMetricValues(domain, metric);

        return fftAnalysisService.calculateSeasonalityStrength(
                domain.name(),
                metric.getColumnName(),
                values
        );
    }

    public List<DatasetComparisonResponse> compare(
            AnalysisMetric metric
    ) {
        List<DatasetComparisonResponse> result =
                new ArrayList<>();

        for (AnalysisDomain domain : AnalysisDomain.values()) {

            SeasonalityStrengthResponse strength =
                    seasonalityStrength(domain, metric);

            result.add(
                    new DatasetComparisonResponse(
                            domain.name(),
                            metric.getColumnName(),
                            strength.dailyStrengthPercent,
                            strength.weeklyStrengthPercent,
                            strength.seasonalVarianceExplainedPercent,
                            strength.nonPeriodicPowerPercent,
                            interpret(strength)
                    )
            );
        }

        return result;
    }

    private String interpret(
            SeasonalityStrengthResponse strength
    ) {
        if (strength.dailyStrengthPercent >= 50.0) {
            return "Strong daily seasonality. Seasonal forecasting models should work well.";
        }

        if (strength.seasonalVarianceExplainedPercent >= 40.0) {
            return "Moderate seasonality. Model may work, but errors can appear during peaks.";
        }

        if (strength.nonPeriodicPowerPercent >= 70.0) {
            return "Weak fixed-period seasonality. Forecasting will be harder and less stable.";
        }

        return "Mixed seasonal and non-periodic behavior.";
    }

    private double[] loadMetricValues(
            AnalysisDomain domain,
            AnalysisMetric metric
    ) {
        List<AnalysisCsvRow> rows =
                csvLoader.load(domain.getTrainingPath());

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