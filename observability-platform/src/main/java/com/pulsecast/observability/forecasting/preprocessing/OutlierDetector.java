package com.pulsecast.observability.forecasting.preprocessing;

public class OutlierDetector {

    public boolean[] detectOutliers(
            double[] values,
            double zThreshold
    ) {

        boolean[] outliers = new boolean[values.length];

        double mean = mean(values);
        double std = standardDeviation(values);

        for (int i = 0; i < values.length; i++) {

            double zScore =
                    Math.abs(values[i] - mean) / std;

            outliers[i] = zScore > zThreshold;
        }

        return outliers;
    }

    private double mean(double[] values) {

        double sum = 0;

        for (double v : values) {
            sum += v;
        }

        return sum / values.length;
    }

    private double standardDeviation(double[] values) {

        double mean = mean(values);

        double variance = 0;

        for (double v : values) {

            variance += Math.pow(v - mean, 2);
        }

        variance /= values.length;

        return Math.sqrt(variance);
    }
}