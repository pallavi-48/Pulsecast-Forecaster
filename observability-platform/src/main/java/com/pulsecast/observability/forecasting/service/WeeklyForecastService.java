package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;
import org.springframework.stereotype.Service;

@Service
public class WeeklyForecastService {

    public ForecastResult forecastNextWeek(
            SarimaModel model,
            double[] historical
    ) {

        int pointsPerDay =
                model.getParameters().getSeasonLength();

        int horizon =
                pointsPerDay * 7;

        double[] forecast =
                new double[horizon];

        double mean =
                model.getMeanTraffic();

        double ar =
                model.getArCoefficient();

        double seasonal =
                model.getSeasonalCoefficient();

        double previous =
                historical[historical.length - 1];

        for (int i = 0; i < horizon; i++) {

            int seasonalIndex =
                    historical.length
                            - pointsPerDay
                            + (i % pointsPerDay);

            double seasonalValue =
                    historical[seasonalIndex];

            double prediction =
                    mean
                            + ar * (previous - mean)
                            + seasonal * (seasonalValue - mean);

            forecast[i] =
                    Math.max(0, prediction);

            previous =
                    forecast[i];
        }

        return new ForecastResult(forecast);
    }
}