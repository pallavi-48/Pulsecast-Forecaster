package com.pulsecast.observability.forecasting.model;

public class ArForecaster {

    public double[] forecast(
            double phi,
            double lastValue,
            int horizon
    ) {

        double[] forecasts =
                new double[horizon];

        forecasts[0] =
                phi * lastValue;

        for (int i = 1; i < horizon; i++) {

            forecasts[i] =
                    phi * forecasts[i - 1];
        }

        return forecasts;
    }
}