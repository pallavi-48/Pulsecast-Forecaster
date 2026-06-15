package com.pulsecast.observability.forecasting.model;

public class ArModelTrainer {

    public double train(double[] differencedSeries) {

        double numerator = 0;
        double denominator = 0;

        for (int i = 1; i < differencedSeries.length; i++) {

            numerator +=
                    differencedSeries[i]
                            * differencedSeries[i - 1];

            denominator +=
                    differencedSeries[i - 1]
                            * differencedSeries[i - 1];
        }

        return numerator / denominator;
    }
}