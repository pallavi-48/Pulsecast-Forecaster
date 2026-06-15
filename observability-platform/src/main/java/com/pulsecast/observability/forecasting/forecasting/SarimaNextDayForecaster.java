package com.pulsecast.observability.forecasting.forecasting;

import com.pulsecast.observability.forecasting.model.ForecastResult;
import com.pulsecast.observability.forecasting.model.SarimaModel;

public class SarimaNextDayForecaster {

    public ForecastResult forecastNextDay(
            SarimaModel model,
            double[] historicalTraffic
    ) {

        int horizon =
                model.getParameters()
                        .getSeasonLength();

        double[] forecastValues =
                new double[horizon];

        double[] lowerBounds =
                new double[horizon];

        double[] upperBounds =
                new double[horizon];

        double mean =
                model.getMeanTraffic();

        double ar =
                model.getArCoefficient();

        double ma =
                model.getMaCoefficient();

        double seasonal =
                model.getSeasonalCoefficient();

        double seasonalMa =
                model.getSeasonalMaCoefficient();

        double residualStd =
                model.getResidualStdDev();

        double previousValue =
                historicalTraffic[
                        historicalTraffic.length - 1
                        ];

        double previousResidual = 0.0;

        for (int i = 0; i < horizon; i++) {

            int seasonalIndex =
                    historicalTraffic.length
                            - horizon
                            + i;

            double seasonalValue =
                    historicalTraffic[
                            seasonalIndex
                            ];

            double seasonalResidual = 0.0;

            double prediction =
                    mean
                            + ar * (previousValue - mean)
                            + seasonal * (seasonalValue - mean)
                            + ma * previousResidual
                            + seasonalMa * seasonalResidual;

            double predictedValue =
                    Math.max(
                            0,
                            prediction
                    );

            forecastValues[i] =
                    predictedValue;

            double margin =
                    1.96
                            * residualStd
                            * Math.sqrt(i + 1);

            lowerBounds[i] =
                    Math.max(
                            0,
                            predictedValue - margin
                    );

            upperBounds[i] =
                    predictedValue + margin;

            previousValue =
                    predictedValue;
        }

        return new ForecastResult(
                forecastValues,
                lowerBounds,
                upperBounds
        );
    }
}