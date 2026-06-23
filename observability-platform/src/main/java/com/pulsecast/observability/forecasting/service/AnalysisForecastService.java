package com.pulsecast.observability.forecasting.service;

import org.springframework.stereotype.Service;

@Service
public class AnalysisForecastService {

    public double[] forecast(
            double[] trainingValues,
            int horizon
    ) {
        /*
         * Connect your existing SARIMA model here later.
         *
         * For now, this uses seasonal naive forecasting:
         * prediction[t] = value from same time yesterday.
         *
         * Since your data has 5-minute intervals:
         * 288 points = 1 day.
         */

        int seasonLength = 288;

        double[] forecast =
                new double[horizon];

        for (int i = 0; i < horizon; i++) {

            int sourceIndex =
                    trainingValues.length - seasonLength + (i % seasonLength);

            if (sourceIndex < 0 || sourceIndex >= trainingValues.length) {
                forecast[i] =
                        trainingValues[trainingValues.length - 1];
            } else {
                forecast[i] =
                        trainingValues[sourceIndex];
            }
        }

        return forecast;
    }
}