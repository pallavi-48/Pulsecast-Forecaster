package com.pulsecast.observability.forecasting.preprocessing;

public class MissingValueImputer {

    public double[] forwardFill(double[] values) {

        double[] result = values.clone();

        for (int i = 1; i < result.length; i++) {

            if (Double.isNaN(result[i])) {

                result[i] = result[i - 1];
            }
        }

        return result;
    }
}