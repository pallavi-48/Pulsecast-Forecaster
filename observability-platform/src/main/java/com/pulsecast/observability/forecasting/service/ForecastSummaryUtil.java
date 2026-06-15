package com.pulsecast.observability.forecasting.service;

public class ForecastSummaryUtil {

    public static double average(double[] values) {

        double sum = 0;

        for (double value : values) {
            sum += value;
        }

        return sum / values.length;
    }

    public static double min(double[] values) {

        double min =
                Double.MAX_VALUE;

        for (double value : values) {
            min = Math.min(min, value);
        }

        return min;
    }

    public static double max(double[] values) {

        double max =
                Double.MIN_VALUE;

        for (double value : values) {
            max = Math.max(max, value);
        }

        return max;
    }
}