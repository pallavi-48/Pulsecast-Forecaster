package com.pulsecast.observability.forecasting.analysis;

public class PacfCalculator {

    private final AcfCalculator acfCalculator;

    public PacfCalculator() {
        this.acfCalculator = new AcfCalculator();
    }

    public double[] calculate(double[] series, int maxLag) {

        if (series == null || series.length < 2) {
            throw new IllegalArgumentException(
                    "Series must contain at least 2 values"
            );
        }

        double[] acf =
                acfCalculator.calculate(series, maxLag);

        double[][] phi =
                new double[maxLag + 1][maxLag + 1];

        double[] pacf =
                new double[maxLag + 1];

        phi[1][1] = acf[0];
        pacf[1] = phi[1][1];

        for (int k = 2; k <= maxLag; k++) {

            double numerator = acf[k - 1];

            for (int j = 1; j < k; j++) {

                numerator -=
                        phi[k - 1][j]
                                *
                                acf[k - j - 1];
            }

            double denominator = 1.0;

            for (int j = 1; j < k; j++) {

                denominator -=
                        phi[k - 1][j]
                                *
                                acf[j - 1];
            }

            if (Math.abs(denominator) < 1e-10) {
                denominator = 1e-10;
            }

            phi[k][k] =
                    numerator / denominator;

            for (int j = 1; j < k; j++) {

                phi[k][j] =
                        phi[k - 1][j]
                                -
                                phi[k][k]
                                        *
                                        phi[k - 1][k - j];
            }

            pacf[k] = phi[k][k];
        }

        return pacf;
    }
}