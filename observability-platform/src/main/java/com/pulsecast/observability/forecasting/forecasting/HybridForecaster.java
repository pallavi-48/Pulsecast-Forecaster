package com.pulsecast.observability.forecasting.forecasting;

import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
public class HybridForecaster
        implements Forecaster {

    private final double phi;
    private final double alpha;

    public HybridForecaster(
            double phi,
            double alpha
    ) {
        this.phi = phi;
        this.alpha = alpha;
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

        int window = 50;
        double sum = 0;

        for (int i = traffic.length - window; i < traffic.length; i++) {
            sum += traffic[i];
        }

        double movingAverage = sum / window;
        double mean = model.getMeanTraffic();
        double residualStd = model.getResidualStdDev();
        double previous = traffic[traffic.length - 1];

        for (int i = 0; i < horizon; i++) {
            double arPrediction =
                    mean + phi * (previous - mean);

            double hybridPrediction =
                    alpha * movingAverage
                            + (1 - alpha) * arPrediction;

            double predictedValue =
                    Math.max(0, hybridPrediction);

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