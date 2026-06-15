package com.pulsecast.observability.forecasting.analysis;

import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;

public class AnalysisRunner {

    public static void main(String[] args) {

        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries series =
                loader.loadTwoMonthDataset();

        DatasetAnalysisService analysis =
                new DatasetAnalysisService();

        MetricStatistics traffic =
                analysis.analyzeTraffic(series);

        MetricStatistics latency =
                analysis.analyzeLatency(series);

        MetricStatistics errors =
                analysis.analyzeErrors(series);

        MetricStatistics cpu =
                analysis.analyzeCpu(series);

        System.out.println("\n========== TRAFFIC ==========");
        printStats(traffic);

        System.out.println("\n========== LATENCY ==========");
        printStats(latency);

        System.out.println("\n========== ERRORS ==========");
        printStats(errors);

        System.out.println("\n========== CPU ==========");
        printStats(cpu);
    }

    private static void printStats(
            MetricStatistics stats
    ) {

        System.out.println(
                "Average = " + stats.mean()
        );

        System.out.println(
                "Minimum = " + stats.min()
        );

        System.out.println(
                "Maximum = " + stats.max()
        );
    }
}