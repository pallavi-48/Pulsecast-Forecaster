package com.pulsecast.observability.forecasting.optimization;

public class CoefficientOptimizer {

    public OptimizationResult optimize(
            double[] values,
            double mean,
            int seasonLength
    ) {
        double bestSse = Double.MAX_VALUE;

        double bestAr = 0.0;
        double bestMa = 0.0;
        double bestSeasonal = 0.0;
        double bestSeasonalMa = 0.0;

        for (double ar = -0.9; ar <= 0.9; ar += 0.1) {
            for (double ma = -0.9; ma <= 0.9; ma += 0.1) {
                for (double seasonal = -0.9; seasonal <= 0.9; seasonal += 0.1) {
                    for (double seasonalMa = -0.9; seasonalMa <= 0.9; seasonalMa += 0.1) {

                        double sse =
                                calculateSse(
                                        values,
                                        mean,
                                        ar,
                                        ma,
                                        seasonal,
                                        seasonalMa,
                                        seasonLength
                                );

                        if (sse < bestSse) {
                            bestSse = sse;
                            bestAr = ar;
                            bestMa = ma;
                            bestSeasonal = seasonal;
                            bestSeasonalMa = seasonalMa;
                        }
                    }
                }
            }
        }

        return new OptimizationResult(
                bestAr,
                bestMa,
                bestSeasonal,
                bestSeasonalMa,
                bestSse
        );
    }

    private double calculateSse(
            double[] values,
            double mean,
            double ar,
            double ma,
            double seasonal,
            double seasonalMa,
            int seasonLength
    ) {
        double[] residuals = new double[values.length];

        double sse = 0.0;

        for (int i = seasonLength; i < values.length; i++) {

            double previousValue = values[i - 1];
            double seasonalValue = values[i - seasonLength];

            double previousResidual = residuals[i - 1];
            double seasonalResidual = residuals[i - seasonLength];

            double predicted =
                    mean
                            + ar * (previousValue - mean)
                            + seasonal * (seasonalValue - mean)
                            + ma * previousResidual
                            + seasonalMa * seasonalResidual;

            double residual =
                    values[i] - predicted;

            residuals[i] = residual;

            sse += residual * residual;
        }

        return sse;
    }
}