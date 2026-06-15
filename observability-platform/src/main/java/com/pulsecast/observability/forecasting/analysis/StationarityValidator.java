package com.pulsecast.observability.forecasting.analysis;

import java.util.Arrays;

public class StationarityValidator {

    public StationarityReport validate(
            double[] values
    ) {

        if (values == null || values.length < 2) {

            throw new IllegalArgumentException(
                    "Series must contain at least 2 values"
            );
        }

        int midpoint = values.length / 2;

        double[] firstHalf =
                Arrays.copyOfRange(
                        values,
                        0,
                        midpoint
                );

        double[] secondHalf =
                Arrays.copyOfRange(
                        values,
                        midpoint,
                        values.length
                );

        double overallMean =
                mean(values);

        double firstMean =
                mean(firstHalf);

        double secondMean =
                mean(secondHalf);

        double overallVariance =
                variance(values);

        double firstVariance =
                variance(firstHalf);

        double secondVariance =
                variance(secondHalf);

        double meanDifference =
                Math.abs(
                        firstMean
                                - secondMean
                );

        double varianceDifferencePercent =
                Math.abs(
                        firstVariance
                                - secondVariance
                )
                        / overallVariance
                        * 100.0;

        boolean stationary =
                meanDifference < 0.5
                        &&
                        varianceDifferencePercent < 20.0;

        return new StationarityReport(
                overallMean,
                firstMean,
                secondMean,
                overallVariance,
                firstVariance,
                secondVariance,
                varianceDifferencePercent,
                stationary
        );
    }

    private double mean(
            double[] values
    ) {

        double sum = 0.0;

        for (double value : values) {

            sum += value;
        }

        return sum / values.length;
    }

    private double variance(
            double[] values
    ) {

        double mean =
                mean(values);

        double sum = 0.0;

        for (double value : values) {

            sum += Math.pow(
                    value - mean,
                    2
            );
        }

        return sum / values.length;
    }
}