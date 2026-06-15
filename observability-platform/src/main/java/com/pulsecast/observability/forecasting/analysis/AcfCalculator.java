package com.pulsecast.observability.forecasting.analysis;

public class AcfCalculator {

    public double[] calculate(
            double[] values,
            int maxLag
    ) {

        double[] acf =
                new double[maxLag];

        double mean = mean(values);

        double denominator = 0;

        for (double value : values) {

            denominator +=
                    Math.pow(
                            value - mean,
                            2
                    );
        }

        for (int lag = 1; lag <= maxLag; lag++) {

            double numerator = 0;

            for (
                    int i = lag;
                    i < values.length;
                    i++
            ) {

                numerator +=
                        (values[i] - mean)
                                *
                                (values[i - lag] - mean);
            }

            acf[lag - 1] =
                    numerator / denominator;
        }

        return acf;
    }

    private double mean(
            double[] values
    ) {

        double sum = 0;

        for (double value : values) {

            sum += value;
        }

        return sum / values.length;
    }
}