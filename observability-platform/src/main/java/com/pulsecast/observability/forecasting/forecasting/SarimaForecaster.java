package com.pulsecast.observability.forecasting.forecasting;

import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
public class SarimaForecaster
        implements Forecaster{

    private final double phi;

    public SarimaForecaster(double phi) {
        this.phi = phi;
    }
    @Override
    public ForecastResult forecast(
            SarimaModel model,
            double[] traffic,
            int horizon
    ) {
        double[] forecastValues = new double[horizon];
        double[] lowerBounds = new double[horizon];
        double[] upperBounds = new double[horizon];

        double mean = model.getMeanTraffic();
        double residualStd = model.getResidualStdDev();
        double previous = traffic[traffic.length - 1];

        for (int i = 0; i < horizon; i++) {
            double prediction =
                    mean + phi * (previous - mean);

            double predictedValue =
                    Math.max(0, prediction);

            double margin =
                    1.96 * residualStd * Math.sqrt(i + 1);

            forecastValues[i] = predictedValue;
            lowerBounds[i] = Math.max(0, predictedValue - margin);
            upperBounds[i] = predictedValue + margin;

            previous = predictedValue;
        }

        return new ForecastResult(
                forecastValues,
                lowerBounds,
                upperBounds
        );
    }
}