package com.pulsecast.observability.forecasting.analysis;

import java.util.Arrays;

public class StationarityAnalyzer {

    public void analyze(
            double[] original,
            double[] differenced
    ) {

        System.out.println("\n===== ORIGINAL SERIES =====");

        printStats(original);

        System.out.println("\n===== DIFFERENCED SERIES =====");

        printStats(differenced);
    }

    private void printStats(
            double[] values
    ) {

        double mean =
                Arrays.stream(values)
                        .average()
                        .orElse(0);

        double variance = 0;

        for (double value : values) {

            variance +=
                    Math.pow(
                            value - mean,
                            2
                    );
        }

        variance =
                variance / values.length;

        System.out.println(
                "Mean = " + mean
        );

        System.out.println(
                "Variance = " + variance
        );
    }
}