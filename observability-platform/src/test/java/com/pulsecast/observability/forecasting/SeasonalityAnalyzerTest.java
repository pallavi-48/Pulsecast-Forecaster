package com.pulsecast.observability.forecasting;

import com.pulsecast.observability.forecasting.analysis.SeasonalityAnalyzer;
import com.pulsecast.observability.forecasting.analysis.SeasonalityPeak;
import com.pulsecast.observability.forecasting.analysis.SeasonalityReport;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.TrafficSeriesExtractor;
import com.pulsecast.observability.forecasting.preprocessing.DifferencingTransformer;

import org.junit.jupiter.api.Test;

public class SeasonalityAnalyzerTest {

    @Test
    void analyzeSeasonality() {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        TrafficSeriesExtractor extractor =
                new TrafficSeriesExtractor();

        double[] traffic =
                extractor.extract(series);

        DifferencingTransformer transformer =
                new DifferencingTransformer();

        double[] differenced =
                transformer.difference(
                        traffic
                );

        SeasonalityAnalyzer analyzer =
                new SeasonalityAnalyzer();

        SeasonalityReport report =
                analyzer.analyze(
                        differenced,
                        1000
                );

        System.out.println(
                "\n===== TOP SEASONAL CANDIDATES =====\n"
        );

        for (
                SeasonalityPeak peak :
                report.getTopPeaks()
        ) {

            System.out.printf(
                    "Lag %d = %.4f%n",
                    peak.getLag(),
                    peak.getCorrelation()
            );
        }
    }
}