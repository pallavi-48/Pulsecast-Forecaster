package com.pulsecast.observability.forecasting.service;

import com.pulsecast.observability.forecasting.analysis.SarimaParameterSelector;
import com.pulsecast.observability.forecasting.domain.TimeSeries;
import com.pulsecast.observability.forecasting.loader.ForecastDatasetLoader;
import com.pulsecast.observability.forecasting.model.*;
import com.pulsecast.observability.forecasting.training.SarimaModelTrainer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ChartDataService {

    public ChartDataResponse getComparisonChart(
            MetricType metricType,
            String view
    ) {
        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historicalSeries =
                loader.loadTwoMonthDataset();

        TimeSeries currentSeries =
                loader.loadCurrent();

        MetricSeriesExtractor extractor =
                new MetricSeriesExtractor();

        double[] historicalValues =
                extractor.extract(
                        historicalSeries,
                        metricType
                );

        double[] currentValues =
                extractor.extract(
                        currentSeries,
                        metricType
                );

        int window =
                getWindowSize(
                        view,
                        historicalValues.length
                );

        double[] actualValues;
        double[] forecastValues;

        if ("daily".equalsIgnoreCase(view)) {

            SarimaModel model =
                    trainModel(
                            historicalValues
                    );

            actualValues =
                    currentValues;

            forecastValues =
                    forecastFuture(
                            model,
                            historicalValues,
                            currentValues.length
                    );

        } else {

            SarimaModel model =
                    trainModel(
                            historicalValues
                    );

            double[] fittedValues =
                    calculateFittedValues(
                            model,
                            historicalValues
                    );

            actualValues =
                    tail(
                            historicalValues,
                            window
                    );

            forecastValues =
                    tail(
                            fittedValues,
                            window
                    );
        }

        int finalLength =
                Math.min(
                        actualValues.length,
                        forecastValues.length
                );

        actualValues =
                Arrays.copyOfRange(
                        actualValues,
                        0,
                        finalLength
                );

        forecastValues =
                Arrays.copyOfRange(
                        forecastValues,
                        0,
                        finalLength
                );

        return new ChartDataResponse(
                metricType.name(),
                view,
                "Actual",
                "Forecast",
                buildLabels(finalLength),
                actualValues,
                forecastValues
        );
    }

    public ChartDataResponse getTrainingChart(
            MetricType metricType,
            String view
    ) {
        ForecastDatasetLoader loader =
                new ForecastDatasetLoader();

        TimeSeries historicalSeries =
                loader.loadTwoMonthDataset();

        MetricSeriesExtractor extractor =
                new MetricSeriesExtractor();

        double[] historicalValues =
                extractor.extract(
                        historicalSeries,
                        metricType
                );

        int window =
                getWindowSize(
                        view,
                        historicalValues.length
                );

        double[] trainingValues =
                tail(
                        historicalValues,
                        window
                );

        return new ChartDataResponse(
                metricType.name(),
                view,
                "Training Data",
                null,
                buildLabels(trainingValues.length),
                trainingValues,
                null
        );
    }

    private SarimaModel trainModel(
            double[] values
    ) {
        SarimaParameterSelector selector =
                new SarimaParameterSelector();

        SarimaParameters parameters =
                selector.selectParameters();

        SarimaModelTrainer trainer =
                new SarimaModelTrainer();

        return trainer.train(
                values,
                parameters
        );
    }

    private double[] forecastFuture(
            SarimaModel model,
            double[] historicalValues,
            int horizon
    ) {
        double[] forecast =
                new double[horizon];

        int seasonLength =
                model.getParameters()
                        .getSeasonLength();

        double[] residuals =
                model.getResiduals();

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

        double previousValue =
                historicalValues[
                        historicalValues.length - 1
                        ];

        double previousResidual =
                residuals[
                        residuals.length - 1
                        ];

        for (int i = 0; i < horizon; i++) {

            double seasonalValue;

            if (i < seasonLength) {
                seasonalValue =
                        historicalValues[
                                historicalValues.length
                                        - seasonLength
                                        + i
                                ];
            } else {
                seasonalValue =
                        forecast[
                                i - seasonLength
                                ];
            }

            double seasonalResidual =
                    i < seasonLength
                            ? residuals[
                              residuals.length
                              - seasonLength
                              + i
                              ]
                            : 0.0;

            double prediction =
                    mean
                            + ar * (previousValue - mean)
                            + seasonal * (seasonalValue - mean)
                            + ma * previousResidual
                            + seasonalMa * seasonalResidual;

            forecast[i] =
                    Math.max(
                            0,
                            prediction
                    );

            previousValue =
                    forecast[i];

            previousResidual =
                    0.0;
        }

        return forecast;
    }

    private double[] calculateFittedValues(
            SarimaModel model,
            double[] values
    ) {
        double[] fitted =
                new double[values.length];

        int seasonLength =
                model.getParameters()
                        .getSeasonLength();

        double[] residuals =
                model.getResiduals();

        double mean =
                model.getMeanTraffic();

        for (int i = 0; i < values.length; i++) {

            if (i < seasonLength) {
                fitted[i] =
                        values[i];
                continue;
            }

            double prediction =
                    mean
                            + model.getArCoefficient()
                            * (values[i - 1] - mean)
                            + model.getSeasonalCoefficient()
                            * (values[i - seasonLength] - mean)
                            + model.getMaCoefficient()
                            * residuals[i - 1]
                            + model.getSeasonalMaCoefficient()
                            * residuals[i - seasonLength];

            fitted[i] =
                    Math.max(
                            0,
                            prediction
                    );
        }

        return fitted;
    }

    private int getWindowSize(
            String view,
            int fullLength
    ) {
        if ("daily".equalsIgnoreCase(view)) {
            return 288;
        }

        if ("weekly".equalsIgnoreCase(view)) {
            return 2016;
        }

        if ("monthly".equalsIgnoreCase(view)) {
            return 8640;
        }

        if ("twomonth".equalsIgnoreCase(view)) {
            return fullLength;
        }

        return 288;
    }

    private double[] tail(
            double[] values,
            int size
    ) {
        int safeSize =
                Math.min(
                        size,
                        values.length
                );

        return Arrays.copyOfRange(
                values,
                values.length - safeSize,
                values.length
        );
    }

    private List<String> buildLabels(
            int size
    ) {
        List<String> labels =
                new ArrayList<>();

        for (int i = 0; i < size; i++) {
            labels.add(
                    String.valueOf(i + 1)
            );
        }

        return labels;
    }
}