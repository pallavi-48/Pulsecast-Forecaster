package com.pulsecast.observability.forecasting.preprocessing;

public class DifferencingTransformer {

    public double[] difference(double[] values) {

        double[] result =
                new double[values.length - 1];

        for (int i = 1; i < values.length; i++) {

            result[i - 1] =
                    values[i] - values[i - 1];
        }

        return result;
    }
}