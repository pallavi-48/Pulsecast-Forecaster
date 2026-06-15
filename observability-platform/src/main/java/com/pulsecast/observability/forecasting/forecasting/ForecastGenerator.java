package com.pulsecast.observability.forecasting.forecasting;

import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;

public class ForecastGenerator
        implements Forecaster {
    @Override
    public ForecastResult forecast(
            SarimaModel model,
            double[] traffic,
            int horizon
    ) {
        double[] forecastValues = new double[horizon];
        double[] lowerBounds = new double[horizon];
        double[] upperBounds = new double[horizon];

        int window = 50;
        double sum = 0;

        for (int i = traffic.length - window; i < traffic.length; i++) {
            sum += traffic[i];
        }

        double movingAverage = sum / window;
        double residualStd = model.getResidualStdDev();

        for (int i = 0; i < horizon; i++) {
            double predictedValue = Math.max(0, movingAverage);

            double margin = 1.96 * residualStd * Math.sqrt(i + 1);

            forecastValues[i] = predictedValue;
            lowerBounds[i] = Math.max(0, predictedValue - margin);
            upperBounds[i] = predictedValue + margin;
        }

        return new ForecastResult(
                forecastValues,
                lowerBounds,
                upperBounds
        );
    }
}