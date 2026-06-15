package com.pulsecast.observability.forecasting.evaluation;

public class ForecastAccuracyEvaluator {

    public AccuracyReport evaluate(
            double[] actual,
            double[] predicted
    ) {

        double mae = calculateMae(
                actual,
                predicted
        );

        double rmse = calculateRmse(
                actual,
                predicted
        );

        return new AccuracyReport(
                mae,
                rmse
        );
    }

    private double calculateMae(
            double[] actual,
            double[] predicted
    ) {

        double sum = 0;

        for (int i = 0; i < actual.length; i++) {

            sum += Math.abs(
                    actual[i]
                            - predicted[i]
            );
        }

        return sum / actual.length;
    }

    private double calculateRmse(
            double[] actual,
            double[] predicted
    ) {

        double sum = 0;

        for (int i = 0; i < actual.length; i++) {

            double error =
                    actual[i]
                            - predicted[i];

            sum += error * error;
        }

        return Math.sqrt(
                sum / actual.length
        );
    }
}